package engine.model.items;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for the Item factory.
 */
public class TestItemFactory {

  /**
   * Test setup.
   *
   * @throws NoSuchFieldException   if the items field not found from reflection
   * @throws IllegalAccessException if reflection fails to set the factory field
   */
  @BeforeEach
  void resetFactory() throws NoSuchFieldException, IllegalAccessException {
    // clear the static factory before each test
    Field f = ItemFactory.class.getDeclaredField("items");
    f.setAccessible(true);
    f.set(ItemFactory.getInstance(), new HashMap<>());
  }

  /**
   * Tests that ItemFactory returns the same singleton instance each time.
   */
  @Test
  void testSingleton() {
    ItemFactory f1 = ItemFactory.getInstance();
    ItemFactory f2 = ItemFactory.getInstance();
    assertSame(f1, f2);
  }

  /**
   * Tests that item data from JSON can be loaded into the factory.
   */
  @Test
  void testJsonLoadsItems() {
    JSONArray arr = new JSONArray();
    JSONObject obj = new JSONObject();
    obj.put("name", "lamp");
    obj.put("weight", "3");
    obj.put("max_uses", "100");
    obj.put("uses_remaining", "20");
    obj.put("value", "100");
    obj.put("description", "Test lamp");
    arr.add(obj);
    ItemFactory.getInstance().itemsFromJson(arr);
    Item lamp = ItemFactory.getInstance().getItem("lamp");
    assertNotNull(lamp);
    assertEquals("lamp", lamp.getName());
    assertEquals(100, lamp.getValue());
  }

  /**
   * Test that an exception is thrown if the ID doesn't exist.
   */
  @Test
  void testNullIfDoesNotExist() {
    assertNull(Item.fromName("Doesnt_exist"));
  }
}


