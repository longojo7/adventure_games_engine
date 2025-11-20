package enginedriver;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Simple test class to demonstrate GameEngineApp functionality
 * with different Readable and Appendable implementations.
 */
public class TestGameEngineApp {
  public static void main(String[] args) {
    String testInput = "Josh The Great\nL\nX Monster\nT Room Item 1\nI\nQ\n";

    StringReader input = new StringReader(testInput);

    StringWriter output = new StringWriter();

    GameEngineApp app = new GameEngineApp("test-game.json", input, output);

    try {
      app.start();

      System.out.println("=== GAME OUTPUT ===");
      System.out.println(output.toString());

      String gameOutput = output.toString();
      if (gameOutput.contains("HALLWAY")) {
        System.out.println("\n=== TEST PASSED ===");
        System.out.println("Game successfully started and displayed room information.");
      }

    } catch (IOException e) {
      System.err.println("Error during game execution: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
