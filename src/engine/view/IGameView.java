package engine.view;

import java.awt.event.ActionListener;
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

  void updateScore(String score);

  void updateRank(String rank);


  void updateFeedback(String feedback);

  void appendFeedback(String append);

  String getAnswer();

  String selectedItemPlayer();

  String selectedItemRoom();

  void addActionListener(ActionListener listener);

  void display();
}
