package engine.model.rooms.obstacles;

import org.junit.jupiter.api.Test;

import java.util.List;

import engine.model.items.Item;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the ItemSolution class.
 */
public class TestItemSolution {

  /**
   * Tests to make sure a solution is match regardless of order of items.
   */
  @Test
  public void testCheckItemSolutionTrue() {
    Item lamp = Item.fromName("lamp");
    Item carrot = Item.fromName("carrot");
    ISolution s1 = new ItemSolution(List.of(lamp, carrot));
    ISolution s2 = new ItemSolution(List.of(lamp, carrot));
    assertTrue(s1.checkSolution(s2));

    // should still be true if the order is different
    ISolution s3 = new ItemSolution(List.of(carrot, lamp));
    assertTrue(s1.checkSolution(s3));
  }

  /**
   * ZTests to make sure an item solution is not a password solution
   */
  @Test
  public void testCheckSolutionFalse() {
    Item lamp = Item.fromName("lamp");
    ISolution item = new ItemSolution(List.of(lamp));
    ISolution password = new PasswordSolution("'password'");
    assertFalse(item.checkSolution(password));
  }

  /**
   * Tests that solutions of different sizes are not a match.
   */
  @Test
  public void testCheckSolutionDifferentSize() {
    Item lamp = Item.fromName("lamp");
    Item carrot = Item.fromName("carrot");
    ISolution s1 = new ItemSolution(List.of(lamp, carrot));
    ISolution s2 = new ItemSolution(List.of(lamp));
    assertFalse(s1.checkSolution(s2));
  }

  /**
   * Tests that solutions of the same size with different items are not a match.
   */
  @Test
  public void testCheckSolutionDifferentItems() {
    Item lamp = Item.fromName("lamp");
    Item carrot = Item.fromName("carrot");
    ISolution s1 = new ItemSolution(List.of(lamp));
    ISolution s2 = new ItemSolution(List.of(carrot));
    assertFalse(s1.checkSolution(s2));
  }

}
