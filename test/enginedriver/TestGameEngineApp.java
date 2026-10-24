package enginedriver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

// Test start() directly: main() calls System.exit(), which would
// terminate the JUnit process if invoked without process isolation.
public class TestGameEngineApp {

  private GameEngineApp createApp(Path gameFile, StringWriter output) {
    return new GameEngineApp(
            gameFile.toString(),
            "-text",
            new StringReader("Test Player\nQ\n"),
            output
    );
  }

  private Path writeGame(String filename, String json) throws IOException {
    return Files.writeString(temporaryDirectory.resolve(filename), json);
  }

  @TempDir
  Path temporaryDirectory;

  @Test
  void propagatesMissingGameFile() {
    Path missingFile = temporaryDirectory.resolve("missing.json");
    StringWriter output = new StringWriter();
    GameEngineApp app = createApp(missingFile, output);

    assertThrows(FileNotFoundException.class, app::start);
    assertEquals("", output.toString());
  }

  @Test
  void propagatesMalformedJson() throws IOException {
    Path gameFile = writeGame("malformed.json", "{");
    StringWriter output = new StringWriter();
    GameEngineApp app = createApp(gameFile, output);

    IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, app::start);

    assertTrue(exception.getMessage().contains(gameFile.toString()));
    assertNotNull(exception.getCause());
    assertEquals("", output.toString());
  }

  @Test
  void propagatesInvalidGameStructure() throws IOException {
    Path gameFile = writeGame("missing-rooms.json", "{}");
    StringWriter output = new StringWriter();
    GameEngineApp app = createApp(gameFile, output);

    IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, app::start);

    assertEquals(
            "The game file must contain a 'rooms' array.",
            exception.getMessage()
    );
    assertEquals("", output.toString());
  }

  @Test
  void startsValidTextGameAndProcessesQuit() throws IOException {
    Path gameFile = writeGame("valid.json", """
            {
              "rooms": [
                {
                  "room_number": "1",
                  "room_name": "Test Hallway",
                  "description": "A quiet hallway.",
                  "N": "0",
                  "S": "0",
                  "E": "0",
                  "W": "0"
                }
              ]
            }
            """);

    StringWriter output = new StringWriter();
    GameEngineApp app = createApp(gameFile, output);

    assertDoesNotThrow(app::start);

    String gameOutput = output.toString();
    assertTrue(gameOutput.contains("You will now be named: Test Player"));
    assertTrue(gameOutput.contains(
            "You start your adventure in the Test Hallway"));
    assertTrue(gameOutput.contains("A quiet hallway."));
    assertTrue(gameOutput.contains("Thank you for playing! Goodbye."));
  }
}
