package engine.model.rooms;

/**
 * Represents a fixed, non-movable object in a room.
 */
public class Fixture {
  private final String name;
  private final String description;

  /**
   * Constructs a fixture with name and description.
   *
   * @param name the fixture name
   * @param description the fixture description
   */
  public Fixture(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Gets the fixture name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the fixture description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }
}
