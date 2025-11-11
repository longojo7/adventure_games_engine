package engine.player;

import engine.Direction;
import engine.rooms.IRoom;

public interface IPlayer {
  int getMaxHealth();

  void damage(int damage);

  void heal(int heal);

  int getScore();

  String getName();

  String setName();

  IRoom peek(Direction direction);

  boolean walk(Direction direction);

  IRoom getCurrentRoom();

  boolean pickupItem(String id);

  boolean dropItem(String id);

  boolean hasItem(String id);
}
