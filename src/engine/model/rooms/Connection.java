package engine.model.rooms;

/**
 * Represents a connection between rooms.
 */
public class Connection {
  private boolean isOpen;
  private final IRoom destination;

  /**
   * Constructs a connection to a destination room.
   *
   * @param isOpen whether the connection is open
   * @param destination the destination room
   */
  public Connection(boolean isOpen, IRoom destination) {
    this.isOpen = isOpen;
    this.destination = destination;
  }

  /**
   * Sets whether the connection is open.
   *
   * @param open true to open, false to close
   */
  public void setOpen(boolean open) {
    isOpen = open;
  }

  /**
   * Checks if the connection is open.
   *
   * @return true if open, false otherwise
   */
  public boolean isOpen() {
    return isOpen;
  }

  /**
   * Gets the destination room.
   *
   * @return the destination room
   */
  public IRoom getDestination() {
    return destination;
  }
}
