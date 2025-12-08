package engine.model.rooms;

import java.util.List;

import engine.Direction;
import engine.model.items.IInventory;
import engine.model.rooms.obstacles.IObstacle;
import engine.model.rooms.obstacles.Puzzle;

/**
 * Interface for game rooms.
 */
public interface IRoom {

  /**
   * Gets the room name.
   *
   * @return the name
   */
  String getName();

  /**
   * Gets the room description.
   *
   * @return the description
   */
  String getDescription();

  String getImagePath();

  /**
   * Gets the connection in the specified direction.
   *
   * @param direction the direction
   * @return the connection or null if none exists
   */
  Connection getConnection(Direction direction);

  /**
   * Gets the list of fixtures in the room.
   *
   * @return list of fixtures
   */
  List<Fixture> getFixtures();

  /**
   * Gets the obstacle in the room.
   *
   * @return the obstacle or null if none exists
   */
  IObstacle getObstacle();

  /**
   * Gets the room's inventory.
   *
   * @return the inventory
   */
  IInventory getInventory();
}
