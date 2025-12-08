package enginedriver;

import engine.controller.GameController;
import engine.controller.GraphicsController;
import engine.controller.IGameController;
import engine.model.DummyModel;
import engine.model.IEngineModel;
import engine.model.JsonBuilder;
import engine.view.GameView;
import engine.view.IGameView;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
  private final String mode;
  private IEngineModel model;
  private IGameController controller;

  /**
   * Constructs a new GameEngineApp.
   * @param gameFileName the name of the game file to load.
   * @param source       the source of user input (e.g., System.in, StringReader, FileReader).
   * @param mode         the game mode.
   * @param output       the destination for game output (e.g., System.out, StringWriter, FileWriter).
   * @throws IllegalArgumentException if any parameter is null.
   */
  public GameEngineApp(String gameFileName, String mode, Readable source, Appendable output) {
    if (gameFileName == null || mode == null || source == null || output == null) {
      throw new IllegalArgumentException("All parameters must be non-null");
    }
    this.gameFileName = gameFileName;
    this.mode= mode;
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

    JsonBuilder builder = null;

    try {
      builder = new JsonBuilder(new FileReader(gameFileName), gameFileName);
      
    } catch (Exception e) {
      e.printStackTrace();
    }



    // Create the appropriate controller based on the mode
    if ("-graphics".equals(mode)) {
      // create a GameView and GraphicsController
      IGameView view = new GameView("Adventure Game");
      String playerName = view.startView();
      try {
        model = builder.createModel(playerName);
      } catch (Exception e) {
        e.printStackTrace();
      }
      controller = new GraphicsController(model, view, builder);
    } else {
      // create a GameController
      try {
        model = builder.createModel("");
      } catch (Exception e) {
        e.printStackTrace();
      }
      controller = new GameController(model, builder, source, output);
    }

    controller.go();
  }

  /**
   * Creates a game model from the specified game file.
   * @param gameFileName the name of the game file to load.
   * @return the initialized game model.
   */
  private IEngineModel createModel(String gameFileName) {
    JsonBuilder builder;

    try {
      builder = new JsonBuilder(new FileReader(gameFileName), gameFileName);
      return builder.createModel("");
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Main method for running the game from the command line.
   * @param args command line arguments (expects game file name as first argument).
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage:");
      System.err.println("  java -jar game_engine <filename> -text");
      System.err.println("  java -jar game_engine <filename> -graphics");
      System.err.println("  java -jar game_engine <filename> -batch <source>");
      System.err.println("  java -jar game_engine <filename> -batch <source> <target>");
      System.exit(1);
    }

    String gameFile = args[0];
    String mode = args[1];

    Readable source;
    Appendable output;

    try {
      switch (mode) {
        case "-text":
          // Text mode: console input and output
          source = new InputStreamReader(System.in);
          output = System.out;
          break;

        case "-graphics":
         //Graphics mode setup.
          source = new InputStreamReader(System.in);
          output = System.out;
          break;

        case "-batch":
          if (args.length < 3) {
            System.err.println("Error: -batch mode requires a source file");
            System.err.println("Usage: java -jar game_engine <filename> -batch <source> [<target>]");
            System.exit(1);
            return;
          }

          String sourceFile = args[2];

          // Batch mode with file input
          try {
            source = new FileReader(sourceFile);
          } catch (FileNotFoundException e) {
            System.err.println("Error: Source file not found: " + sourceFile);
            System.exit(1);
            return;
          }

          // Check if target file is specified
          if (args.length >= 4) {
            // Batch mode with file input and file output
            String targetFile = args[3];
            try {
              output = new java.io.FileWriter(targetFile);
            } catch (IOException e) {
              System.err.println("Error: Cannot create target file: " + targetFile);
              System.exit(1);
              return;
            }
          } else {
            // Batch mode with file input and console output
            output = System.out;
          }
          break;

        default:
          System.err.println("Error: Unknown mode: " + mode);
          System.err.println("Valid modes are: -text, -graphics, -batch");
          System.exit(1);
          return;
      }

      GameEngineApp app = new GameEngineApp(gameFile, mode, source, output);
      app.start();

      // Close file output if it was used
      if (output instanceof java.io.FileWriter) {
        ((java.io.FileWriter) output).close();
      }

    } catch (IOException e) {
      System.err.println("Error running game: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
}
