package engine.model.rooms;

import java.util.List;
import java.util.Map;

import engine.Direction;
import engine.model.items.IInventory;
import engine.model.rooms.obstacles.IObstacle;

/**
 * Represents a room in the game world.
 */
public class Room implements IRoom {
  private final String name;
  private final String description;
  private final Map<Direction, Connection> connections;
  private final List<Fixture> fixtures;
  private final IObstacle obstacle;
  private final IInventory inventory;
  private final String imagePath;

  /**
   * Constructs a room with all properties.
   *
   * @param name        the room name
   * @param description the room description
   * @param connections map of directions to connections
   * @param fixtures    list of fixtures in the room
   * @param obstacle    the obstacle in the room, or null
   * @param inventory   the room's inventory
   * @param imagePath   path to the room's image
   */
  public Room(String name,
              String description,
              Map<Direction, Connection> connections,
              List<Fixture> fixtures,
              IObstacle obstacle,
              IInventory inventory,
              String imagePath) {
    this.name = name;
    this.description = description;
    this.connections = connections;
    this.fixtures = fixtures;
    this.obstacle = obstacle;
    this.inventory = inventory;
    this.imagePath = imagePath.endsWith("png") ? imagePath : "data/images/generic_location.png";
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    if (obstacle != null && obstacle.isActive())
      return obstacle.getEffects();
    return description;
  }

  @Override
  public String getImagePath() {
    if (obstacle != null && obstacle.isActive())
      return obstacle.getImagePath();
    return imagePath;
  }

  @Override
  public Connection getConnection(Direction direction) {
    return connections.get(direction);
  }

  @Override
  public List<Fixture> getFixtures() {
    return List.copyOf(fixtures);
  }

  @Override
  public IObstacle getObstacle() {
    return obstacle;
  }

  @Override
  public IInventory getInventory() {
    return inventory;
  }
}
