package engine.model.events;

/**
 * Represents a game event with a message.
 */
public class GameEvent {
  private final String message;

  /**
   * Constructs a game event with a message.
   *
   * @param message the event message
   */
  public GameEvent(String message) {
    this.message = message;
  }

  /**
   * Gets the event message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }
}
