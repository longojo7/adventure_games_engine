package engine.controller;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import engine.Direction;
import engine.model.IEngineModel;
import engine.model.JsonBuilder;

/**
 * Controller for the adventure game engine.
 * Handles user input, command parsing, and interaction with the game model.
 */
public class GameController implements IGameController {
  private static final String saveFile = "savegame.json";
  private IEngineModel model;
  private JsonBuilder jsonBuilder;
  private final Readable input;
  private final Appendable output;
  private boolean gameRunning;
  private String savedState;


  /**
   * Constructs a new GameController.
   * @param model  the game model to control.
   * @param input  the source of user input.
   * @param output the destination for game output.
   */
  public GameController(IEngineModel model, JsonBuilder jsonBuilder, Readable input, Appendable output) {
    if (model == null || input == null || output == null) {
      throw new IllegalArgumentException("Model, input, and output cannot be null");
    }
    this.model = model;
    this.input = input;
    this.output = output;
    this.gameRunning = false;
    this.savedState = null;
    this.jsonBuilder = jsonBuilder;
  }

  /**
   * Starts the game loop and processes user commands.
   * @throws IOException if an I/O error occurs.
   */
  @Override
  public void go() throws IOException {
    gameRunning = true;
    output.append("Please enter the name of your player: ");
    Scanner scanner = new Scanner(input);
    String playerName = scanner.nextLine();
    model.setPlayerName(playerName);
    output.append("You will now be named: "
            + model.getPlayerName()
            + "\nWelcome to the world of Adventures!"
            + "\n------------------------------\n");
    displayCommandMenu();
    output.append("\nYou start your adventure in the ").append(model.roomName()).append("\n");
    displayRoomDescription();
    output.append("\n------------------------------" +
            "\nYour Choice: ");
    while (gameRunning && scanner.hasNextLine()) {
      String command = scanner.nextLine().trim();
      if (command.isEmpty()) {
        continue;
      }
      processCommand(command);
      if (gameRunning) {
        output.append("\n------------------------------\n"
                + "Enter (H)elp to view player commands\n"
                + "Your Choice: ");
      }
    }
    scanner.close(); 
  }

  /**
   * Displays the command instructions.
   * @throws IOException if an I/O error occurs.
   */
  private void displayCommandMenu() throws IOException {
    output.append("\nAvailable commands:\n"
            + "To move, enter: (N)orth, (S)outh, (E)ast or (W)est.\n"
            + "Other actions: (I)nventory, (L)ook around the location, (U)se an item\n"
            + "(T)ake an item, (D)rop an item, or e(X)amine something.\n"
            + "(A)nswer a question or provide a text solution.\n"
            + "Enter (H)elp to look at the available player commands.\n"
            + "To end the game, enter (Q)uit to quit and exit.\n"
            + "Save and Restore to save/load your game.\n");
  }

  /**
   * Displays the current room's description and available exits.
   * @throws IOException if an I/O error occurs.
   */
  private void displayRoomDescription() throws IOException {
    output.append(model.environmentDescription()).append("\n");
    String monsterAttack = model.monsterAttack();
    if (monsterAttack != null && model.roomObstacleActive()) {
      output.append( model.getPlayerName() + " Takes " + model.dealDamage() + " Damage");
      if (model.playerStatus().equals("Asleep")){
        output.append("\nYou have Fallen Asleep. ");
        handleQuit();
        return;
      }
    }
    displayAvailableExits();
    displayRoomItems();
  }

  /**
   * Displays available exits from the current room.
   * @throws IOException if an I/O error occurs.
   */
  private void displayAvailableExits() throws IOException {
    StringBuilder exits = new StringBuilder("\nAvailable exits:");
    boolean hasExits = false;
    for (Direction dir : Direction.values()) {
      String roomName = model.peek(dir);
      if (roomName != null) {
        exits.append(" ").append(dir.name());
        hasExits = true;
      }
    }
    if (hasExits) {
      output.append(exits.toString());
    } else {
      output.append("\nNo available exits.");
    }
  }

  /**
   * Displays items available in the current room.
   * @throws IOException if an I/O error occurs.
   */
  private void displayRoomItems() throws IOException {
    List<String> items = model.roomItemList();
    if (items != null && !items.isEmpty()) {
      output.append("\nItems here: ").
              append(String.join(", ", items)).
              append("\n");
    }
  }

