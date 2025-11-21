package engine.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.Reader;
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

public class JsonBuilder implements IGameBuilder {
  private JSONObject json;
  private JSONObject playerData;
  private Map<String, JSONObject> jsonRooms;
  private Map<String, JSONObject> jsonFixtures;
  private Map<String, JSONObject> jsonObstacles;

  public JsonBuilder(Reader jsonReader) {
    JSONParser parser = new JSONParser();
    try {
      json = (JSONObject) parser.parse(jsonReader);
      if (json.get("player") != null)
        playerData = (JSONObject) json.get("player");
      jsonRooms = new HashMap<>();
      for (Object o : (JSONArray) json.get("rooms")) {
        JSONObject jsonRoom = (JSONObject) o;
        jsonRooms.put(jsonRoom.get("room_number").toString(), jsonRoom);
      }
      jsonFixtures = new HashMap<>();
      if (json.get("fixtures") != null)
        for (Object o : (JSONArray) json.get("fixtures")) {
          JSONObject jsonFixture = (JSONObject) o;
          jsonFixtures.put(jsonFixture.get("name").toString(), jsonFixture);
        }
      jsonObstacles = new HashMap<>();
      if (json.get("monsters") != null)
        for (Object o : (JSONArray) json.get("monsters")) {
          JSONObject jsonObstacle = (JSONObject) o;
          jsonObstacles.put(jsonObstacle.get("name").toString(), jsonObstacle);
        }
      if (json.get("puzzles") != null)
        for (Object o : (JSONArray) json.get("puzzles")) {
          JSONObject jsonObstacle = (JSONObject) o;
          jsonObstacles.put(jsonObstacle.get("name").toString(), jsonObstacle);
        }
    } catch (Exception e) {
      e.printStackTrace();
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
          if (room.getInventory().hasItem(item.getId()))
            room.getInventory().removeItem(item.getId());
        for (String solvedRoom : playerData.get("solved_rooms").toString().split(", "))
          if (rooms.get(solvedRoom) == room)
            room.getObstacle().forceSolve();
      }
    }

    return new EngineModel(player, rooms);
  }

  private JSONObject generatePlayerData(Player player, Map<String, IRoom> rooms) {
    JSONObject newPlayerData = new JSONObject();
    newPlayerData.put("name", player.getName());
    newPlayerData.put("current_health", player.currentHealth());
    newPlayerData.put("max_health", player.maxHealth());
    newPlayerData.put("items", listToString(
            player.itemsList().stream().map(item -> item.getId()).collect(Collectors.toList())
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

  private List<Item> itemListFromString(String string) {
    List<Item> items = new ArrayList<>();
    if (string.isEmpty()) {
      return items;
    }
    for (String itemName : string.split(", "))
      items.add(ItemFactory.getInstance().getItem(itemName));
    return items;
  }

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

  private IRoom createRoom(
          JSONObject jsonRoom,
          Map<Direction, Connection> connections
  ) throws Exception {
    String name = jsonRoom.get("room_name").toString();
    String description = jsonRoom.get("description").toString();
    IObstacle obstacle = null;
    if (jsonRoom.get("puzzle") != null)
      obstacle = createPuzzle(jsonRoom.get("puzzle").toString());
    else if (jsonRoom.get("monster") != null)
      obstacle = createMonster(jsonRoom.get("monster").toString());
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
            inventory
    );
  }

  private Fixture createFixture(String fixtureName) throws Exception {
    if (jsonFixtures.get(fixtureName) == null)
      throw new Exception("Invalid JSON: Fixture " + fixtureName + " does not exist.");
    JSONObject jsonFixture = jsonFixtures.get(fixtureName);
    return new Fixture(
            jsonFixture.get("name").toString(),
            jsonFixture.get("description").toString()
    );
  }

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
            !Boolean.parseBoolean(jsonPuzzle.get("active").toString())
    );
  }

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
            !Boolean.parseBoolean(jsonMonster.get("active").toString())
    );
  }

  private void itemFactorySetup() {
    if (json.get("items") instanceof JSONArray)
      ItemFactory.getInstance().itemsFromJson((JSONArray) json.get("items"));
  }

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

  private String getMessageOnSolve(String itemName) {
    return ((JSONObject) (((JSONArray) json.get("items")).stream().filter(
            object -> ((JSONObject) object).get("name").toString().equals(itemName)
    ).findFirst().get())).get("when_used").toString();
  }
}
