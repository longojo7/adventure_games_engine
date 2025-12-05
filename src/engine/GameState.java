package engine;

import java.util.List;
import java.util.Map;
import engine.Direction;

/**
 * Class that represents the state of the game. Might need this not sure.
 */
public class GameState {
  public final String roomName;
  public final String description;
  public final List<String> roomItems;
  public final List<String> inventory;
  public final Map<Direction, String> exits;
  public final String playerStatus;
  public final boolean obstacleActive;
  public final boolean monsterPresent;

  public GameState(String roomName,
                   String description,
                   List<String> roomItems,
                   List<String> inventory,
                   Map<Direction,String> exits,
                   String playerStatus,
                   boolean obstacleActive,
                   boolean monsterPresent) {
    this.roomName = roomName;
    this.description = description;
    this.roomItems = roomItems;
    this.inventory = inventory;
    this.exits = exits;
    this.playerStatus = playerStatus;
    this.obstacleActive = obstacleActive;
    this.monsterPresent = monsterPresent;
  }
}