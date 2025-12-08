package engine.model.rooms.obstacles;

import java.util.List;

import engine.model.items.Item;

/**
 * Solution that requires specific items.
 */
public class ItemSolution implements ISolution {
  private final List<Item> items;

  /**
   * Constructs an item solution with required items.
   *
   * @param items the list of required items
   */
  public ItemSolution(List<Item> items) {
    this.items = items;
  }

  @Override
  public boolean checkSolution(ISolution solution) {
    if (!(solution instanceof ItemSolution)) return false;
    ItemSolution other = (ItemSolution) solution;
    if (this.items.size() != other.items.size()) return false;
    for (Item item : this.items)
      if (!other.items.contains(item)) return false;
    return true;
  }
}