  /**
   * Processes a user command.
   * @param command the command to process.
   * @throws IOException if an I/O error occurs.
   */
  private void processCommand(String command) throws IOException {
    String[] parts = command.trim().split("\\s+", 2);
    String cmd = parts[0].toUpperCase();
    String argument = parts.length > 1 ? parts[1] : "";

    switch (cmd) {
      case "N":
      case "NORTH":
        handleMove(Direction.North);

        break;
      case "S":
      case "SOUTH":
        handleMove(Direction.South);
        break;
      case "E":
      case "EAST":
        handleMove(Direction.East);
        break;
      case "W":
      case "WEST":
        handleMove(Direction.West);
        break;
      case "T":
      case "TAKE":
        handleTake(argument);
        break;
      case "D":
      case "DROP":
        handleDrop(argument);
        break;
      case "X":
      case "EXAMINE":
        handleExamine(argument);
        break;
      case "L":
      case "LOOK":
        handleLook();
        break;
      case "U":
      case "USE":
        handleUse(argument);
        break;
      case "I":
      case "INVENTORY":
        handleInventory();
        displayPlayerStatus();
        break;
      case "A":
      case "ANSWER":
        handleAnswer(argument);
        break;
      case "SAVE":
        handleSave();
        break;
      case "RESTORE":
        handleRestore();
        break;
      case "Q":
      case "QUIT":
        handleQuit();
        break;
      case "H":
      case "HELP":
        displayCommandMenu();
        break;
      default:
        output.append("Unknown command: ").append(cmd).append("\n");
        output.append("Type one of the available commands ((N)orth, "
                + "(S)outh, (E)ast, (W)est, (T)ake, (D)rop, e(X)amine, "
                + "(L)ook, (U)se, (I)nventory, (A)nswer, (H)elp, (Q)uit)\n");
    }
  }

  /**
   * Handles movement in a given direction.
   * @param direction the direction to move.
   * @throws IOException if an I/O error occurs.
   */
  private void handleMove(Direction direction) throws IOException {
    if (model.walk(direction)) {
      output.append("You move ").append(direction.name().toLowerCase()).append(".\n");
      // may have to revisit this since I'm not sure what movement will look it
      displayRoomDescription();
    } else {
      output.append(">> You cannot go that direction! <<\n");
    }
  }

  /**
   * Handles the take/pickup command.
   * @param itemName the name of the item to take.
   * @throws IOException if an I/O error occurs.
   */
  private void handleTake(String itemName) throws IOException {
    if (itemName.isEmpty()) {
      output.append("Take what? Please specify an item name.\n");
      return;
    }

    if (model.pickupItem(itemName)) {
      output.append("You picked up: ").append(itemName).append("\n");
      displayPlayerStatus();
    } else {
      output.append("Cannot take that item. It may not exist or your inventory is full.\n");
    }
  }

  /**
   * Handles the drop command.
   * @param itemName the name of the item to drop.
   * @throws IOException if an I/O error occurs.
   */
  private void handleDrop(String itemName) throws IOException {
    if (itemName.isEmpty()) {
      output.append("Drop what? Please specify an item name.\n");
      return;
    }
    if (model.dropItem(itemName)) {
      output.append("You dropped ").append(itemName).append("in the ").append(model.roomName());
    } else {
      output.append("You don't have that item.\n");
    }
  }

  /**
   * Handles the examine command.
   * @param target the name of the object to examine.
   * @throws IOException if an I/O error occurs.
   */
  private void handleExamine(String target) throws IOException {
    if (target.isEmpty()) {
      output.append("Examine what? Please specify a target.\n");
      return;
    }

    String description = model.examine(target);
    if (description.equals("Nothing")) {
      output.append("You don't see anything named '").append(target).append("' to examine.\n");
    } else {
      output.append(description).append("\n");
    }
  }

  /**
   * Handles the look command - re displays the current room.
   * @throws IOException if an I/O error occurs.
   */
  private void handleLook() throws IOException {
    output.append("\nYou are standing in the ").append(model.roomName()).append("\n");
       displayRoomDescription();
  }

