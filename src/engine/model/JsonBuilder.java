package engine.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.Reader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import engine.Direction;
import engine.model.items.IInventory;
import engine.model.items.InfiniteInventory;
import engine.model.items.Item;
import engine.model.items.ItemFactory;
import engine.model.items.LimitedInventory;
import engine.model.player.Player;
import engine.model.player.ThresholdHealth;
import engine.model.rooms.Connection;
import engine.model.rooms.Fixture;
import engine.model.rooms.IRoom;
import engine.model.rooms.Room;
import engine.model.rooms.obstacles.IObstacle;
import engine.model.rooms.obstacles.ISolution;
import engine.model.rooms.obstacles.ItemSolution;
import engine.model.rooms.obstacles.Monster;
import engine.model.rooms.obstacles.PasswordSolution;
import engine.model.rooms.obstacles.Puzzle;

/**
 * Builder that creates game models from JSON configuration.
 */
public class JsonBuilder implements IGameBuilder {
  private JSONObject json;
  private JSONObject playerData;
  private Map<String, JSONObject> jsonRooms;
  private Map<String, JSONObject> jsonFixtures;
  private Map<String, JSONObject> jsonObstacles;
  private String fileName;

  /**
   * Constructs a JSON builder from a reader.
   *
   * @param jsonReader the reader containing JSON data
   */
  public JsonBuilder(Reader jsonReader, String fileName) {
    this.fileName = fileName;
    JSONParser parser = new JSONParser();
    Object parsedData;

    // Handle only the checked exceptions declared by parser.parse() when JSON is invalid
    try {
      parsedData = parser.parse(jsonReader);
    } catch (IOException | ParseException e) {
      throw new IllegalArgumentException(
              "Could not read or parse game file '" + fileName + "': "
                      + e.getMessage(),
              e);
    }

    // Validation exceptions propagate directly to main's error handler.
    if (!(parsedData instanceof JSONObject)) {
      throw new IllegalArgumentException(
              "The game file must contain a JSON object.");
    }

    json = (JSONObject) parsedData;

    if (!(json.get("rooms") instanceof JSONArray)) {
      throw new IllegalArgumentException(
              "The game file must contain a 'rooms' array.");
    }

    // If init fails, construction stops automatically
    // no partially initialized JsonBuilder is returned.
    if (json.get("player") != null) {
      playerData = (JSONObject) json.get("player");
    }

    jsonRooms = new HashMap<>();
    for (Object object : (JSONArray) json.get("rooms")) {
      JSONObject jsonRoom = (JSONObject) object;
      jsonRooms.put(jsonRoom.get("room_number").toString(), jsonRoom);
    }

    jsonFixtures = new HashMap<>();
    if (json.get("fixtures") != null) {
      for (Object object : (JSONArray) json.get("fixtures")) {
        JSONObject jsonFixture = (JSONObject) object;
        jsonFixtures.put(jsonFixture.get("name").toString(), jsonFixture);
      }
    }

    jsonObstacles = new HashMap<>();
    if (json.get("monsters") != null) {
      for (Object object : (JSONArray) json.get("monsters")) {
        JSONObject jsonObstacle = (JSONObject) object;
        jsonObstacles.put(jsonObstacle.get("name").toString(), jsonObstacle);
      }
    }

    if (json.get("puzzles") != null) {
      for (Object object : (JSONArray) json.get("puzzles")) {
        JSONObject jsonObstacle = (JSONObject) object;
        jsonObstacles.put(jsonObstacle.get("name").toString(), jsonObstacle);
      }
    }
  }

