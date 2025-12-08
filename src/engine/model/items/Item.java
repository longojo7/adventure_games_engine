package engine.model.items;

import java.util.Objects;

/**
 * Represents an item in the game.
 */
public class Item {
  private String name;
  private int value;
  private int weight;
  private String description;
  private int maxUses;
  private int usesRemaining;

  /**
   * Constructs an item with specified properties.
   *
   * @param name the item name
   * @param value the item value
   * @param weight the item weight
   * @param description the item description
   * @param maxUses the maximum number of uses
   * @param usesRemaining the remaining uses
   */
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

  /**
   * Gets the item name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the item value.
   *
   * @return the value
   */
  public int getValue() {
    return value;
  }

  /**
   * Gets the item weight.
   *
   * @return the weight
   */
  public int getWeight() {
    return weight;
  }

  /**
   * Gets the item description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the maximum number of uses.
   *
   * @return the max uses
   */
  public int getMaxUses() {
    return maxUses;
  }

  /**
   * Gets the remaining uses.
   *
   * @return the uses remaining
   */
  public int getUsesRemaining() {
    return usesRemaining;
  }

  /**
   * Sets the remaining uses.
   *
   * @param usesRemaining the uses remaining
   */
  public void setUsesRemaining(int usesRemaining) {
    this.usesRemaining = usesRemaining;
  }

  /**
   * Creates an item from its name using the factory.
   *
   * @param name the item name
   * @return the item or null if not found
   */
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
