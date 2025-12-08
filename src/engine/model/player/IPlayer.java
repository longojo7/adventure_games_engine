package engine.model.player;

import java.util.List;

import engine.Direction;
import engine.model.items.Item;
import engine.model.rooms.IRoom;

/**
 * Interface for player operations.
 */
public interface IPlayer {

  /**
   * Gets the player's health status description.
   *
   * @return the health status string
   */
  String healthStatus();

  /**
   * Gets the player's current health value.
   *
   * @return the current health
   */
  int currentHealth();

  /**
   * Gets the player's maximum health value.
   *
   * @return the max health
   */
  int maxHealth();

  /**
   * Applies damage to the player.
   *
   * @param damage the amount of damage
   */
  void damage(int damage);

  /**
   * Heals the player.
   *
   * @param heal the amount to heal
   */
  void heal(int heal);

  /**
   * Gets the player's score.
   *
   * @return the score
   */
  int getScore();

  /**
   * Adds score from solving an obstacle.
   *
   * @param score the score to add
   */
  void addObstacleScore(int score);

  /**
   * Gets the player's name.
   *
   * @return the name
   */
  String getName();

  /**
   * Sets the player's name.
   *
   * @param name the name to set
   */
  void setName(String name);

  /**
   * Peeks at the room in the specified direction.
   *
   * @param direction the direction to peek
   * @return the room or null if none exists
   */
  IRoom peek(Direction direction);

  /**
   * Moves the player in the specified direction.
   *
   * @param direction the direction to walk
   * @return true if movement was successful, false otherwise
   */
  boolean walk(Direction direction);

  /**
   * Gets the player's current room.
   *
   * @return the current room
   */
  IRoom getCurrentRoom();

  /**
   * Picks up an item from the current room.
   *
   * @param id the item name
   * @return true if successful, false otherwise
   */
  boolean pickupItem(String id);

  /**
   * Drops an item into the current room.
   *
   * @param id the item name
   * @return true if successful, false otherwise
   */
  boolean dropItem(String id);

  /**
   * Checks if player has an item.
   *
   * @param id the item name
   * @return true if player has the item, false otherwise
   */
  boolean hasItem(String id);

  /**
   * Gets list of all items in player's inventory.
   *
   * @return list of items
   */
  List<Item> itemsList();
}
