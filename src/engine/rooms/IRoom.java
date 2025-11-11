package engine.rooms;

import java.util.List;

import engine.Direction;
import engine.items.IInventory;

public interface IRoom {
  String getDescription();

  Connection getConnection();

  List<Fixture> getFixtures();

  Puzzle getPuzzle();

  Monster getMonster();

  IInventory getInventory();

  void addConnection(Direction direction, Connection connection);
}
