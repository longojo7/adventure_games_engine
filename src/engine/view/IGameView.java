package engine.view;

import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interface for game view.
 */
public interface IGameView {

  /**
   * Updates the displayed image.
   * @param path the file path to the image
   */
  void updateImage(String path);

  /**
   * Updates the room description text.
   * @param description the description to display
   */
  void updateDescription(String description);

  /**
   * Updates both player and room inventory displays.
   * @param playerInventory list of items in player's inventory
   * @param roomInventory list of items in current room
   */
  void updateInventory(List<String> playerInventory, List<String> roomInventory);

  /**
   * Updates the player's health display.
   * @param health the health value to display
   */
  void updateHealth(String health);

  /**
   * Updates the player's score display.
   * @param score the score value to display
   */
  void updateScore(String score);

  /**
   * Updates the player's rank display.
   * @param rank the rank to display
   */
  void updateRank(String rank);

  /**
   * Replaces the feedback text.
   * @param feedback the feedback message to display
   */
  void updateFeedback(String feedback);

  /**
   * Appends text to existing feedback.
   * @param append the text to append
   */
  void appendFeedback(String append);

  /**
   * Gets user input answer.
   * @return the answer text entered by user
   */
  String getAnswer();

  /**
   * Gets the selected item from player's inventory.
   * @return the selected player inventory item
   */
  String selectedItemPlayer();

  /**
   * Gets the selected item from room's inventory.
   * @return the selected room inventory item
   */
  String selectedItemRoom();

  /**
   * Adds an action listener to the view.
   * @param listener the action listener to add
   */
  void addActionListener(ActionListener listener);

  /**
   * Displays the view.
   */
  void display();
}