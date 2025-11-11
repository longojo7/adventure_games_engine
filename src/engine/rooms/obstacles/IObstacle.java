package engine.rooms.obstacles;

public interface IObstacle {
  String getName();

  boolean isActive();

  boolean affectsTarget();

  boolean attemptSolution(ISolution solution);

  void addSolveListener(SolveListener listener);

  int getValue();

  String getEffects();
}