  @Override
  public IEngineModel createModel(String playerName) throws Exception {
    itemFactorySetup();

    Map<String, IRoom> rooms = new HashMap<>();
    Map<String, Map<Direction, Connection>> connectionsForEachRoom = new HashMap<>();
    for (String key : jsonRooms.keySet()) {
      Map<Direction, Connection> connections = new HashMap<>();
      connectionsForEachRoom.put(key, connections);
      rooms.put(key, createRoom(jsonRooms.get(key), connections));
    }
    connectRooms(rooms, connectionsForEachRoom);

    Map<Integer, String> thresholdMap = new HashMap<>();
    thresholdMap.put(0, "Asleep");
    thresholdMap.put(40, "Exhausted");
    thresholdMap.put(60, "Tired");
    thresholdMap.put(100, "Awake");

    Player player;

    if (playerData == null) {
      player = new Player(
              playerName,
              new ThresholdHealth(100, 100, thresholdMap),
              new LimitedInventory(new ArrayList<Item>(), 20),
              rooms.get("1")
      );
      for (IRoom room : rooms.values()) {
        if (room.getObstacle() != null)
          room.getObstacle().addEventHandler(
                  event -> player.addObstacleScore(room.getObstacle().getValue())
          );
      }
    } else {
      player = new Player(
              playerData.get("name") == null ? playerName : playerData.get("name").toString(),
              new ThresholdHealth(
                      Integer.parseInt(playerData.get("current_health").toString()),
                      Integer.parseInt(playerData.get("max_health").toString()),
                      thresholdMap
              ),
              new LimitedInventory(
                      itemListFromString(playerData.get("items").toString()), 20),
              rooms.get(playerData.get("room_number").toString())
      );
      for (IRoom room : rooms.values()) {
        if (room.getObstacle() != null)
          room.getObstacle().addEventHandler(
                  event -> player.addObstacleScore(room.getObstacle().getValue())
          );
      }
      for (IRoom room : rooms.values()) {
        for (Item item : player.itemsList())
          if (room.getInventory().hasItem(item.getName()))
            room.getInventory().removeItem(item.getName());
        String solvedRoomsStr = playerData.get("solved_rooms").toString();
        if (!solvedRoomsStr.isEmpty()) {
          for (String solvedRoom : solvedRoomsStr.split(", ")) {
            if (rooms.get(solvedRoom) == room && room.getObstacle() != null) {
              room.getObstacle().forceSolve();
            }
          }
        }
      }
    }
    return new EngineModel(player, rooms, fileName);
  }

  /**
   * Generates JSON player data from the player and rooms.
   *
   * @param player the player
   * @param rooms map of rooms
   * @return JSON object containing player data
   */
  private JSONObject generatePlayerData(Player player, Map<String, IRoom> rooms) {
    JSONObject newPlayerData = new JSONObject();
    newPlayerData.put("name", player.getName());
    newPlayerData.put("current_health", player.currentHealth());
    newPlayerData.put("max_health", player.maxHealth());
    newPlayerData.put("items", listToString(player.itemsList().stream()
            .map(item -> item.getName()).collect(Collectors.toList())
    ));
    List<String> solvedRoomNumbers = new ArrayList<>();
    for (String key : rooms.keySet()) {
      if (rooms.get(key) == player.getCurrentRoom())
        newPlayerData.put("room_number", key);
      if(rooms.get(key).getObstacle() != null && !rooms.get(key).getObstacle().isActive())
        solvedRoomNumbers.add(key);
    }
    newPlayerData.put("solved_rooms", listToString(solvedRoomNumbers));
    return newPlayerData;
  }

  @Override
  public String exportModel(IEngineModel model) {
    json.put("player", generatePlayerData(model.getPlayer(), model.getRooms()));
    return json.toJSONString();
  }

  /**
   * Parses a comma-separated string into a list of items.
   *
   * @param string the comma-separated item names
   * @return list of items
   */
  private List<Item> itemListFromString(String string) {
    List<Item> items = new ArrayList<>();
    if (string.isEmpty()) {
      return items;
    }
    for (String itemName : string.split(", ")) {
      Item item = ItemFactory.getInstance().getItem(itemName);
      if (item != null) {
        items.add(item);
      }
    }

    return items;
  }

  /**
   * Converts a list to a comma-separated string.
   *
   * @param list the list to convert
   * @return comma-separated string
   */
  private String listToString(List list) {
    if (list.isEmpty())
      return "";
    StringBuilder sb = new StringBuilder();
    sb.append(list.get(0).toString());
    for (int i = 1; i < list.size(); i++) {
      sb.append(", ");
      sb.append(list.get(i).toString());
    }
    return sb.toString();
  }

