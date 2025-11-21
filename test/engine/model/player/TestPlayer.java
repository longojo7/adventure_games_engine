package engine.model.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.Direction;
import engine.model.items.IInventory;
import engine.model.items.InfiniteInventory;
import engine.model.items.Item;
import engine.model.items.ItemFactory;
import engine.model.items.LimitedInventory;
import engine.model.rooms.Connection;
import engine.model.rooms.IRoom;
import engine.model.rooms.Room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test class for the player class.
 */
public class TestPlayer {
  private IHealth health;
  private Map<Integer, String> thresh;
  private IInventory inv;
  private Item key;
  private IRoom r1;
  private IRoom r2;
  private IRoom r3;
  private IPlayer player;

  /**
   * Test setup.
   * @throws NoSuchFieldException if the items field not found from reflection
   * @throws IllegalAccessException if reflection fails to set the factory field
   */
  @BeforeEach
  public void setup() throws NoSuchFieldException, IllegalAccessException {
    thresh = Map.of(0, "Sleeping", 50, "Awake");
    health = new ThresholdHealth(100, 100, thresh);
    inv = new LimitedInventory(13);
    r1 = new Room("Entrance", "Your standing in the entrance",
            Map.of(), List.of(), null, new InfiniteInventory());
    r2 = new Room("Hallway", "Your standing in the hallway",
            Map.of(), List.of(), null, new InfiniteInventory());
    r3 = new Room("Office", "You are in the office",
            Map.of(), List.of(), null, new InfiniteInventory());

    // create a connection A to B (open)
    Connection c1 = new Connection(true, r2);

    // create a connection A -> C (closed)
    Connection c2 = new Connection(false, r3);

    // Wire them into r1 connection map
    r1 = new Room("Entrance", "Your standing in the entrance",
            Map.of(Direction.North, c1, Direction.South, c2),
            List.of(), null, new InfiniteInventory());
    player = new Player("Player1", health, inv, r1);

    // Seed ItemFactory (had to look this up because I had no idea how to test)
    ItemFactory factory = ItemFactory.getInstance();

    // Reflectively set the private items field and add the key item to factory
    Field f = ItemFactory.class.getDeclaredField("items");
    f.setAccessible(true);
    Map<String, Item> map = new HashMap<>();
    map.put("key", key);
    f.set(factory, map);
  }

  /**
   * Tests getting health information for a player.
   */
  @Test
  public void testGetHealthInfo() {
    assertEquals("Awake", player.healthStatus());
    assertEquals(100, player.currentHealth());
    assertEquals(100, player.maxHealth());
  }

  /**
   * Tests that players health can be damaged.
   */
  @Test
  public void testHealthDamage() {
    player.damage(10);
    assertEquals(90, player.currentHealth());

    // putting a player to sleep
    player.damage(101);
    assertEquals("Sleeping", player.healthStatus());
    assertEquals(0, player.currentHealth());
  }

  /**
   * Tests that player can heal and it affects health.
   */
  @Test
  public void testHealthHeal() {
    // shouldn't go above max health
    player.heal(10);
    assertEquals(100, player.currentHealth());
    assertEquals("Awake", player.healthStatus());

    // new player with low health
    IHealth health2 = new ThresholdHealth(20, 100, thresh);
    IPlayer player2 = new Player("Player2", health2, inv, r1);
    player2.heal(20);
    assertEquals(40, player2.currentHealth());
  }

  /**
   * Tests getting and setting a player name.
   */
  @Test
  public void testPlayerNameOperations() {
    // getter works for name
    assertEquals("Player1", player.getName());
    player.setName("Josh");
    assertEquals("Josh", player.getName());
  }

  /**
   * Tests getting a player score and adding an obstacle affecting the score.
   */
  @Test
  public void testScoreOperations() {
    // getter for score should be 0 since null
    assertEquals(0, player.getScore());

    // adding obstacle score should now change
    player.addObstacleScore(200);
    assertEquals(200, player.getScore());
  }

  /**
   * Tests getting the current room a player is in.
   */
  @Test
  public void testGetRoom() {
    assertEquals(r1, player.getCurrentRoom());
  }

  /**
   * Tests peeking when the connection is open and there.
   */
  @Test
  public void testPeekOpen() {
    IRoom room = player.peek(Direction.North);
    assertEquals(room, player.peek(Direction.North));
  }

  /**
   * Tests peeking when a connection is closed.
   */
  @Test
  public void testPeekClosed() {
    assertNull(player.peek(Direction.South));
  }

  /**
   * Tests peeking when there is no connection.
   */
  @Test
  public void testPeekNoConnection() {
    assertNull(player.peek(Direction.West));
  }

  /**
   * Tests walking when there is an open connection.
   */
  @Test
  public void testWalkOpen() {
    assertTrue(player.walk(Direction.North));
    assertEquals("Hallway", player.getCurrentRoom().getName());
  }

  /**
   * Tests walking when a connection is there but closed.
   */
  @Test
  public void testWalkClosed() {
    assertFalse(player.walk(Direction.South));

    // should stay in entrance
    assertEquals("Entrance", player.getCurrentRoom().getName());
  }

  /**
   * Tests walking when there is no destination.
   */
  @Test
  public void testWalkNoDestination() {
    assertFalse(player.walk(Direction.East));

    // should stay in entrance
    assertEquals("Entrance", player.getCurrentRoom().getName());
  }

  /**
   * Tests a player picking an item.
   */
  @Test
  public void testPickUpItem() {
    // add item to room inventory
    r1.getInventory().addItem("key");
    // check that player doesn't have it yet but room does
    assertFalse(player.hasItem("key"));
    assertTrue(r1.getInventory().hasItem("key"));

    // player can pick up item in the room and now has item while room doesn't
    assertTrue(player.pickupItem("key"));
    assertTrue(player.hasItem("key"));
    assertFalse(r1.getInventory().hasItem("key"));
  }

  /**
   * Tests player trying to pick up an item not there.
   */
  @Test
  public void testPickUpItemNo() {
    // room doesn't have item and player shouldn't pick up anything
    assertFalse(r1.getInventory().hasItem("key"));
    assertFalse(player.pickupItem("key"));
  }

  /**
   * Tests player dropping an item.
   */
  @Test
  public void testDropItem() {
    // First put the item in the room
    r1.getInventory().addItem("key");
    // do initial checks
    player.pickupItem("key");
    assertTrue(player.hasItem("key"));

    // check player can drop item and inventory updates
    assertTrue(player.dropItem("key"));
    assertFalse(player.hasItem("key"));
    assertTrue(r1.getInventory().hasItem("key"));
  }

  /**
   * Tests that a player cannot drop an item it doesn't have
   */
  @Test
  public void testDropItemDoesNotHave() {
    assertFalse(player.dropItem("key"));
    // room shouldn't have item
    assertFalse(r1.getInventory().hasItem("key"));
  }

  /**
   * Tests player has an item.
   */
  @Test
  public void testHasItem() {
    r1.getInventory().addItem("key");
    // player should not have item
    assertFalse(player.hasItem("key"));

    // pick up player should now
    player.pickupItem("key");
    assertTrue(player.hasItem("key"));
  }

  /**
   * Tests getting the item list of the player inventory.
   */
  @Test
  public void testItemList() {
    r1.getInventory().addItem("key");
    player.pickupItem("key");
    List<Item> items = player.itemsList();
    assertEquals(1, items.size());
    assertEquals("key", items.get(0).getId());
  }
}
