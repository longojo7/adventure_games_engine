package engine.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import engine.Direction;
import engine.model.IEngineModel;
import engine.view.IGameView;

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
    else if(command.startsWith("use"))
      handleUse();
    else  if(command.startsWith("answer"))
      handleAnswer();
  }

  private void updateView() {
    view.updateImage("");

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
    view.updateFeedback("");
  }

  private void failToMove() {
    view.updateFeedback("There is no room in that direction.");
  }

  private void handleExamine(boolean examiningPlayer) {
    String item = examiningPlayer ? view.selectedItemPlayer() : view.selectedItemRoom();
    view.updateFeedback(
            item.isEmpty() ?
                    "You have not selected an item to examine." :
                    model.examine(item));
  }

  private void handleTake() {
    String selectedItem = view.selectedItemRoom();
    if (model.fixtureList().contains(selectedItem)) {
      view.updateFeedback("You cannot pick up " + selectedItem + ". It is a fixed item.");
    } else {
      view.updateFeedback(
              model.pickupItem(view.selectedItemRoom()) ?
                      "Picked up " + view.selectedItemRoom() :
                      "You cannot pick up that item. It may be too heavy.");
    }
    updateView();
  }

  private void handleDrop() {
    model.dropItem(view.selectedItemPlayer());
    view.updateFeedback("Dropped " + view.selectedItemPlayer());
    updateView();
  }

  private void handleUse() {
    String message = model.useItem(view.selectedItemPlayer());
    view.updateFeedback(
            "no use".equals(message) ?
                    "Cannot use this item." :
                    message);
    updateView();
  }

  private void handleAnswer() {
    view.updateFeedback(
            model.answerQuestion(view.getAnswer()) ?
                    "Success!" :
                    "Answer is incorrect.");
    updateView();
  }
}
