package engine.model.player;

import java.util.List;

import engine.Direction;
import engine.model.items.Item;
import engine.model.rooms.IRoom;

public interface IPlayer {
  String healthStatus();

  void damage(int damage);

  void heal(int heal);

  int getScore();

  void addObstacleScore(int score);

  String getName();

  void setName(String name);

  IRoom peek(Direction direction);

  boolean walk(Direction direction);

  IRoom getCurrentRoom();

  boolean pickupItem(String id);

  boolean dropItem(String id);

  boolean hasItem(String id);

  List<Item> itemsList();
}
