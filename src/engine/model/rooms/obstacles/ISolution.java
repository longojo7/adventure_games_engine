package engine.model.rooms.obstacles;

/**
 * Interface for obstacle solutions.
 */
public interface ISolution {

  /**
   * Checks if this solution matches another solution.
   *
   * @param solution the solution to compare against
   * @return true if solutions match, false otherwise
   */
  boolean checkSolution(ISolution solution);
}

