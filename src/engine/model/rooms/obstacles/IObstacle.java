package engine.model.rooms.obstacles;

import engine.model.events.EventHandler;

public interface IObstacle {
  String getName();

  boolean isActive();

  boolean attemptSolution(ISolution solution);

  void addEventHandler(EventHandler eventHandler);

  int getValue();

  String getEffects();

  String getDescription();
}
