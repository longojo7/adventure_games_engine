package engine.model.rooms;

public class Connection {
  private boolean isOpen;
  private final IRoom destination;

  public Connection(boolean isOpen, IRoom destination) {
    this.isOpen = isOpen;
    this.destination = destination;
  }

  public void setOpen(boolean open) {
    isOpen = open;
  }

  public boolean isOpen() {
    return isOpen;
  }

  public IRoom getDestination() {
    return destination;
  }
}
