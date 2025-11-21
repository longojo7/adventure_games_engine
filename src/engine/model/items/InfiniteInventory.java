package engine.model.items;

import java.util.ArrayList;
import java.util.List;

public class InfiniteInventory implements IInventory {
  private final List<Item> items;

  public InfiniteInventory(List<Item> items) {
    // this was changed from this.items = List.copyOf(items);
    // since copyOf is an immutable list so takeFrom wouldn't remove!
    this.items = new ArrayList<>(items);
  }

  public InfiniteInventory() {
    this.items = new ArrayList<>();
  }

  @Override
  public boolean takeFrom(IInventory inventory, String id) {
    if (Item.fromID(id) == null) {
      return false;
    }
    if (!inventory.hasItem(id)) return false;
    inventory.removeItem(id);
    addItem(id);
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
  public boolean hasItem(String id) {
    return items.stream().anyMatch(item -> item.getId().equals(id));
  }

  @Override
  public boolean addItem(String id) {
    return items.add(Item.fromID(id));
  }

  @Override
  public boolean removeItem(String id) {
    return items.remove(Item.fromID(id));
  }
}
