package engine.model;

public interface IGameBuilder {
  IEngineModel createModel(String playerName) throws Exception;
}
