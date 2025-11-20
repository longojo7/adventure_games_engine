package engine.model;

import java.util.List;
import java.util.Map;

import engine.Direction;
import engine.model.player.Player;
import engine.model.rooms.IRoom;

public interface IEngineModel {
  void setPlayerName(String name);

  String getPlayerName();

  int getPlayerScore();

  String getPlayerRank();

  boolean walk(Direction direction);

  String peek(Direction direction);

  List<String> playerItemList();

  List<String> roomItemList();

  boolean pickupItem(String itemName);

  boolean dropItem(String itemName);

  String examine(String target);

  boolean useItem(String itemName);

  boolean answerQuestion(String answer);

  String playerStatus();

  String roomName();

  String environmentDescription();

  String monsterAttack();

  Player getPlayer();

  Map<String, IRoom> getRooms();
}
