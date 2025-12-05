package engine.controller;

import java.io.IOException;

import engine.Direction;
import engine.model.IEngineModel;
import engine.view.IGameView;

public class GraphicsController implements IGameController {
  private IEngineModel model;
  private IGameView view;

  public GraphicsController(IEngineModel model, IGameView view) {
    this.model = model;
    this.view = view;
  }
  @Override
  public void go() throws IOException {

  }
}
