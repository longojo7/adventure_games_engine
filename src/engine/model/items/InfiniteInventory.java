package engine.model.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory with unlimited capacity.
 */
public class InfiniteInventory implements IInventory {
  private final List<Item> items;

  /**
   * Constructs an infinite inventory with initial items.
   *
   * @param items the initial list of items
   */
  public InfiniteInventory(List<Item> items) {
    this.items = new ArrayList<>(items);
  }

  /**
   * Constructs an empty infinite inventory.
   */
  public InfiniteInventory() {
    this.items = new ArrayList<>();
  }

  @Override
  public boolean takeFrom(IInventory inventory, String name) {
    if (Item.fromName(name) == null) {
      return false;
    }
    if (!inventory.hasItem(name)) return false;
    inventory.removeItem(name);
    addItem(name);
    return true;
  }

  @Override
  public int getValue() {
    return items.stream().reduce(0,
            (value, item) -> value + item.getValue(),
            Integer::sum);
  }

  @Override
  public List<Item> getItems() {
    if (items == null) {
      return new ArrayList<>();
    }
    return List.copyOf(items);
  }

  @Override
  public boolean hasItem(String name) {
    return items.stream().anyMatch(item -> item.getName().equalsIgnoreCase(name));
  }

  @Override
  public boolean addItem(String name) {
    Item item = Item.fromName(name);
    if (item == null) { return false; }
    return items.add(item);
  }

  @Override
  public boolean removeItem(String name) {
    Item item = Item.fromName(name);
    if (item == null) { return false; }
    return items.remove(item);
  }
}
