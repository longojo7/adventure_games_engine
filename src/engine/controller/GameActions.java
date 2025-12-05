package engine.controller;

/**
 * Interface that describes game actions.
 */
public interface GameActions {
  void moveNorth();
  void moveSouth();
  void moveEast();
  void moveWest();
  void examine(String name);
  void inspect(String name);
  void useItem(String name);
  void takeItem(String name);
  void dropItem(String name);
  void saveGame();
  void restoreGame();
  void aboutGame();
  void exitGame();
}