  /**
   * Creates a room from JSON data.
   *
   * @param jsonRoom the JSON room object
   * @param connections the connections map for this room
   * @return the created room
   * @throws Exception if room creation fails
   */
  private IRoom createRoom(
          JSONObject jsonRoom,
          Map<Direction, Connection> connections
  ) throws Exception {
    String name = jsonRoom.get("room_name").toString();
    String description = jsonRoom.get("description").toString();
    IObstacle obstacle = null;
    if (jsonRoom.get("puzzle") != null) {
      String puzzleName = jsonRoom.get("puzzle").toString().toUpperCase();

      if (jsonObstacles.containsKey(puzzleName)) {
        obstacle = createPuzzle(puzzleName);
      }
    } else if (jsonRoom.get("monster") != null) {
      String monsterName = jsonRoom.get("monster").toString();

      if (jsonObstacles.containsKey(monsterName)) {
        obstacle = createMonster(monsterName);
      }
    }
    List<Fixture> fixtures = new ArrayList<>();
    if (jsonRoom.get("fixtures") != null)
      for (String fixtureName : jsonRoom.get("fixtures").toString().split(", "))
        if (jsonFixtures.get(fixtureName) != null)
          fixtures.add(createFixture(fixtureName));
    List<Item> items = new ArrayList<>();
    if (jsonRoom.get("items") != null)
      items = itemListFromString(jsonRoom.get("items").toString());
    IInventory inventory = new InfiniteInventory(items);
    return new Room(
            name,
            description,
            connections,
            fixtures,
            obstacle,
            inventory,
            "data/images/" + jsonRoom.get("picture")
    );
  }

  /**
   * Creates a fixture from its name.
   *
   * @param fixtureName the name of the fixture
   * @return the created fixture
   * @throws Exception if fixture does not exist
   */
  private Fixture createFixture(String fixtureName) throws Exception {
    if (jsonFixtures.get(fixtureName) == null)
      throw new Exception("Invalid JSON: Fixture " + fixtureName + " does not exist.");
    JSONObject jsonFixture = jsonFixtures.get(fixtureName);
    return new Fixture(
            jsonFixture.get("name").toString(),
            jsonFixture.get("description").toString()
    );
  }

  /**
   * Creates a puzzle from its name.
   *
   * @param puzzleName the name of the puzzle
   * @return the created puzzle
   * @throws Exception if puzzle does not exist
   */
  private Puzzle createPuzzle(String puzzleName) throws Exception {
    puzzleName = puzzleName.toUpperCase();
    if (jsonObstacles.get(puzzleName) == null)
      throw new Exception("Invalid JSON: Puzzle " + puzzleName + " does not exist.");
    JSONObject jsonPuzzle = jsonObstacles.get(puzzleName);

    ISolution correctSolution;
    String solutionString = jsonPuzzle.get("solution").toString();
    boolean passwordPuzzle = solutionString.startsWith("'") && solutionString.endsWith("'");
    if (passwordPuzzle)
      correctSolution = new PasswordSolution(
              solutionString.substring(1, solutionString.length() - 1)
      );
    else
      correctSolution = new ItemSolution(
              Arrays.stream(solutionString.split(", ")).map(
                      string -> ItemFactory.getInstance().getItem(string)
              ).collect(Collectors.toList())
      );

    String messageOnSolve = "Correct answer!";
    if (!passwordPuzzle)
      messageOnSolve = getMessageOnSolve(solutionString);

    return new Puzzle(
            jsonPuzzle.get("name").toString(),
            correctSolution,
            jsonPuzzle.get("effects").toString(),
            jsonPuzzle.get("description").toString(),
            Integer.parseInt(jsonPuzzle.get("value").toString()),
            messageOnSolve,
            !Boolean.parseBoolean(jsonPuzzle.get("active").toString()),
            "data/images/" + jsonPuzzle.get("picture")
    );
  }

