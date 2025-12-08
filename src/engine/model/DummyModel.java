package engine.model;

import java.util.List;
import java.util.Map;

import engine.Direction;
import engine.model.player.Player;
import engine.model.rooms.IRoom;

public class DummyModel implements IEngineModel {
  private String name;
  private int score = 0;
  private int westStepsTaken;
  private boolean monsterActive = true;

  public int getWestStepsTaken() {
    return westStepsTaken;
  }

  @Override
  public void setPlayerName(String name) {
    this.name = name;
  }

  @Override
  public String getPlayerName() {
    return name;
  }

  @Override
  public int getPlayerScore() {
    // simple score adds or subtracts 10 if item is picked up or dropped
    return score;
  }

  @Override
  public String getPlayerRank() {
    if (getPlayerScore() >= 20) {
      return "MASTER";
    } else if (getPlayerScore() >= 10) {
      return "SKILLED";
    }
    return "NOVICE";
  }

  @Override
  public boolean walk(Direction direction) {
    return false;
  }

  @Override
  public String peek(Direction direction) {
    switch (direction) {
      case North:
      case South:
        return null;
      case East:
        return "MORE HALLWAY TO THE EAST";
      case West:
        return "MORE HALLWAY TO THE WEST";
    }
    return null;
  }

  @Override
  public List<String> fixtureList() {
    return List.of();
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
    if (roomItemList().contains(itemName)) {
      score += 10;
      return true;
    }
    return false;
  }

  @Override
  public boolean dropItem(String itemName) {
    if (playerItemList().contains(itemName)) {
      score -= 10;
      return true;
    }
    return false;
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
  public String useItem(String itemName) {
    if (itemName.equals("KEY_ITEM")) {
      return "Success";
    } else {
      return "Cannot use here";
    }
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

  @Override
  public int monsterDamage() {
    return 0;
  }

  @Override
  public boolean roomObstacleActive() {
    return false;
  }

  @Override
  public Player getPlayer() {
    return null;
  }

  @Override
  public Map<String, IRoom> getRooms() {
    return Map.of();
  }

  @Override
  public boolean damagePlayer() {
    return false;
  }
}
