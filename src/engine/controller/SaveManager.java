package engine.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import engine.model.IEngineModel;
import engine.model.JsonBuilder;

/**
 * Manages save and restore operations for the game.
 * Supports multiple save files with naming based on player name and current room.
 */
public class SaveManager {
  private static final String SAVE_DIRECTORY = "";
  private static final String SAVE_EXTENSION = ".json";

  /**
   * Saves the current game state to a file.
   *
   * @param model       the game model to save
   * @param jsonBuilder the JsonBuilder to use for serialization
   * @param filename    the name of the save file (optional - will be auto-generated if null)
   * @return the full path to the saved file
   * @throws IOException if an error occurs while saving
   */
  public static String saveGame(IEngineModel model, JsonBuilder jsonBuilder, String filename) {
    String saveFilePath = getSaveFilePath(filename);
    String gameState = jsonBuilder.exportModel(model);

    try {
      FileWriter writer = new FileWriter(saveFilePath);
      writer.write(gameState);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return saveFilePath;
  }

  /**
   * Loads a game state from a file.
   *
   * @param filename the name of the save file to load
   * @return the serialized game state as a JSON string
   * @throws IOException if an error occurs while loading
   */
  public static String loadGame(String filename) throws IOException {
    String saveFilePath = getSaveFilePath(filename);
    Path path = Paths.get(saveFilePath);

    if (!Files.exists(path)) {
      throw new IOException("Save file not found: " + saveFilePath);
    }

    return new String(Files.readAllBytes(path));
  }

  /**
   * Checks if a save file exists.
   *
   * @param filename the name of the save file
   * @return true if the file exists, false otherwise
   */
  public static boolean saveFileExists(String filename) {
    String saveFilePath = getSaveFilePath(filename);
    return Files.exists(Paths.get(saveFilePath));
  }

  /**
   * Gets the full path for a save file.
   *
   * @param filename the name of the save file
   * @return the full path to the save file
   */
  public static String getSaveFilePath(String filename) {
    return filename.replaceAll("_save","").replaceAll(".json", "_save.json");
  }
}