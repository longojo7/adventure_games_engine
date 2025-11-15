package engine.model;

import java.util.List;

import engine.Direction;

public class DummyModel implements IEngineModel {
  private int westStepsTaken;
  private boolean monsterActive = true;

  public int getWestStepsTaken() {
    return westStepsTaken;
  }

  @Override
  public boolean walk(Direction direction) {
    return false;
  }

  @Override
  public String peek(Direction direction) {
    switch (direction) {
      case null:
      case North:
      case South:
        return null;
      case East:
        return "MORE HALLWAY TO THE EAST";
      case West:
        return "MORE HALLWAY TO THE WEST";
    }
  }

  @Override
  public List<String> playerItemList() {
    return List.of("Player Item 1", "Player Item 2", "Player Item 3");
  }

  @Override
  public List<String> roomItemList() {
    return List.of("Room Item 1", "Room Item 2", "Room Item 3");
  }

  @Override
  public boolean pickupItem(String itemName) {
    return roomItemList().contains(itemName);
  }

  @Override
  public boolean dropItem(String itemName) {
    return playerItemList().contains(itemName);
  }

  @Override
  public String examine(String target) {
    switch (target) {
      case "Player Item 1":
        return "This is player item 1";
      case "Player Item 2":
        return "This is player item 2";
      case "Player Item 3":
        return "This is player item 3";
      case "Room Item 1":
        return "This is room item 1";
      case "Room Item 2":
        return "This is room item 2";
      case "Room Item 3":
        return "This is room item 3";
      case "Monster":
        return "This is the evil monster";
      default:
        return "There is nothing to examine with that name.";
    }
  }

  @Override
  public boolean useItem(String itemName) {
    return itemName.equals("KEY_ITEM");
  }

  @Override
  public boolean answerQuestion(String answer) {
    return answer.equals("PASSWORD");
  }

  @Override
  public String playerStatus() {
    return "HEALTHY";
  }

  @Override
  public String roomName() {
    return "HALLWAY";
  }

  @Override
  public String environmentDescription() {
    return "You are in a long hallway stretching to the east and west.";
  }

  @Override
  public String monsterAttack() {
    return "The monster attacks you nonviolently. You take 5 damage.";
  }
}
