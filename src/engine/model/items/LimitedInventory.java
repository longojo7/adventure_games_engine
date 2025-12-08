package engine.model.items;

import java.util.List;

/**
 * Inventory with a maximum weight capacity.
 */
public class LimitedInventory extends InfiniteInventory {
  int maxWeight;

  /**
   * Constructs a limited inventory with initial items and max weight.
   *
   * @param items the initial list of items
   * @param maxWeight the maximum weight capacity
   */
  public LimitedInventory(List<Item> items, int maxWeight) {
    super(items);
    this.maxWeight = maxWeight;
  }

  /**
   * Constructs an empty limited inventory with max weight.
   *
   * @param maxWeight the maximum weight capacity
   */
  public LimitedInventory(int maxWeight) {
    super();
    this.maxWeight = maxWeight;
  }

  /**
   * Gets the maximum weight capacity.
   *
   * @return the max weight
   */
  int getMaxWeight() {
    return maxWeight;
  }

  /**
   * Sets the maximum weight capacity.
   *
   * @param maxWeight the max weight to set
   */
  public void setMaxWeight(int maxWeight) {
    this.maxWeight = maxWeight;
  }

  @Override
  public boolean takeFrom(IInventory inventory, String name) {
    Item item = Item.fromName(name);
    if (item == null) {
      return false;
    }
    if (currentWeight() + item.getWeight() > maxWeight) {
      return false;
    }
    return super.takeFrom(inventory, name);
  }

  /**
   * Calculates the current total weight of items.
   *
   * @return the current weight
   */
  private int currentWeight() {
    return getItems().stream().reduce(0,
            (value, item) -> value + item.getWeight(),
            Integer::sum);
  }
}
