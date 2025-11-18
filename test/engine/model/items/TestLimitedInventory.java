package engine.model.items;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test class for the limitedInventory class.
 */
public class TestLimitedInventory {

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
    Item drive = Item.fromID("thumb drive");
    Item carrot = Item.fromID("carrot");
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
    Item carrot = Item.fromID("carrot");
    LimitedInventory limited = new LimitedInventory(List.of(carrot), 13);
    limited.addItem("lamp");
    Item drive = Item.fromID("thumb drive");
    IInventory source = new InfiniteInventory(List.of(drive));
    assertFalse(limited.takeFrom(source, "thumb drive"));
    assertEquals(2, limited.getItems().size());
    assertEquals(1, source.getItems().size());
  }

  @Test
  void testCurrentWeightCalculation() {
    Item sword = Item.fromID("thumb drive");
    Item carrot = Item.fromID("carrot");
    LimitedInventory limited = new LimitedInventory(List.of(sword, carrot), 13);
    int weightTotal = sword.getWeight() + carrot.getWeight();
    assertEquals(weightTotal, limited.getItems().stream().mapToInt(Item::getWeight).sum());
  }

}