  /**
   * Handles the use command.
   * @param itemName the name of the item to use.
   * @throws IOException if an I/O error occurs.
   */
  private void handleUse(String itemName) throws IOException {
    if (itemName.isEmpty()) {
      output.append("Use what? Specify an item name.\n");
      return;
    }
    String result = model.useItem(itemName);
    if (result.equals("no item")) {
      output.append("Cannot use an item you don't have.\n");
    } else if (result.equals("no use")) {
      output.append("Cannot use that item here.\n");
    } else {
      output.append("You used: ").append(itemName).append("\n");
      displayRoomDescription();
      displayPlayerStatus();
    }
  }

  /**
   * Handles the inventory command.
   * @throws IOException if an I/O error occurs.
   */
  private void handleInventory() throws IOException {
    List<String> items = model.playerItemList();
    output.append("\n=== Your Inventory ===\n");
    if (items != null && !items.isEmpty()) {
      for (String item : items) {
        output.append("  - ").append(item).append("\n");
      }
    } else {
      output.append("  (empty)\n");
    }
  }

  /**
   * Handles the answer command.
   * @param answer the answer to provide.
   * @throws IOException if an I/O error occurs.
   */
  private void handleAnswer(String answer) throws IOException {
    if (answer.isEmpty()) {
      output.append("Answer what? Please provide an answer.\n");
      return;
    }

    if (model.answerQuestion(answer)) {
      output.append("Correct! The obstacle has been cleared.\n");
      displayRoomDescription();
    } else {
      output.append("That answer is incorrect.\n");
    }
  }

  /**
   * Handles the save command.
   * @throws IOException if an I/O error occurs.
   */
  private void handleSave() throws IOException {
    // Save current game state to memory
    savedState = captureGameState();
    // Saves to file
    try (FileWriter writer = new FileWriter(saveFile)) {
      writer.write(savedState);
      output.append("Game saved successfully to ").append(saveFile).append(".\n");
    } catch (IOException e) {
      output.append("Error saving game to file: ").append(e.getMessage()).append("\n");
      throw e;
    }
  }

  /**
   * Handles the restore command.
   * @throws IOException if an I/O error occurs.
   */
  private void handleRestore() throws IOException {
    String stateToRestore = null;

    // load from file
    if (Files.exists(Paths.get(saveFile))) {
      try {
        stateToRestore = new String(Files.readAllBytes(Paths.get(saveFile)));
        output.append("Loading saved game from ").append(saveFile).append("...\n");
      } catch (IOException e) {
        output.append("Error reading save file: ").append(e.getMessage()).append("\n");
      }
    }
    // Loads save from memory
    if (stateToRestore == null) {
      if (savedState == null) {
        output.append("No saved game found. Please save a game first.\n");
        return;
      }
      stateToRestore = savedState;
    }

    restoreGameState(stateToRestore);
    output.append("Game restored successfully.\n");
    displayRoomDescription();
  }

  /**
   * Handles the quit command.
   * @throws IOException if an I/O error occurs.
   */
  private void handleQuit() throws IOException {
    output.append("Thank you for playing! Goodbye.\n");
    output.append("Player Score: ").append(String.valueOf(model.getPlayerScore()))
            .append("\n").append("Player Rank: ").append(String.valueOf(model.getPlayerRank()));
    gameRunning = false;
  }

  /**
   * Displays the player's current status (health, etc.).
   * @throws IOException if an I/O error occurs.
   */
  private void displayPlayerStatus() throws IOException {
    String status = model.playerStatus();
    if (status != null && !status.isEmpty()) {
      output.append("Player Status: ").append(status).append("\n");
    }
  }

  /**
   * Captures the current game state for saving.
   * This is a simple implementation.
   * @return a string representation of the game state.
   */
  private String captureGameState() {
    return jsonBuilder.exportModel(model);
  }

  /**
   * Restores a previously saved game state.
   * @param state the saved state to restore.
   * @throws IOException if an I/O error occurs.
   */
  private void restoreGameState(String state) throws IOException {
    if (state == null || state.isEmpty()) {
      return;
    }
    jsonBuilder = new JsonBuilder(new StringReader(state));
    try {
      model = jsonBuilder.createModel(model.getPlayerName());
    } catch (Exception e) {
      output.append("Error restoring game: ").append(e.getMessage()).append("\n");
      throw new IOException("Failed to restore game state", e);
    }
  }
}
