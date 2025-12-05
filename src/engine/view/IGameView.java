package engine.view;

import engine.GameState;
import engine.controller.IGameController;

/**
 * Interface for game view.
 */
public interface IGameView {
  // need to write logic for these methods
  void set(IGameController gameController);
  void refresh(GameState gameState);
}
