package engine.model.items;

import java.util.List;

public interface IInventory {
  boolean takeFrom(IInventory inventory, String name);

  int getValue();

  List<Item> getItems();

  boolean hasItem(String name);

  boolean addItem(String name);

  boolean removeItem(String name);
}
