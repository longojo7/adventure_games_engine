package engine.model.rooms.obstacles;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test class for the password solution class.
 */
public class TestPasswordSolution {

  /**
   * Tests to make sure the same password solutions match.
   */
  @Test
  public void testCheckPasswordSolutionTrue() {
    ISolution s1 = new PasswordSolution("'password'");
    ISolution s2 = new PasswordSolution("'password'");
    assertTrue(s1.checkSolution(s2));
  }

  /**
   * Tests that two different password solutions don't match.
   */
  @Test
  public void testCheckPasswordSolutionDifferent() {
    ISolution s1 = new PasswordSolution("'password'");
    ISolution s2 = new PasswordSolution("'different'");
    assertFalse(s1.checkSolution(s2));
  }

  /**
   * Tests that two different solution types don't match.
   */
  @Test
  public void testSolutionTypeDifferent() {
    ISolution s1 = new PasswordSolution("'password'");
    ISolution s2 = new ItemSolution(List.of());
    assertFalse(s1.checkSolution(s2));
  }

  /**
   * Tests that passwords are case-sensitive.
   */
  @Test
  public void testPasswordCaseSensitive() {
    ISolution s1 = new PasswordSolution("'PASSWORD'");
    ISolution s2 = new PasswordSolution("'password'");
    assertFalse(s1.checkSolution(s2));
  }

  /**
   * Testing that empty password solutions are the same.
   */
  @Test
  public void testEmptyPassword() {
    ISolution s1 = new PasswordSolution("");
    ISolution s2 = new PasswordSolution("");
    assertTrue(s1.checkSolution(s2));
  }
}
