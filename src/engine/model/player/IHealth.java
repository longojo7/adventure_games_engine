package engine.model.player;

import engine.model.events.EventHandler;

public interface IHealth {
  void damage(int damage);
  void heal(int heal);
  int getHealth();
  int getMaxHealth();
  void sleep();
  String getStatus();
  void addEventHandler(EventHandler eventHandler);
}
