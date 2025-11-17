package engine.model;

import java.util.Collections;
import java.util.List;
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
    // TODO: Need to add player rank functionality in the model
    return "";
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
    if (player.hasItem(target))
      return Item.fromID(target).getDescription();
    else if (player.getCurrentRoom().getObstacle().getName().equals(target))
      return player.getCurrentRoom().getObstacle().getDescription();
    else if (player.getCurrentRoom().getFixtures()
            .stream().anyMatch(x -> x.getName().equals(target)))
      return player.getCurrentRoom().getFixtures()
              .stream().filter(x -> x.getName().equals(target))
              .findFirst().get().getDescription();
    else
      return null;
  }

  @Override
  public boolean useItem(String itemName) {
    ItemSolution solution = new ItemSolution(List.of(Item.fromID(itemName)));
    if (!player.hasItem(itemName)
            || !player.getCurrentRoom().getObstacle().attemptSolution(solution))
      return false;
    takeDamage();
    return true;
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

  private boolean obstacleIsMonster() {
    return player.getCurrentRoom().getObstacle() instanceof Monster;
  }

  private void takeDamage() {
    if (obstacleIsMonster())
      player.damage(((Monster) player.getCurrentRoom().getObstacle()).getDamage());
  }
}
