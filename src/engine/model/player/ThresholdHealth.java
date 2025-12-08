package engine.model.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import engine.model.events.EventHandler;
import engine.model.events.GameEvent;

/**
 * Health system with threshold-based status descriptions.
 */
public class ThresholdHealth implements IHealth {
  private int health;
  private final int maxHealth;
  private final Map<Integer, String> thresholds;
  private List<EventHandler> eventHandlers;

  /**
   * Constructs a threshold health system.
   *
   * @param health the initial health
   * @param maxHealth the maximum health
   * @param thresholds map of health thresholds to status descriptions
   */
  public ThresholdHealth(int health, int maxHealth, Map<Integer, String> thresholds) {
    this.health = health;
    this.maxHealth = maxHealth;
    this.thresholds = thresholds;

    eventHandlers = new ArrayList<>();
  }

  @Override
  public void damage(int damage) {
    health -= damage;

    if (health <= 0)
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

    // had to update this method to handle if health is 0 or below
    if (health <= keys.get(0)) {
      return thresholds.get(keys.get(0));
    }

    // then loop through the keys to and see if health is less than and return thresh if so
    for (Integer key : keys) {
      if (health < key) {
        return thresholds.get(key);
      }
    }
    // return last key if health is greater than all keys
    return thresholds.get(keys.get(keys.size() - 1));
  }

  @Override
  public void addEventHandler(EventHandler eventHandler) {
    eventHandlers.add(eventHandler);
  }
}
