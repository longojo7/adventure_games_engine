package engine.model.rooms;

import engine.Direction;
import engine.model.items.IInventory;
import engine.model.items.InfiniteInventory;
import engine.model.rooms.obstacles.IObstacle;
import engine.model.rooms.obstacles.Puzzle;
import engine.model.rooms.obstacles.PasswordSolution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test class for the Room class.
 */
public class TestRoom {

  private String name;
  private String description;
  private Map<Direction, Connection> connections;
  private List<Fixture> fixtures;
  private IInventory inventory;

  /**
   * Test setup.
   */
  @BeforeEach
  public void setup() {
    name = "Entrance";
    description = "Your standing in the entrance. There is a hallway north";
    connections = Map.of();
    fixtures = List.of();
    inventory = new InfiniteInventory();
  }

  /**
   * Tests getting the name of the room.
   */
  @Test
  public void testGetName() {
    IRoom r = new Room(name, description, connections, fixtures, null, inventory, "");
    assertEquals("Entrance", r.getName());
  }

  /**
   * Tests getting the description of the room when there is no obstacle.
   */
  @Test
  public void testGetDescriptionNoObstacle() {
    IRoom r = new Room(name, description, connections, fixtures, null, inventory, "");
    assertEquals("Your standing in the entrance. There is a hallway north",
            r.getDescription());
  }

  /**
   * Tests getting the description of the room when there is an unsolved puzzle.
   */
  @Test
  public void testGetDescriptionNotSolved() {
    IObstacle puzzle = new Puzzle("password", new PasswordSolution("abc"),
            "Blocked by password!", "Computer with password.",
            10, "Solved", false, "");
    Room r = new Room(name, description, connections, fixtures, puzzle, inventory, "");
    assertEquals("Blocked by password!", r.getDescription());
  }

  /**
   * Tests getting the description of the room when there is a solved puzzle.
   */
  @Test
  public void testGetDescriptionSolved() {
    IObstacle puzzle = new Puzzle("password", new PasswordSolution("abc"),
            "Blocked by password!", "Computer with password.",
            10, "Solved", true, "");
    IRoom r = new Room(name, description, connections, fixtures, puzzle, inventory, "");
    assertEquals(description, r.getDescription());
  }

  /**
   * Tests getting the connections of the room.
   */
  @Test
  public void testGetConnection() {
    IRoom r2 = new Room("Hallway", "Hallway after entrance",
            Map.of(), List.of(), null, new InfiniteInventory(), "");
    Connection northConn = new Connection(true, r2);
    connections = Map.of(Direction.North, northConn);
    IRoom r = new Room(name, description, connections, fixtures, null, inventory, "");
    assertEquals(northConn, r.getConnection(Direction.North));
    assertNull(r.getConnection(Direction.South));
  }

  /**
   * Tests getting the fixtures of the room.
   */
  @Test
  public void testGetFixtures() {
    Fixture f1 = new Fixture("Dresser", "A brown dresser.");
    Fixture f2 = new Fixture("Chair", "A heavy metal chair.");
    fixtures = List.of(f1, f2);
    IRoom r = new Room(name, description, connections, fixtures, null, inventory, "");
    List<Fixture> returned = r.getFixtures();
    assertEquals(2, returned.size());
    assertEquals(f1, returned.get(0));
    assertThrows(UnsupportedOperationException.class, () -> returned.add(f1));
  }

  /**
   * Tests getting the obstacles of the room.
   */
  @Test
  public void testGetObstacle() {
    IObstacle obstacle = new Puzzle("password", new PasswordSolution("abc"),
            "Blocked by password!", "Computer with password.",
            10, "Solved", false, "");
    Room r = new Room(name, description, connections, fixtures, obstacle, inventory, "");
    assertEquals(obstacle, r.getObstacle());
  }

  /**
   * Tests getting the inventory of the room
   */
  @Test
  public void testGetInventory() {
    Room r = new Room(name, description, connections, fixtures, null, inventory, "");
    assertEquals(inventory, r.getInventory());

    // test adding an item now
    inventory.addItem("carrot");
    assertTrue(r.getInventory().hasItem("carrot"));
    assertEquals(1, r.getInventory().getItems().size());
  }
}
