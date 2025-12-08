package engine.model.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test class for the limitedInventory class.
 */
public class TestLimitedInventory {

  /**
   * Test setup.
   * @throws NoSuchFieldException if the items field not found from reflection
   * @throws IllegalAccessException if reflection fails to set the factory field
   */
  @BeforeEach
  void seedFactory() throws NoSuchFieldException, IllegalAccessException {
    Item thumbDrive = new Item("Thumb Drive", 150,
            1, "A USB storage device.", 1, 1);
    Item carrot = new Item("Carrot", 0, 10,
            "A fresh carrot.", 1, 1);
    Item lamp = new Item("Lamp", 100, 3,
            "An old oil lamp with flint to spark.", 100, 20);

    // Seed ItemFactory using reflection
    Field f = ItemFactory.class.getDeclaredField("items");
    f.setAccessible(true);
    Map<String, Item> map = new HashMap<>();
    map.put("thumb drive", thumbDrive);
    map.put("carrot", carrot);
    map.put("lamp", lamp);
    f.set(ItemFactory.getInstance(), map);
  }

  /**
   * Tests getting the max weight.
   */
  @Test
  public void testGetMaxWeight() {
    LimitedInventory inv = new LimitedInventory(13);
    assertEquals(13, inv.getMaxWeight());
  }

  /**
   * Tests setting the max weight.
   */
  @Test
  public void testSetMaxWeight() {
    LimitedInventory inv = new LimitedInventory(50);
    inv.setMaxWeight(13);
    assertEquals(13, inv.getMaxWeight());
  }

  /**
   * Tests that takeFrom works when inventory total weight is below max.
   */
  @Test
  public void testTakeFromBelowMax() {
    Item drive = Item.fromName("thumb drive");
    Item carrot = Item.fromName("carrot");
    LimitedInventory limited = new LimitedInventory(List.of(drive), 13);
    IInventory source = new InfiniteInventory(List.of(carrot));
    assertTrue(limited.takeFrom(source, "carrot"));
    assertEquals(2, limited.getItems().size());
    assertEquals(0, source.getItems().size());
  }

  /**
   * Tests that takeFrom doesn't work when inventory total weight would be above max.
   */
  @Test
  public void testTakeFromAboveMax() {
    Item carrot = Item.fromName("carrot");
    LimitedInventory limited = new LimitedInventory(List.of(carrot), 13);
    limited.addItem("lamp");
    Item drive = Item.fromName("thumb drive");
    IInventory source = new InfiniteInventory(List.of(drive));
    assertFalse(limited.takeFrom(source, "thumb drive"));
    assertEquals(2, limited.getItems().size());
    assertEquals(1, source.getItems().size());
  }

  @Test
  void testCurrentWeightCalculation() {
    Item sword = Item.fromName("thumb drive");
    Item carrot = Item.fromName("carrot");
    LimitedInventory limited = new LimitedInventory(List.of(sword, carrot), 13);
    int weightTotal = sword.getWeight() + carrot.getWeight();
    assertEquals(weightTotal, limited.getItems().stream().mapToInt(Item::getWeight).sum());
  }

}
