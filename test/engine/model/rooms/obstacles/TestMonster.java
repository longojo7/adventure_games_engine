package engine.model.rooms.obstacles;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import engine.model.items.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test class for the Monster class.
 */
public class TestMonster {

  /**
   * Tests getting the damage and the attack of a monster.
   */
  @Test
  public void testMonsterGetters() {
    Item lamp = Item.fromName("lamp");
    List<Item> items = new ArrayList<>();
    items.add(lamp);
    ISolution solution = new ItemSolution(items);
    Monster ghost = new Monster("GHOST", solution, "Haunts you",
            "A spooky cloud passes you?", 5, "Spooky attack",
            200, "Ghost is gone!", false);
    assertEquals(5, ghost.getDamage());
    assertEquals("Spooky attack", ghost.getAttack());
  }

  /**
   * Tests that the monster is solvable similar to a puzzle.
   */
  @Test
  public void MonsterCanSolve() {
    Item lamp = Item.fromName("lamp");
    List<Item> items = new ArrayList<>();
    items.add(lamp);
    ISolution solution = new ItemSolution(items);
    Monster ghost = new Monster("GHOST", solution, "Haunts you",
            "A spooky cloud passes you?", 5, "Spooky attack",
            200, "Ghost is gone!", false);
    // Make sure the monster is active first
    assertTrue(ghost.isActive());
    assertTrue(ghost.attemptSolution(solution));
    assertFalse(ghost.isActive());
  }
}
