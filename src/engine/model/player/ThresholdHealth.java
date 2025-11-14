package engine.model.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import engine.model.events.EventHandler;
import engine.model.events.GameEvent;

public class ThresholdHealth implements IHealth {
  private int health;
  private final int maxHealth;
  private final Map<Integer, String> thresholds;
  private List<EventHandler> eventHandlers;

  public ThresholdHealth(int health, int maxHealth, Map<Integer, String> thresholds) {
    this.health = health;
    this.maxHealth = maxHealth;
    this.thresholds = thresholds;

    eventHandlers = new ArrayList<>();
  }

  @Override
  public void damage(int damage) {
    if (damage > health)
      sleep();
  }

  @Override
  public void heal(int heal) {
    health += heal;
    if (health > maxHealth)
      health = maxHealth;
  }

  @Override
  public int getHealth() {
    return health;
  }

  @Override
  public int getMaxHealth() {
    return maxHealth;
  }

  @Override
  public void sleep() {
    health = 0;
    for (EventHandler eventHandler : eventHandlers) {
      eventHandler.onEvent(new GameEvent("You have fallen asleep."));
    }
  }

  @Override
  public String getStatus() {
    List<Integer> keys = new ArrayList<>(thresholds.keySet());
    keys.sort(Integer::compareTo);

    String status = thresholds.get(0);
    for (Integer key : keys) {
      if(health < key)
        status = thresholds.get(key);
    }
    return status;
  }

  @Override
  public void addEventHandler(EventHandler eventHandler) {
    eventHandlers.add(eventHandler);
  }
}
