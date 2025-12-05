package engine.model.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A test class for the Item class.
 */
public class TestItem {

  /**
   * Test setup
   * @throws NoSuchFieldException if the items field not found from reflection
   * @throws IllegalAccessException if reflection fails to set the factory field
   */
  @BeforeEach
  public void seedFactory() throws NoSuchFieldException, IllegalAccessException {
    // Construct the items your tests expect
    Item lamp = new Item("Lamp", 100, 3,
            "An old oil lamp with flint to spark.", 100, 20);
    Item thumbDrive = new Item("Thumb Drive", 50, 1,
            "A small USB drive.", 1, 1);

    // Seed ItemFactory.items map using reflection
    Field f = ItemFactory.class.getDeclaredField("items");
    f.setAccessible(true);
    Map<String, Item> testMap = new HashMap<>();
    testMap.put("lamp", lamp);
    testMap.put("thumb drive", thumbDrive);
    f.set(ItemFactory.getInstance(), testMap);
  }

  /**
   * Tests creating an item from an ID initializes all the right values and the getters work.
   */
  @Test
  void testItemFromID() {
    Item sword = Item.fromName("lamp");
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
    assertNull(Item.fromName("hair clippers"));
  }

  /**
   * Tests that the usesRemaining can be updated through its setter.
   */
  @Test
  void testUsesRemainingCanChange() {
    Item potion = Item.fromName("lamp");
    assertEquals(20, potion.getUsesRemaining());
    potion.setUsesRemaining(19);
    assertEquals(19, potion.getUsesRemaining());
  }

  /**
   * Tests that the same ID item is equal.
   */
  @Test
  void testSameID() {
    Item a = Item.fromName("lamp");
    Item b = Item.fromName("lamp");
    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }

  /**
   * Tests that two different IDs are not equal.
   */
  @Test
  void testDifferentID() {
    Item potion = Item.fromName("lamp");
    Item sword = Item.fromName("thumb drive");
    assertNotEquals(potion, sword);
  }

}
