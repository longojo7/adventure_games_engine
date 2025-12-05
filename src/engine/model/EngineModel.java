package engine.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import engine.Direction;
import engine.model.items.Item;
import engine.model.player.Player;
import engine.model.rooms.IRoom;
import engine.model.rooms.obstacles.ItemSolution;
import engine.model.rooms.obstacles.Monster;
import engine.model.rooms.obstacles.PasswordSolution;

public class EngineModel implements IEngineModel {
  Player player;
  Map<String, IRoom> rooms;

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
    if (getPlayerScore() >= 50) {
      return "MASTER";
    } else if (getPlayerScore() >= 100) {
      return "SKILLED";
    }
    return "NOVICE";
  }

  @Override
  public boolean walk(Direction direction) {
    return player.walk(direction);
  }

  @Override
  public String peek(Direction direction) {
    IRoom room = player.peek(direction);
    return room == null ? null : room.getName();
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
    takeDamage();
    return player.pickupItem(itemName);
  }

  @Override
  public boolean dropItem(String itemName) {
    takeDamage();
    return player.dropItem(itemName);
  }

  @Override
  public String examine(String target) {
    if (player.getCurrentRoom().getInventory().hasItem(target)) {
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
    if (item == null || !player.hasItem(itemName)) {return "no item";}
    ItemSolution solution = new ItemSolution(List.of(Item.fromName(itemName)));
    if (player.getCurrentRoom().getObstacle() == null
            || !player.getCurrentRoom().getObstacle().attemptSolution(solution)) {return "no use";}
    takeDamage();
    return "can use";
  }

  @Override
  public boolean answerQuestion(String answer) {
    if (!player.getCurrentRoom().getObstacle().attemptSolution(new PasswordSolution(answer)))
      return false;
    takeDamage();
    return true;
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
  public String monsterAttack() {
    if (!obstacleIsMonster())
      return null;
    Monster monster = (Monster) player.getCurrentRoom().getObstacle();
    return monster.getAttack();
  }

  @Override
  public int dealDamage() {

    if (player.getCurrentRoom().getObstacle() instanceof Monster && player.getCurrentRoom().getObstacle().isActive())
      player.damage(((Monster)(player.getCurrentRoom().getObstacle())).getDamage());
    return ((Monster)(player.getCurrentRoom().getObstacle())).getDamage();
  }

  @Override
  public boolean roomObstacleActive() {
    return player.getCurrentRoom().getObstacle()!=null && player.getCurrentRoom().getObstacle().isActive();
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public Map<String, IRoom> getRooms() {
    return rooms;
  }

  private boolean obstacleIsMonster() {
    return player.getCurrentRoom().getObstacle() instanceof Monster;
  }

  private void takeDamage() {
    if (obstacleIsMonster())
      player.damage(((Monster) player.getCurrentRoom().getObstacle()).getDamage());
  }
}
