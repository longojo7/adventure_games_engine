package engine.model.player;

import org.junit.jupiter.api.Test;

import java.util.Map;

import engine.model.events.EventHandler;
import engine.model.events.GameEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the ThreshHoldHealth class.
 */
public class TestThresholdHealth {

  /**
   * Tests getters for health and maxhealth.
   */
  @Test
  public void testGetters() {
    Map<Integer, String> thresholds = Map.of(0, "Sleep", 50, "Awake");
    IHealth health = new ThresholdHealth(75, 100, thresholds);
    assertEquals(75, health.getHealth());
    assertEquals(100, health.getMaxHealth());
  }

  @Test
  public void testDamage() {
    Map<Integer, String> thresholds = Map.of(0, "Sleep", 50, "Tired",
            75, "Awake");
    IHealth health = new ThresholdHealth(75, 100, thresholds);
    assertEquals(75, health.getHealth());
    assertEquals("Awake", health.getStatus());

    // take damage
    health.damage(30);
    assertEquals(45, health.getHealth());
    assertEquals("Tired", health.getStatus());
  }

  /**
   * Test that damage above health puts a player to sleep and health at 0.
   */
  @Test
  public void testDamageSleep() {
    Map<Integer, String> thresholds = Map.of(0, "Sleep", 50, "Awake");
    IHealth health = new ThresholdHealth(100, 100, thresholds);
    health.damage(101);
    assertEquals(0, health.getHealth());
  }

  /**
   * Tests that adding an event handler works and when dealing sleep worthy (lethal) damage
   * it correctly triggers sleep and delivers the expected GameEvent.
   */
  @Test
  public void testDamageSleepEvent() {
    Map<Integer, String> thresholds = Map.of(0, "Sleeping", 50, "Awake");
    ThresholdHealth health = new ThresholdHealth(30, 100, thresholds);
    class TestHandler implements EventHandler {
      GameEvent received = null;
      @Override
      public void onEvent(GameEvent event) {
        this.received = event;
      }
    }
    TestHandler handler = new TestHandler();
    health.addEventHandler(handler);
    health.damage(50);
    assertNotNull(handler.received);
    assertEquals("You have fallen asleep.", handler.received.getMessage());
  }

  /**
   * Tests the heal method in a normal case.
   */
  @Test
  public void testHealNormal() {
    Map<Integer, String> thresholds = Map.of(0, "Sleeping", 50, "Awake");
    ThresholdHealth health = new ThresholdHealth(30, 100, thresholds);
    health.heal(50);
    assertEquals(80, health.getHealth());
  }

  /**
   * Tests tht heal doesn't go above maxhealth.
   */
  @Test
  public void testHealIncreasesMaxHealth() {
    Map<Integer, String> thresholds = Map.of(0, "Sleeping", 50, "Awake");
    ThresholdHealth health = new ThresholdHealth(30, 50, thresholds);
    health.heal(50);
    assertEquals(50, health.getHealth());
    assertEquals(50, health.getMaxHealth());
  }

  /**
   * Tests that the get status works for different health values.
   */
  @Test
  public void testHealthStatus() {
    Map<Integer, String> thresholds = Map.of(0, "SLEEP", 40, "WOOZY",
            70, "FATIGUED", 101, "AWAKE");
    IHealth healthSleep = new ThresholdHealth(0, 100, thresholds);
    assertEquals("SLEEP", healthSleep.getStatus());
    IHealth healthWoozy = new ThresholdHealth(20, 100, thresholds);
    assertEquals( "WOOZY", healthWoozy.getStatus());
    IHealth healthFatigued = new ThresholdHealth(55, 100, thresholds);
    assertEquals("FATIGUED", healthFatigued.getStatus());
    IHealth healthAwake = new ThresholdHealth(100, 100, thresholds);
    assertEquals("AWAKE", healthAwake.getStatus());
  }
}
