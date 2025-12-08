package engine.model;

/**
 * Interface for building and exporting game models.
 */
public interface IGameBuilder {

  /**
   * Creates a new engine model with the specified player name.
   *
   * @param playerName the name of the player
   * @return the created engine model
   * @throws Exception if model creation fails
   */
  IEngineModel createModel(String playerName) throws Exception;

  /**
   * Exports the engine model to a string representation.
   *
   * @param model the model to export
   * @return the exported model as a string
   */
  String exportModel(IEngineModel model);
}