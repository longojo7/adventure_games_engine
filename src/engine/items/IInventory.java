package engine.items;

import java.util.List;

public interface IInventory {
  boolean takeFrom(IInventory inventory, String id);

  int getValue();

  List<Item> getItems();

  boolean hasItem(String id);

  boolean addItem(String id);

  boolean removeItem(String id);
}
