package engine.model.player;

import java.util.List;

import engine.Direction;
import engine.model.items.IInventory;
import engine.model.items.Item;
import engine.model.rooms.Connection;
import engine.model.rooms.IRoom;

public class Player implements IPlayer {
  private String name;
  private final IHealth health;
  private final IInventory inventory;
  private IRoom currentRoom;
  private int obstacleScore;

  public Player(String name, IHealth health, IInventory inventory, IRoom startingRoom, int obstacleScore) {
    this.name = name;
    this.health = health;
    this.inventory = inventory;
    currentRoom = startingRoom;
    this.obstacleScore = obstacleScore;
  }

  public Player(String name, IHealth health, IInventory inventory, IRoom startingRoom) {
    this.name = name;
    this.health = health;
    this.inventory = inventory;
    currentRoom = startingRoom;
  }

  @Override
  public String healthStatus() {
    return health.getStatus();
  }

  @Override
  public int currentHealth() {
    return health.getHealth();
  }

  @Override
  public int maxHealth() {
    return health.getMaxHealth();
  }

  @Override
  public void damage(int damage) {
    health.damage(damage);
  }

  @Override
  public void heal(int heal) {
    health.heal(heal);
  }

  @Override
  public int getScore() {
    return obstacleScore + inventory.getValue();
  }

  @Override
  public void addObstacleScore(int score) {
    obstacleScore += score;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public IRoom peek(Direction direction) {
    Connection connection = currentRoom.getConnection(direction);

    return (connection != null && connection.isOpen())
            ? connection.getDestination() : null;
  }

  @Override
  public boolean walk(Direction direction) {
    IRoom destination = peek(direction);
    if (destination == null)
      return false;
    currentRoom = destination;
    return true;
  }

  @Override
  public IRoom getCurrentRoom() {
    return currentRoom;
  }

  @Override
  public boolean pickupItem(String id) {
    return inventory.takeFrom(currentRoom.getInventory(), id);
  }

  @Override
  public boolean dropItem(String id) {
    return currentRoom.getInventory().takeFrom(inventory, id);
  }

  @Override
  public boolean hasItem(String id) {
    return inventory.hasItem(id);
  }

  @Override
  public List<Item> itemsList() {
    return inventory.getItems();
  }
}
