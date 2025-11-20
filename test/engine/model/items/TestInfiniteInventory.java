package engine.model.items;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the InfiniteInventory class.
 */
public class TestInfiniteInventory {

  /**
   * Tests the empty parameter constructor initializes an empty inventory.
   */
  @Test
  public void testEmptyConstructor() {
    IInventory inv = new InfiniteInventory();
    assertTrue(inv.getItems().isEmpty());
  }

  /**
   * Tests that the constructor with a list argument creates an inventory of that list.
   */
  @Test
  public void testListConstructor() {
    Item td = Item.fromID("thumb drive");
    List<Item> list = new ArrayList<>(List.of(td));
    IInventory inv = new InfiniteInventory(list);
    assertEquals(1, inv.getItems().size());
    assertEquals(td, inv.getItems().getFirst());
  }

  /**
   * Ensures that InfiniteInventory makes a defensive copy so mutations to
   * the original list outside the classes add method do not affect the inventory.
   */
  @Test
  public void testDefensiveConstructor() {
    Item td = Item.fromID("thumb drive");
    List<Item> list = new ArrayList<>(List.of(td));
    IInventory inv = new InfiniteInventory(list);
    list.add(Item.fromID("carrot"));
    assertEquals(1, inv.getItems().size());
  }

  /**
   * Tests that you can take an item from an inventory.
   */
  @Test
  public void testTakeItem() {
    Item drive = Item.fromID("thumb drive");
    Item carrot = Item.fromID("carrot");
    IInventory inv = new InfiniteInventory(new ArrayList<>(List.of(drive)));
    IInventory inv2 = new InfiniteInventory(new ArrayList<>(List.of(carrot)));
    // carrot moves from inv2 to inv
    assertTrue(inv.takeFrom(inv2, "carrot"));
    assertEquals(0, inv2.getItems().size());
    assertEquals(2, inv.getItems().size());
    // carrot no longer exists in inv2 and itshould return false
    assertFalse(inv.takeFrom(inv2, "carrot"));
  }

  /**
   * Tests get value and items.
   */
  @Test
  public void testGetters() {
    Item drive = Item.fromID("thumb drive");
    IInventory inv = new InfiniteInventory(new ArrayList<>(List.of(drive)));
    assertEquals(150, inv.getValue());
    assertEquals(List.of(drive), inv.getItems());
  }

  /**
   * Tests that an item can be added to an infinite inventory.
   */
  @Test
  public void testAddItem() {
    IInventory inv = new InfiniteInventory();
    assertTrue(inv.addItem("carrot"));
    assertEquals(1, inv.getItems().size());
  }

  /**
   * Tests that an item can be removed from an infinite inventory
   */
  @Test
  public void testRemoveItem() {
    Item carrot = Item.fromID("carrot");
    IInventory inv = new InfiniteInventory(new  ArrayList<>(List.of(carrot)));
    assertTrue(inv.removeItem("carrot"));
    assertEquals(0, inv.getItems().size());
  }
}
