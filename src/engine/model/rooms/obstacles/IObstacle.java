package engine.model.rooms.obstacles;

import engine.model.events.EventHandler;

/**
 * Interface for room obstacles like puzzles and monsters.
 */
public interface IObstacle {

  /**
   * Gets the obstacle name.
   *
   * @return the name
   */
  String getName();

  /**
   * Checks if the obstacle is currently active.
   *
   * @return true if active, false if solved
   */
  boolean isActive();

  /**
   * Attempts to solve the obstacle with a solution.
   *
   * @param solution the solution to attempt
   * @return true if solution is correct, false otherwise
   */
  boolean attemptSolution(ISolution solution);

  /**
   * Forces the obstacle to be solved without checking solution.
   */
  void forceSolve();

  /**
   * Adds an event handler for obstacle events.
   *
   * @param eventHandler the event handler to add
   */
  void addEventHandler(EventHandler eventHandler);

  /**
   * Gets the point value for solving the obstacle.
   *
   * @return the value
   */
  int getValue();

  /**
   * Gets the effects description.
   *
   * @return the effects string
   */
  String getEffects();

  /**
   * Gets the obstacle description.
   *
   * @return the description
   */
  String getDescription();

  /**
   * Gets the message displayed when obstacle is solved.
   *
   * @return the solve message
   */
  String getMessageOnSolve();

  /**
   * Gets the damage dealt by the obstacle.
   *
   * @return the damage amount, default is 0
   */
  default int getDamage() {
    return 0;
  }

  /**
   * Gets the attack description.
   *
   * @return the attack string, default is null
   */
  default String getAttack() {
    return null;
  }
}
