package engine.model.rooms.obstacles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test class for the Puzzle class.
 */
public class TestPuzzle {

  private ISolution solution;
  private IObstacle puzzle;

  /**
   * Test setup.
   */
  @BeforeEach
  public void setup() {
    solution = new PasswordSolution("'Base Case'");
    puzzle = new Puzzle("RECURSION-PUZZLE", solution,
            "Recursive Madness has you stuck", "Solve the infinite loop!",
            700, "You Solved Recursion!", false, "");
  }

  /**
   * Tests getting the name, value, effects, and description of a puzzle.
   */
  @Test
  public void testGetters() {
    assertEquals("RECURSION-PUZZLE", puzzle.getName());
    assertEquals(700, puzzle.getValue());
    assertEquals("Solve the infinite loop!", puzzle.getDescription());
    assertEquals("Recursive Madness has you stuck", puzzle.getEffects());
  }

  /**
   * Tests to check if the puzzle is active or not when it hasn't been solved
   */
  @Test
  public void testIsActiveNotSolved() {
    assertTrue(puzzle.isActive());
  }

  /**
   * Tests the solved state change when it has been solved.
   */
  @Test
  public void testIsSolvedStateTransitions() {
    // it should be active first
    assertTrue(puzzle.isActive());
    puzzle.attemptSolution(solution);
    assertFalse(puzzle.isActive());
  }

  /**
   * Tests attempting the solution with the wrong solution.
   */
  @Test
  public void testAttemptIncorrectSolution() {
    ISolution wrongSolution = new PasswordSolution("'Wrong Solution'");
    assertFalse(puzzle.attemptSolution(wrongSolution));
    assertTrue(puzzle.isActive());
  }

  /**
   * Tests attempting the solution with the right solution.
   */
  @Test
  public void testAttemptCorrectSolution() {
    assertTrue(puzzle.attemptSolution(solution));
    assertFalse(puzzle.isActive());
  }

  /**
   * Tests that the eventHandler will fire when the puzzle is solved
   */
  @Test
  public void testEventHandlerFiresOnSolve() {
    final boolean[] called = {false};
    puzzle.addEventHandler(event -> called[0] = true);
    assertTrue(puzzle.attemptSolution(solution));
    assertTrue(called[0]);
  }

  @Test
  public void testMessagePassedToEventHandler() {
    final String[] receivedMessage = {null};
    puzzle.addEventHandler(event -> receivedMessage[0] = event.getMessage());
    puzzle.attemptSolution(solution);
    assertEquals("You Solved Recursion!", receivedMessage[0]);
  }

}
