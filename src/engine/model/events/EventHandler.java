package engine.model.events;

/**
 * Interface for handling game events.
 */
public interface EventHandler {

  /**
   * Handles a game event.
   *
   * @param event the game event to handle
   */
  void onEvent(GameEvent event);
}
