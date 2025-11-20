package engine.model.rooms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import engine.model.items.InfiniteInventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test class for the connection class.
 */
public class TestConnection {

  private IRoom r1;
  private Connection northConn;

  /**
   * Test setup.
   */
  @BeforeEach
  public void setup() {
    r1 = new Room("Hallway", "Hallway after entrance",
            Map.of(), List.of(), null, new InfiniteInventory());
    northConn = new Connection(false, r1);
  }

  /**
   * tests that set open works properly.
   */
  @Test
  public void testSetOpen() {
    northConn.setOpen(true);
    assertTrue(northConn.isOpen());
  }

  /**
   * Test that a connection returns if it's open correctly.
   */
  @Test
  public void testIsOpen() {
    assertFalse(northConn.isOpen());

    // set it to true now and check
    northConn.setOpen(true);
    assertTrue(northConn.isOpen());
  }

  /**
   * tests that we can get the destination properly.
   */
  @Test
  public void testGetDestination() {
    assertEquals(r1, northConn.getDestination());
  }
}
