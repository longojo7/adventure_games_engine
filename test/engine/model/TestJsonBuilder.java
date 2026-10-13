package engine.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class TestJsonBuilder {

  @Test
  void rejectsMalformedJson() {
    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new JsonBuilder(new StringReader("{"), "broken.json")
    );

    assertTrue(exception.getMessage().contains("broken.json"));
    assertNotNull(exception.getCause());
  }

  @Test
  void rejectsTopLevelArray() {
    assertValidationError(
            "[]",
            "The game file must contain a JSON object."
    );
  }

  @Test
  void rejectsTopLevelNull() {
    assertValidationError(
            "null",
            "The game file must contain a JSON object."
    );
  }

  @Test
  void rejectsMissingRooms() {
    // {} is valid JSON and a valid object, but lacks required rooms.
    assertValidationError(
            "{}",
            "The game file must contain a 'rooms' array."
    );
  }

  @Test
  void rejectsRoomsWithWrongType() {
    assertValidationError(
            "{\"rooms\": {}}",
            "The game file must contain a 'rooms' array."
    );
  }

  @Test
  void acceptsRoomsArrayDuringConstruction() {
    // Constructor validation checks structure.
    // Starting-room validation will belong to createModel().
    assertDoesNotThrow(
            () -> new JsonBuilder(
                    new StringReader("{\"rooms\": []}"),
                    "empty.json")
    );
  }

  @Test
  void preservesReaderFailureAsCause() {
    IOException readFailure = new IOException("Simulated read failure");

    Reader failingReader = new Reader() {
      @Override
      public int read(char[] buffer, int offset, int length)
              throws IOException {
        throw readFailure;
      }

      @Override
      public void close() {
        // No resource is held by this simulated reader.
      }
    };

    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new JsonBuilder(failingReader, "unreadable.json")
    );

    assertTrue(exception.getMessage().contains("unreadable.json"));
    assertSame(readFailure, exception.getCause());
  }

  private void assertValidationError(String json, String expectedMessage) {
    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new JsonBuilder(new StringReader(json), "test.json")
    );

    assertEquals(expectedMessage, exception.getMessage());
  }
}