package engine.model.items;

import java.util.Objects;

public class Item {
  private String name;
  private int value;
  private int weight;
  private String description;
  private int maxUses;
  private int usesRemaining;

  protected Item(String name,
                 int value,
                 int weight,
                 String description,
                 int maxUses,
                 int usesRemaining) {
    this.name = name;
    this.value = value;
    this.weight = weight;
    this.description = description;
    this.maxUses = maxUses;
    this.usesRemaining = usesRemaining;
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

  public static Item fromName(String name)  {
    Item item = ItemFactory.getInstance().getItem(name);
    return item;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Item)) return false;
    Item item = (Item) o;
    return Objects.equals(name, item.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}