  /**
   * Creates a monster from its name.
   *
   * @param monsterName the name of the monster
   * @return the created monster
   * @throws Exception if monster does not exist
   */
  private Monster createMonster(String monsterName) throws Exception {
    if (jsonObstacles.get(monsterName) == null)
      throw new Exception("Invalid JSON: Monster " + monsterName + " does not exist.");
    JSONObject jsonMonster = jsonObstacles.get(monsterName);

    ISolution correctSolution;
    String solutionString = jsonMonster.get("solution").toString();
    boolean passwordPuzzle = solutionString.startsWith("'") && solutionString.endsWith("'");
    if (passwordPuzzle)
      correctSolution = new PasswordSolution(
              solutionString.substring(1, solutionString.length() - 1)
      );
    else
      correctSolution = new ItemSolution(
              Arrays.stream(solutionString.split(", ")).map(
                      string -> ItemFactory.getInstance().getItem(string)
              ).collect(Collectors.toList())
      );

    String messageOnSolve = "Correct answer!";
    if (!passwordPuzzle)
      messageOnSolve = getMessageOnSolve(solutionString);

    return new Monster(
            jsonMonster.get("name").toString(),
            correctSolution,
            jsonMonster.get("effects").toString(),
            jsonMonster.get("description").toString(),
            -Integer.parseInt(jsonMonster.get("damage").toString()),
            jsonMonster.get("attack").toString(),
            Integer.parseInt(jsonMonster.get("value").toString()),
            messageOnSolve,
            !Boolean.parseBoolean(jsonMonster.get("active").toString()),
            "data/images/" + jsonMonster.get("picture")
    );
  }

  /**
   * Sets up the item factory from JSON data.
   */
  private void itemFactorySetup() {
    JSONArray jsonItems = json.get("items") instanceof JSONArray
            ? (JSONArray) json.get("items")
            : new JSONArray();

    ItemFactory.getInstance().itemsFromJson(jsonItems);
  }

  /**
   * Connects rooms based on JSON configuration.
   *
   * @param rooms map of all rooms
   * @param connectionsForEachRoom map of connections for each room
   */
  private void connectRooms(Map<String, IRoom> rooms, Map<String, Map<Direction, Connection>> connectionsForEachRoom) {
    for (String roomName : rooms.keySet()) {
      Map<Direction, Connection> connections = connectionsForEachRoom.get(roomName);
      JSONObject roomJson = jsonRooms.get(roomName);
      String north = roomJson.get("N").toString();
      if (!north.equals("0")) {
        boolean isOpen = !north.startsWith("-");
        String destination = isOpen ? north : north.substring(1);
        connections.put(Direction.North, new Connection(isOpen, rooms.get(destination)));
        if (!isOpen)
          rooms.get(roomName).getObstacle().addEventHandler(gameEvent -> {
            connections.get(Direction.North).setOpen(true);
          });
      }
      String south = roomJson.get("S").toString();
      if (!south.equals("0")) {
        boolean isOpen = !south.startsWith("-");
        String destination = isOpen ? south : south.substring(1);
        connections.put(Direction.South, new Connection(isOpen, rooms.get(destination)));
        if (!isOpen)
          rooms.get(roomName).getObstacle().addEventHandler(gameEvent -> {
            connections.get(Direction.South).setOpen(true);
          });
      }
      String east = roomJson.get("E").toString();
      if (!east.equals("0")) {
        boolean isOpen = !east.startsWith("-");
        String destination = isOpen ? east : east.substring(1);
        connections.put(Direction.East, new Connection(isOpen, rooms.get(destination)));
        if (!isOpen)
          rooms.get(roomName).getObstacle().addEventHandler(gameEvent -> {
            connections.get(Direction.East).setOpen(true);
          });
      }
      String west = roomJson.get("W").toString();
      if (!west.equals("0")) {
        boolean isOpen = !west.startsWith("-");
        String destination = isOpen ? west : west.substring(1);
        connections.put(Direction.West, new Connection(isOpen, rooms.get(destination)));
        if (!isOpen)
          rooms.get(roomName).getObstacle().addEventHandler(gameEvent -> {
            connections.get(Direction.West).setOpen(true);
          });
      }
    }
  }

  /**
   * Gets the message to display when an item is used.
   *
   * @param itemName the name of the item
   * @return the message when item is used
   */
  private String getMessageOnSolve(String itemName) {
    return ((JSONObject) (((JSONArray) json.get("items")).stream().filter(
            object -> ((JSONObject) object).get("name").toString().equals(itemName)
    ).findFirst().get())).get("when_used").toString();
  }

  public String getFileName() {
    return fileName;
  }
}