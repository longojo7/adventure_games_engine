package engine.model.items;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A test class for the Item class.
 */
public class TestItem {

  /**
   * Tests creating an item from an ID initializes all the right values and the getters work.
   */
  @Test
  void testItemFromID() {
    Item sword = Item.fromID("lamp");
    assertEquals("lamp", sword.getId());
    assertEquals("Lamp", sword.getName());
    assertEquals(100, sword.getValue());
    assertEquals(3, sword.getWeight());
    assertEquals("An old oil lamp with flint to spark.", sword.getDescription());
    assertEquals(100, sword.getMaxUses());
    assertEquals(20, sword.getUsesRemaining());
  }

  /**
   * Tests that an unknown ID throws an exception.
   */
  @Test
  void testUnknownItemIDThrows() {
    assertThrows(IllegalArgumentException.class, () -> Item.fromID("hair clippers"));
  }

  /**
   * Tests that the usesRemaining can be updated through its setter.
   */
  @Test
  void testUsesRemainingCanChange() {
    Item potion = Item.fromID("lamp");
    assertEquals(20, potion.getUsesRemaining());
    potion.setUsesRemaining(19);
    assertEquals(19, potion.getUsesRemaining());
  }

  /**
   * Tests that the same ID item is equal.
   */
  @Test
  void testSameID() {
    Item a = Item.fromID("lamp");
    Item b = Item.fromID("lamp");
    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }

  /**
   * Tests that two different IDs are not equal.
   */
  @Test
  void testDifferentID() {
    Item potion = Item.fromID("lamp");
    Item sword = Item.fromID("thumb drive");
    assertNotEquals(potion, sword);
  }

}
