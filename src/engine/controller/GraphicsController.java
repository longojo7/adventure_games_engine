package engine.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import engine.Direction;
import engine.model.IEngineModel;
import engine.view.IGameView;
import engine.model.JsonBuilder;


/**
 * Graphics controller for adventure game engine.
 */
public class GraphicsController implements IGameController, ActionListener {
  private IEngineModel model;
  private IGameView view;
  private JsonBuilder jsonBuilder;


  public GraphicsController(IEngineModel model, IGameView view, JsonBuilder jsonBuilder) {
    this.model = model;
    this.view = view;
    this.jsonBuilder = jsonBuilder;

    view.addActionListener(this);
  }

  @Override
  public void go() throws IOException {
    updateView();
    view.display();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand().toLowerCase();
    if (command.startsWith("move"))
      handleMove(command.split(" ")[1]);
    else if (command.startsWith("examine"))
      handleExamine(command.contains("player"));
    else if (command.startsWith("take"))
      handleTake();
    else if (command.startsWith("drop"))
      handleDrop();
    else if (command.startsWith("use"))
      handleUse();
    else if (command.startsWith("answer"))
      handleAnswer();
    else if(command.startsWith("save"))
      handleSave();
    else if(command.startsWith("reload"))
      handleReload();
    else if(command.startsWith("exit"))
      handleExit();
  }

  /**
   * Updates all view components with current game state.
   */
  private void updateView() {
    view.updateImage(model.getImage());

    // add exit info
    StringBuilder description = new StringBuilder(model.environmentDescription());
    description.append("\n\nAvailable exits:");
    boolean hasExits = false;
    for (Direction dir : Direction.values()) {
      String roomName = model.peek(dir);
      if (roomName != null) {
        description.append(" ").append(dir.name());
        hasExits = true;
      }
    }
    if (!hasExits) {
      description.append(" None");
    }
    view.updateDescription(description.toString());

    // combine items and fixtures for room contents
    List<String> roomContents = new ArrayList<>(model.roomItemList());
    roomContents.addAll(model.fixtureList());
    view.updateInventory(model.playerItemList(), roomContents);
    view.updateHealth("Health: " + model.getPlayer().currentHealth() + " / "
            + model.getPlayer().maxHealth() + " | Status: " + model.playerStatus());

    if(model.playerStatus().equals("Asleep"))
      view.gameOver(model.playerStatus(), model.getPlayerScore()+"", model.getPlayerRank());
  }

  /**
   * Handles player movement in specified direction.
   *
   * @param directionString the direction to move
   */
  private void handleMove(String directionString) {
    Direction direction = switch (directionString) {
      case "east" -> Direction.East;
      case "south" -> Direction.South;
      case "west" -> Direction.West;
      default -> Direction.North;
    };
    if (!model.walk(direction))
      failToMove();

    updateView();
    view.updateFeedback(damageString());
  }

  /**
   * Displays feedback when movement fails.
   */
  private void failToMove() {
    view.updateFeedback("There is no room in that direction.");
  }

  private void handleSave() {
    try {
      String filename = model.getFileName();

      String savedPath = SaveManager.saveGame(model, jsonBuilder, filename);
      view.updateFeedback("Game saved successfully to " + savedPath);
      view.showSaveView();
    } catch (Exception e) {
      view.updateFeedback("Error saving game: " + e.getMessage());
    }
  }

  private void handleReload() {
    try {
      // Generate the expected filename for this player and room
      String filename = model.getFileName();

      // Check if this specific save file exists
      if (!SaveManager.saveFileExists(filename)) {
        view.updateFeedback("No save file found for " + model.getPlayerName()
                + " in " + model.roomName() + ". Please save first.");
        view.reloadFail();
        return;
      }

      // Load the game state
      String gameState = SaveManager.loadGame(SaveManager.getSaveFilePath(filename));

      // Restore the game state
      jsonBuilder = new JsonBuilder(new StringReader(gameState), SaveManager.getSaveFilePath(filename));
      model = jsonBuilder.createModel(model.getPlayerName());

      // Update the view to reflect the restored state
      updateView();
      view.updateFeedback("Game restored successfully from " + filename);
      view.showReloadView();
    } catch (IOException e) {
      view.updateFeedback("Error loading game: " + e.getMessage());
    } catch (Exception e) {
      view.updateFeedback("Error restoring game state: " + e.getMessage());
    }
  }

  /**
   * Handles examining an item.
   *
   * @param examiningPlayer true if examining player inventory, false for room
   */
  private void handleExamine(boolean examiningPlayer) {
    String item = examiningPlayer ? view.selectedItemPlayer() : view.selectedItemRoom();
    view.updateFeedback(
            item.isEmpty() ?
                    "Examine what? Please specify a target." :
                    model.examine(item));
  }

  /**
   * Handles taking an item from the room.
   */
  private void handleTake() {
    String selectedItem = view.selectedItemRoom();
    if (model.fixtureList().contains(selectedItem)) {
      view.updateFeedback("You cannot pick up " + selectedItem + ". It is a fixed item.");
    } else {
      view.updateFeedback(
              model.pickupItem(view.selectedItemRoom()) ?
                      "Picked up " + view.selectedItemRoom() :
                      "You cannot pick up that item. It may be too heavy.");
      view.appendFeedback(damageString());
    }
    updateView();
  }

  /**
   * Handles dropping an item from player inventory.
   */
  private void handleDrop() {
    String selectedItem = view.selectedItemPlayer();

    if (selectedItem == null || selectedItem.isEmpty()) {
      view.updateFeedback("Drop what? Please select an item to drop.");
    } else {
      model.dropItem(selectedItem);
      view.updateFeedback("Dropped " + selectedItem + "." + damageString());
    }
    updateView();
  }

  /**
   * Handles using an item from player inventory.
   */
  private void handleUse() {
    String playerItem = view.selectedItemPlayer();
    String roomItem = view.selectedItemRoom();

    if (playerItem.isEmpty()) {
      if (!roomItem.isEmpty()) {
        view.updateFeedback("Take the item before trying to use it.");
      } else {
        view.updateFeedback("Select an item from your inventory to use.");
      }
      return;
    }

    String result = model.useItem(playerItem);
    if ("no use".equals(result)) {
      view.updateFeedback("Cannot use that item here." + damageString());
    } else {
      view.updateFeedback("You used: " + playerItem + "\n" + result);
    }

    updateView();
  }

  /**
   * Handles submitting an answer to a question.
   */
  private void handleAnswer() {
    String answer = view.getAnswer();

    if (answer.isEmpty()) {
      view.updateFeedback("Please enter an answer.");
      return;
    }

    if (!model.roomObstacleActive()) {
      view.updateFeedback("There is no active puzzle or monster to answer here.");
      return;
    }

    if (model.answerQuestion(answer)) {
      view.updateFeedback("Success!");
    } else {
      view.updateFeedback("Answer is incorrect." + damageString());
    }
    updateView();
  }

  /**
   * Generates damage message if monster is active.
   *
   * @return damage message or empty string
   */
  private String damageString() {
    if (model.roomObstacleActive() && model.monsterDamage() != 0)
      return " " + model.monsterAttack()
              + " You take " + model.monsterDamage() + " damage.";
    else return "";
  }

  private void handleExit() {
    // Menu to ask if they want to quit
    int choice = JOptionPane.showConfirmDialog(null,
            "Are you sure you want to quit?", "Confirm Exit",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (choice == JOptionPane.YES_OPTION)
      view.gameOver(model.playerStatus(), model.getPlayerScore()+"", model.getPlayerRank());
  }
}
