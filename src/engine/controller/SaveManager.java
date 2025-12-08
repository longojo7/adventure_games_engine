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
  private static final String SAVE_DIRECTORY = "saves";
  private static final String SAVE_EXTENSION = ".json";

  /**
   * Generates a save filename based on player name and current room.
   * @param playerName the name of the player
   * @param roomName the name of the current room
   * @return the generated save filename
   */
  public static String generateSaveFilename(String playerName, String roomName) {
    // Sanitize names to remove invalid filename characters
    String sanitizedPlayer = sanitizeFilename(playerName);
    String sanitizedRoom = sanitizeFilename(roomName);
    return sanitizedPlayer + "_" + sanitizedRoom + SAVE_EXTENSION;
  }

  /**
   * Sanitizes a string to be safe for use in a filename.
   * Removes or replaces characters that are invalid in filenames.
   * @param name the string to sanitize
   * @return the sanitized string
   */
  private static String sanitizeFilename(String name) {
    // Replace spaces with underscores and remove invalid characters
    return name.replaceAll("[^a-zA-Z0-9-_]", "_");
  }

  /**
   * Ensures the save directory exists, creating it if necessary.
   * @throws IOException if the directory cannot be created
   */
  private static void ensureSaveDirectoryExists() throws IOException {
    File directory = new File(SAVE_DIRECTORY);
    if (!directory.exists()) {
      if (!directory.mkdir()) {
        throw new IOException("Failed to create save directory: " + SAVE_DIRECTORY);
      }
    }
  }

  /**
   * Saves the current game state to a file.
   * @param model the game model to save
   * @param jsonBuilder the JsonBuilder to use for serialization
   * @param filename the name of the save file (optional - will be auto-generated if null)
   * @return the full path to the saved file
   * @throws IOException if an error occurs while saving
   */
  public static String saveGame(IEngineModel model, JsonBuilder jsonBuilder, String filename)
          throws IOException {
    ensureSaveDirectoryExists();


    if (filename == null || filename.isEmpty()) {
      filename = generateSaveFilename(model.getPlayerName(), model.roomName());
    }

    String saveFilePath = SAVE_DIRECTORY + File.separator + filename;
    String gameState = jsonBuilder.exportModel(model);

    try (FileWriter writer = new FileWriter(saveFilePath)) {
      writer.write(gameState);
    }

    return saveFilePath;
  }

  /**
   * Loads a game state from a file.
   * @param filename the name of the save file to load
   * @return the serialized game state as a JSON string
   * @throws IOException if an error occurs while loading
   */
  public static String loadGame(String filename) throws IOException {
    String saveFilePath = SAVE_DIRECTORY + File.separator + filename;
    Path path = Paths.get(saveFilePath);

    if (!Files.exists(path)) {
      throw new IOException("Save file not found: " + saveFilePath);
    }

    return new String(Files.readAllBytes(path));
  }

  /**
   * Lists all available save files.
   * @return a list of save file names
   * @throws IOException if an error occurs while listing files
   */
  public static List<String> listSaveFiles() throws IOException {
    File directory = new File(SAVE_DIRECTORY);

    if (!directory.exists() || !directory.isDirectory()) {
      return new ArrayList<>();
    }

    File[] files = directory.listFiles((dir, name) -> name.endsWith(SAVE_EXTENSION));

    if (files == null) {
      return new ArrayList<>();
    }

    List<String> saveFiles = new ArrayList<>();
    for (File file : files) {
      saveFiles.add(file.getName());
    }

    return saveFiles;
  }

  /**
   * Checks if a save file exists.
   * @param filename the name of the save file
   * @return true if the file exists, false otherwise
   */
  public static boolean saveFileExists(String filename) {
    String saveFilePath = SAVE_DIRECTORY + File.separator + filename;
    return Files.exists(Paths.get(saveFilePath));
  }

  /**
   * Deletes a save file.
   * @param filename the name of the save file to delete
   * @return true if the file was deleted, false otherwise
   * @throws IOException if an error occurs while deleting
   */
  public static boolean deleteSaveFile(String filename) throws IOException {
    String saveFilePath = SAVE_DIRECTORY + File.separator + filename;
    Path path = Paths.get(saveFilePath);

    if (Files.exists(path)) {
      return Files.deleteIfExists(path);
    }

    return false;
  }

  /**
   * Gets the full path for a save file.
   * @param filename the name of the save file
   * @return the full path to the save file
   */
  public static String getSaveFilePath(String filename) {
    return SAVE_DIRECTORY + File.separator + filename;
  }
}