package engine.model.items;

import java.util.List;

/**
 * Interface for managing item inventories.
 */
public interface IInventory {

  /**
   * Takes an item from another inventory.
   *
   * @param inventory the inventory to take from
   * @param name the name of the item to take
   * @return true if successful, false otherwise
   */
  boolean takeFrom(IInventory inventory, String name);

  /**
   * Gets the total value of all items in inventory.
   *
   * @return the total value
   */
  int getValue();

  /**
   * Gets all items in the inventory.
   *
   * @return list of items
   */
  List<Item> getItems();

  /**
   * Checks if inventory contains an item.
   *
   * @param name the name of the item
   * @return true if item exists, false otherwise
   */
  boolean hasItem(String name);

  /**
   * Adds an item to the inventory.
   *
   * @param name the name of the item to add
   * @return true if successful, false otherwise
   */
  boolean addItem(String name);

  /**
   * Removes an item from the inventory.
   *
   * @param name the name of the item to remove
   * @return true if successful, false otherwise
   */
  boolean removeItem(String name);
}
