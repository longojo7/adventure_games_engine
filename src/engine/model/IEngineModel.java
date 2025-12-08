package engine.model;

import java.util.List;
import java.util.Map;

import engine.Direction;
import engine.model.player.Player;
import engine.model.rooms.IRoom;

/**
 * Interface for the game engine model.
 */
public interface IEngineModel {

  /**
   * Sets the player's name.
   *
   * @param name the name to set
   */
  void setPlayerName(String name);

  /**
   * Gets the player's name.
   *
   * @return the player's name
   */
  String getPlayerName();

  /**
   * Gets the player's score.
   *
   * @return the player's score
   */
  int getPlayerScore();

  /**
   * Gets the player's rank based on score.
   *
   * @return the player's rank
   */
  String getPlayerRank();

  /**
   * Moves the player in the specified direction.
   *
   * @param direction the direction to move
   * @return true if movement was successful, false otherwise
   */
  boolean walk(Direction direction);

  /**
   * Peeks at the room in the specified direction.
   *
   * @param direction the direction to peek
   * @return the room name or null if no room exists
   */
  String peek(Direction direction);

  /**
   * Gets list of fixtures in the current room.
   *
   * @return list of fixture names
   */
  List<String> fixtureList();

  /**
   * Gets list of items in player's inventory.
   *
   * @return list of player item names
   */
  List<String> playerItemList();

  /**
   * Gets list of items in the current room.
   *
   * @return list of room item names
   */
  List<String> roomItemList();

  /**
   * Picks up an item from the current room.
   *
   * @param itemName the name of the item to pick up
   * @return true if pickup was successful, false otherwise
   */
  boolean pickupItem(String itemName);

  /**
   * Drops an item from player's inventory.
   *
   * @param itemName the name of the item to drop
   * @return true if drop was successful, false otherwise
   */
  boolean dropItem(String itemName);

  /**
   * Examines a target item, fixture, or obstacle.
   *
   * @param target the name of the target to examine
   * @return the description of the target
   */
  String examine(String target);

  /**
   * Uses an item from player's inventory.
   *
   * @param itemName the name of the item to use
   * @return the result message or "no use" if unsuccessful
   */
  String useItem(String itemName);

  /**
   * Attempts to answer a question obstacle.
   *
   * @param answer the answer to submit
   * @return true if answer is correct, false otherwise
   */
  boolean answerQuestion(String answer);

  /**
   * Gets the player's health status.
   *
   * @return the player's health status string
   */
  String playerStatus();

  /**
   * Gets the current room's name.
   *
   * @return the room name
   */
  String roomName();

  /**
   * Gets the current room's description.
   *
   * @return the room description
   */
  String environmentDescription();

  String getImage();

  /**
   * Gets the monster's attack message.
   *
   * @return the attack message or null if no monster
   */
  String monsterAttack();

  /**
   * Gets the damage amount from the current obstacle.
   *
   * @return the damage amount
   */
  int monsterDamage();

  /**
   * Checks if the current room has an active obstacle.
   *
   * @return true if obstacle is active, false otherwise
   */
  boolean roomObstacleActive();

  /**
   * Gets the player object.
   *
   * @return the player
   */
  Player getPlayer();

  /**
   * Gets the map of all rooms.
   *
   * @return map of room names to room objects
   */
  Map<String, IRoom> getRooms();

  /**
   * Applies damage to the player from current obstacle.
   *
   * @return true if damage was applied, false otherwise
   */
  boolean damagePlayer();

  String getFileName();
}