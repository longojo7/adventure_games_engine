package engine.model.items;

import java.util.List;

public class LimitedInventory extends InfiniteInventory {
  int maxWeight;

  public LimitedInventory(List<Item> items, int maxWeight) {
    super(items);
    this.maxWeight = maxWeight;
  }

  public LimitedInventory(int maxWeight) {
    super();
    this.maxWeight = maxWeight;
  }

  int getMaxWeight() {
    return maxWeight;
  }

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

  private int currentWeight() {
    return getItems().stream().reduce(0,
            (value, item) -> value + item.getWeight(),
            Integer::sum);
  }
}
