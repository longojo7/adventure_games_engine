package engine.model.events;

public class GameEvent {
  private final String message;

  public GameEvent(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
