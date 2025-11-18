package engine.model.items;

import java.util.Objects;

public class Item {
  private String id;
  private String name;
  private int value;
  private int weight;
  private String description;
  private int maxUses;
  private int usesRemaining;

  private Item(String id,
               String name,
               int value,
               int weight,
               String description,
               int maxUses,
               int usesRemaining) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.weight = weight;
    this.description = description;
    this.maxUses = maxUses;
    this.usesRemaining = usesRemaining;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getValue() {
    return value;
  }

  public int getWeight() {
    return weight;
  }

  public String getDescription() {
    return description;
  }

  public int getMaxUses() {
    return maxUses;
  }

  public int getUsesRemaining() {
    return usesRemaining;
  }

  public void setUsesRemaining(int usesRemaining) {
    this.usesRemaining = usesRemaining;
  }

  public static Item fromID(String id) {
    // TODO: Implement item creation
    // example for testing!
    switch (id) {
      case "thumb drive":
        return new Item("thumb drive", "Thumb Drive", 150, 1,
                "A USB thumb drive for computers", 1000, 1000);
      case "lamp":
        return new Item("lamp", "Lamp", 100, 3,
                "An old oil lamp with flint to spark.", 100, 20);
      case "carrot":
        return new Item("carrot", "Carrot", 5, 10,
                "A HUGE carrot!", 1, 1);
      default:
        throw new IllegalArgumentException("Unknown item id: " + id);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Item)) return false;
    Item item = (Item) o;
    return Objects.equals(id, item.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
