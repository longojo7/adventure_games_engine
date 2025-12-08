package engine.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import engine.Direction;
import engine.model.items.Item;
import engine.model.player.Player;
import engine.model.rooms.Fixture;
import engine.model.rooms.IRoom;
import engine.model.rooms.obstacles.ItemSolution;
import engine.model.rooms.obstacles.PasswordSolution;

/**
 * Implementation of the game engine model.
 */
public class EngineModel implements IEngineModel {
  Player player;
  Map<String, IRoom> rooms;

  /**
   * Constructs an engine model with player and rooms.
   *
   * @param player the player
   * @param rooms map of room names to rooms
   */
  public EngineModel(Player player, Map<String, IRoom> rooms) {
    this.player = player;
    this.rooms = rooms;
  }

  @Override
  public void setPlayerName(String name) {
    player.setName(name);
  }

  @Override
  public String getPlayerName() {
    return player.getName();
  }

  @Override
  public int getPlayerScore() {
    return player.getScore();
  }

  @Override
  public String getPlayerRank() {
    if (getPlayerScore() >= 100) {
      return "SKILLED";
    } else if (getPlayerScore() >= 50) {
      return "MASTER";
    }
    return "NOVICE";
  }

  @Override
  public boolean walk(Direction direction) {
    boolean success = player.walk(direction);
    damagePlayer();
    return success;
  }

  @Override
  public String peek(Direction direction) {
    IRoom room = player.peek(direction);
    return room == null ? null : room.getName();
  }

  /**
   * Gets list of fixture names in current room.
   *
   * @return list of fixture names
   */
  public List<String> fixtureList() {
    return player.getCurrentRoom().getFixtures().stream().
            map(Fixture::getName).collect(Collectors.toList());
  }

  @Override
  public List<String> playerItemList() {
    return player.itemsList().stream().map(Item::getName)
            .collect(Collectors.toList());
  }

  @Override
  public List<String> roomItemList() {
    return player.getCurrentRoom().getInventory().getItems().stream().map(Item::getName)
            .collect(Collectors.toList());
  }

  @Override
  public boolean pickupItem(String itemName) {
    damagePlayer();
    return player.pickupItem(itemName);
  }

  @Override
  public boolean dropItem(String itemName) {
    damagePlayer();
    return player.dropItem(itemName);
  }

  @Override
  public String examine(String target) {
    if (player.getCurrentRoom().getInventory().hasItem(target) || player.hasItem(target)) {
      return Item.fromName(target).getDescription();
    } else if (player.getCurrentRoom().getObstacle() != null
            && player.getCurrentRoom().getObstacle().getName().equalsIgnoreCase(target)) {
      return player.getCurrentRoom().getObstacle().getDescription();
    } else if (player.getCurrentRoom().getFixtures()
            .stream().anyMatch(x -> x.getName().equalsIgnoreCase(target)))
      return player.getCurrentRoom().getFixtures()
              .stream().filter(x -> x.getName().equalsIgnoreCase(target))
              .findFirst().get().getDescription();
    else
      return "Nothing";
  }

  @Override
  public String useItem(String itemName) {
    Item item = Item.fromName(itemName);
    if (item == null || !player.hasItem(itemName)) {
      return "no item";
    }
    ItemSolution solution = new ItemSolution(List.of(Item.fromName(itemName)));
    if (player.getCurrentRoom().getObstacle() == null
            || !player.getCurrentRoom().getObstacle().attemptSolution(solution)) {
      damagePlayer();
      return "no use";
    }
    return player.getCurrentRoom().getObstacle().getMessageOnSolve();
  }

  @Override
  public boolean answerQuestion(String answer) {
    if (player.getCurrentRoom().getObstacle().attemptSolution(new PasswordSolution(answer)))
      return true;
    damagePlayer();
    return false;
  }

  @Override
  public String playerStatus() {
    return player.healthStatus();
  }

  @Override
  public String roomName() {
    return player.getCurrentRoom().getName();
  }

  @Override
  public String environmentDescription() {
    return player.getCurrentRoom().getDescription();
  }

  @Override
  public String getImage() {
    return player.getCurrentRoom().getImagePath();
  }

  @Override
  public String monsterAttack() {
    if (player.getCurrentRoom().getObstacle() == null)
      return null;
    return player.getCurrentRoom().getObstacle().getName() + " "
            + player.getCurrentRoom().getObstacle().getAttack();
  }

  @Override
  public int monsterDamage() {
    return player.getCurrentRoom().getObstacle() != null
            && player.getCurrentRoom().getObstacle().isActive()
            ? player.getCurrentRoom().getObstacle().getDamage()
            : 0;
  }

  @Override
  public boolean roomObstacleActive() {
    return player.getCurrentRoom().getObstacle() != null
            && player.getCurrentRoom().getObstacle().isActive();
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public Map<String, IRoom> getRooms() {
    return rooms;
  }

  @Override
  public boolean damagePlayer() {
    if (player.getCurrentRoom().getObstacle() == null
            || !player.getCurrentRoom().getObstacle().isActive())
      return false;
    int damage = player.getCurrentRoom().getObstacle().getDamage();
    player.damage(damage);
    return damage != 0;
  }

  @Override
  public String getFileName() {
    return
  }

}