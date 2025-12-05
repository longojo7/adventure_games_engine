package engine.view;

import java.util.List;

/**
 * Interface for game view.
 */
public interface IGameView {
  // need to write logic for these methods
  void updateImage(String path);
  void updateDescription(String description);
  void updateInventory(List<String> playerInventory, List<String> roomInventory);
  void updateHealth(String health);
}
