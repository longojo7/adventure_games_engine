package engine.model.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * A test class for the GameEvent class.
 */
public class TestGameEvent {

  /**
   * Tests get message method.
   */
  @Test
  public void testGetMessage() {
    GameEvent event = new GameEvent("You have successfully solved the puzzle");
    assertEquals("You have successfully solved the puzzle", event.getMessage());
  }

  /**
   * Test if message is empty.
   */
  @Test
  void testEmptyMessage() {
    GameEvent event = new GameEvent("");
    assertEquals("", event.getMessage());
  }

  /**
   * Tests if message is null.
   */
  @Test
  void testNullMessageAllowed() {
    GameEvent event = new GameEvent(null);
    assertNull(event.getMessage());
  }
}
