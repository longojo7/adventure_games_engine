package engine.model.rooms.obstacles;

import java.util.ArrayList;
import java.util.List;

import engine.model.events.EventHandler;
import engine.model.events.GameEvent;

public class Puzzle implements IObstacle {
  private final String name;
  private final ISolution correctSolution;
  private final List<EventHandler> eventHandlers;
  private final String effect;
  private final String description;
  private final String messageOnSolve;
  private final int value;
  private boolean isSolved;

  public Puzzle(String name, ISolution correctSolution, String effect, String description,
                int value, String messageOnSolve, boolean isSolved) {
    this.name = name;
    this.correctSolution = correctSolution;
    this.effect = effect;
    this.description = description;
    this.value = value;
    this.isSolved = isSolved;
    this.messageOnSolve = messageOnSolve;

    eventHandlers = new ArrayList<>();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isActive() {
    return !isSolved;
  }

  @Override
  public boolean attemptSolution(ISolution solution) {
    if (!correctSolution.checkSolution(solution))
      return false;
    isSolved = true;
    for (EventHandler eventHandler : eventHandlers)
      eventHandler.onEvent(new GameEvent(messageOnSolve));
    return true;
  }

  @Override
  public void addEventHandler(EventHandler eventHandler) {
    eventHandlers.add(eventHandler);
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public String getEffects() {
    return effect;
  }

  @Override
  public String getDescription() {
    return description;
  }
}
