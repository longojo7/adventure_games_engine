package engine.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import engine.Direction;
import engine.model.IEngineModel;
import engine.view.IGameView;

/**
 * Graphics controller for adventure game engine.
 */
public class GraphicsController implements IGameController, ActionListener {
  private IEngineModel model;
  private IGameView view;

  public GraphicsController(IEngineModel model, IGameView view) {
    this.model = model;
    this.view = view;

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
    view.updateHealth(model.playerStatus());
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

  /**
   * Handles examining an item.
   *
   * @param examiningPlayer true if examining player inventory, false for room
   */
  private void handleExamine(boolean examiningPlayer) {
    String item = examiningPlayer ? view.selectedItemPlayer() : view.selectedItemRoom();
    view.updateFeedback(
            item.isEmpty() ?
                    "You have not selected an item to examine." :
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
    model.dropItem(view.selectedItemPlayer());
    view.updateFeedback("Dropped " + view.selectedItemPlayer() + "." + damageString());
    updateView();
  }

  /**
   * Handles using an item from player inventory.
   */
  private void handleUse() {
    String message = model.useItem(view.selectedItemPlayer());
    view.updateFeedback(
            "no use".equals(message) ?
                    "Cannot use this item." + damageString() :
                    message);
    updateView();
  }

  /**
   * Handles submitting an answer to a question.
   */
  private void handleAnswer() {
    view.updateFeedback(
            model.answerQuestion(view.getAnswer()) ?
                    "Success!" :
                    "Answer is incorrect." + damageString());
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
}
