package engine.model.player;

import engine.model.events.EventHandler;

/**
 * Interface for managing player health.
 */
public interface IHealth {

  /**
   * Applies damage to health.
   *
   * @param damage the amount of damage to apply
   */
  void damage(int damage);

  /**
   * Heals the player.
   *
   * @param heal the amount of health to restore
   */
  void heal(int heal);

  /**
   * Gets the current health.
   *
   * @return the current health value
   */
  int getHealth();

  /**
   * Gets the maximum health.
   *
   * @return the maximum health value
   */
  int getMaxHealth();

  /**
   * Restores health to maximum.
   */
  void sleep();

  /**
   * Gets the current health status description.
   *
   * @return the status string
   */
  String getStatus();

  /**
   * Adds an event handler for health events.
   *
   * @param eventHandler the event handler to add
   */
  void addEventHandler(EventHandler eventHandler);
}
