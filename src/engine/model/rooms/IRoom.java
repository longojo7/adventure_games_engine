package engine.model.rooms;

import java.util.List;

import engine.Direction;
import engine.model.items.IInventory;
import engine.model.rooms.obstacles.IObstacle;
import engine.model.rooms.obstacles.Puzzle;

public interface IRoom {
  String getName();

  String getDescription();

  Connection getConnection(Direction direction);

  List<Fixture> getFixtures();

  IObstacle getObstacle();

  IInventory getInventory();
}
