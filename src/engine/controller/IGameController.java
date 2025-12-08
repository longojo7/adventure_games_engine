package engine.controller;

import java.io.IOException;

/**
 * The text controller interface.
 */
public interface IGameController {

  /**
   * Starts the game controller.
   *
   * @throws IOException if an I/O error occurs
   */
  void go() throws IOException;
}
