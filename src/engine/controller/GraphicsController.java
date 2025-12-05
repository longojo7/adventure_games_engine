package engine.controller;

import java.io.IOException;

import engine.Direction;
import engine.GameState;
import engine.model.IEngineModel;
import engine.view.IGameView;

public class GraphicsController implements GameActions, IGameController {
  private IEngineModel model;
  private IGameView view;

  public GraphicsController(IEngineModel model, IGameView view) {
    this.model = model;
    this.view = view;
    view.set(this);
    view.refresh(model.getGameState());
  }

  @Override
  public void moveNorth() {
    model.walk(Direction.North);
  }

  @Override
  public void moveSouth() {

  }

  @Override
  public void moveEast() {

  }

  @Override
  public void moveWest() {

  }

  @Override
  public void examine(String name) {

  }

  @Override
  public void inspect(String name) {

  }

  @Override
  public void useItem(String name) {

  }

  @Override
  public void takeItem(String name) {

  }

  @Override
  public void dropItem(String name) {

  }

  @Override
  public void saveGame() {

  }

  @Override
  public void restoreGame() {

  }

  @Override
  public void aboutGame() {

  }

  @Override
  public void exitGame() {

  }

  @Override
  public void go() throws IOException {

  }
}
