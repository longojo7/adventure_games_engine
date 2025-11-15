package enginedriver;

import engine.controller.GameController;
import engine.model.DummyModel;
import engine.model.IEngineModel;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Entry point class for the adventure game engine.
 * This class provides a simple interface to start and run a game.
 */
public class GameEngineApp {
  private final String gameFileName;
  private final Readable source;
  private final Appendable output;
  private IEngineModel model;
  private GameController controller;

  /**
   * Constructs a new GameEngineApp.
   * @param gameFileName the name of the game file to load.
   * @param source       the source of user input (e.g., System.in, StringReader, FileReader).
   * @param output       the destination for game output (e.g., System.out, StringWriter, FileWriter).
   * @throws IllegalArgumentException if any parameter is null.
   */
  public GameEngineApp(String gameFileName, Readable source, Appendable output) {
    if (gameFileName == null || source == null || output == null) {
      throw new IllegalArgumentException("All parameters must be non-null");
    }
    this.gameFileName = gameFileName;
    this.source = source;
    this.output = output;
  }

  /**
   * Starts the game engine.
   * This method initializes the game model and controller, then starts the game loop.
   * @throws IOException if an I/O error occurs during game execution.
   */
  public void start() throws IOException {
    // Initialize the game model.
    // TODO: Replace DummyModel with actual game loading from gameFileName.
    // This would involve parsing the JSON file and constructing the proper model.
    model = createModel(gameFileName);

    controller = new GameController(model, source, output);

    controller.go();
  }

  /**
   * Creates a game model from the specified game file.
   * Currently uses DummyModel as a placeholder.
   * TODO: Implement JSON parsing to load actual game data.
   * @param gameFileName the name of the game file to load.
   * @return the initialized game model.
   */
  private IEngineModel createModel(String gameFileName) {
    // TODO: Implement actual game loading logic
    // This would involve:
    // 1. Reading the JSON file from gameFileName.
    // 2. Parsing the JSON into game objects (rooms, items, puzzles, etc.)
    // 3. Constructing a proper EngineModel with the loaded data.
    // For now, we use DummyModel as a placeholder.
    return new DummyModel();
  }

  /**
   * Main method for running the game from the command line.
   * @param args command line arguments (expects game file name as first argument).
   */
  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Usage: java enginedriver.GameEngineApp <game-file> \n You are seeing this because you are" +
              " trying to run the engine with no data. Parsing must be done so we can hand data to the engine.");
      System.exit(1);
    }

    String gameFile = args[0];
    GameEngineApp app = new GameEngineApp(gameFile, new InputStreamReader(System.in), System.out);

    try {
      app.start();
    } catch (IOException e) {
      System.err.println("Error running game: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
}
