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

  @Test
  void rejectsMissingRoomNumber() {
    assertValidationError(
            "{\"rooms\": [{}]}",
            "Room is missing required property 'room_number'."
    );
  }

  @Test
  void rejectsBlankRoomNumber() {
    assertValidationError(
            "{\"rooms\": [{\"room_number\": \"   \"}]}",
            "Room is missing required property 'room_number'."
    );
  }

  @Test
  void rejectsNonObjectRoomEntry() {
    assertValidationError(
            "{\"rooms\": [\"invalid room\"]}",
            "Each entry in 'rooms' must be a JSON object."
    );
  }

  @Test
  void rejectsMissingRoomName() {
    // Room names are validated during createModel(), not construction.
    JsonBuilder builder = new JsonBuilder(
            new StringReader("""
                  {
                    "rooms": [
                      {
                        "room_number": "1",
                        "description": "A hallway.",
                        "N": "0", "S": "0", "E": "0", "W": "0"
                      }
                    ]
                  }
                  """),
            "test.json"
    );

    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> builder.createModel("Test Player")
    );

    assertEquals(
            "Room 1 is missing required property 'room_name'.",
            exception.getMessage()
    );
  }

  @Test
  void rejectsBlankRoomName() {
    JsonBuilder builder = new JsonBuilder(
            new StringReader("""
                  {
                    "rooms": [
                      {
                        "room_number": "1",
                        "room_name": "   ",
                        "description": "A hallway.",
                        "N": "0", "S": "0", "E": "0", "W": "0"
                      }
                    ]
                  }
                  """),
            "test.json"
    );

    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> builder.createModel("Test Player")
    );

    assertEquals(
            "Room 1 is missing required property 'room_name'.",
            exception.getMessage()
    );
  }

  @Test
  void acceptsEmptyDescription() {
    JsonBuilder builder = new JsonBuilder(
            new StringReader("""
                  {
                    "rooms": [{
                      "room_number": "1",
                      "room_name": "Test Room",
                      "description": "",
                      "N": "0", "S": "0", "E": "0", "W": "0"
                    }]
                  }
                  """),
            "test.json"
    );

    assertDoesNotThrow(() -> builder.createModel("Test Player"));
  }

  @Test
  void rejectsInvalidDescriptions() {
    // Missing, null, and numeric descriptions must all be rejected.
    for (String property : new String[]{
            "",
            "\"description\": null,",
            "\"description\": 123,"
    }) {
      JsonBuilder builder = new JsonBuilder(
              new StringReader("""
                    {
                      "rooms": [{
                        "room_number": "1",
                        "room_name": "Test Room",
                        %s
                        "N": "0", "S": "0", "E": "0", "W": "0"
                      }]
                    }
                    """.formatted(property)),
              "test.json"
      );

      IllegalArgumentException exception = assertThrows(
              IllegalArgumentException.class,
              () -> builder.createModel("Test Player"),
              "Description property: " + property
      );

      assertEquals(
              "Room 1 must define 'description' as a string.",
              exception.getMessage()
      );
    }
  }


  @Test
  void rejectsGameWithoutStartingRoom() {
    JsonBuilder builder = new JsonBuilder(
            new StringReader("""
                  {
                    "rooms": [
                      {
                        "room_number": "2",
                        "room_name": "Other Room",
                        "description": "A valid room.",
                        "N": "0", "S": "0", "E": "0", "W": "0"
                      }
                    ]
                  }
                  """),
            "test.json"
    );

    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> builder.createModel("Test Player")
    );

    assertEquals(
            "The game must define starting room '1'.",
            exception.getMessage()
    );
  }
}