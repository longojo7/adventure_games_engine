package engine.model.rooms;

import java.util.List;
import java.util.Map;

import engine.Direction;
import engine.model.items.IInventory;
import engine.model.items.InfiniteInventory;
import engine.model.items.Item;
import engine.model.rooms.obstacles.IObstacle;
import engine.model.rooms.obstacles.Monster;
import engine.model.rooms.obstacles.Puzzle;

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

  /**
   * Constructs a room with all properties.
   *
   * @param name the room name
   * @param description the room description
   * @param connections map of directions to connections
   * @param fixtures list of fixtures in the room
   * @param obstacle the obstacle in the room, or null
   * @param inventory the room's inventory
   */
  public Room(String name,
              String description,
              Map<Direction, Connection> connections,
              List<Fixture> fixtures,
              IObstacle obstacle,
              IInventory inventory) {
    this.name = name;
    this.description = description;
    this.connections = connections;
    this.fixtures = fixtures;
    this.obstacle = obstacle;
    this.inventory = inventory;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    if (obstacle != null && obstacle.isActive())
      return obstacle.getEffects();
    else
      return description;
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
