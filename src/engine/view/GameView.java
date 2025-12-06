package engine.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

public class GameView extends JFrame implements IGameView {
  private JButton btnNorth;
  private JButton btnSouth;
  private JButton btnEast;
  private JButton btnWest;
  private JButton btnExaminePlayer;
  private JButton btnExamineRoom;
  private JButton btnUse;
  private JButton btnTake;
  private JButton btnDrop;
  private JButton btnAnswer;
  private JList playerInventoryList;
  private JList roomInventoryList;
  private JLabel view;
  private JLabel direction;
  private JLabel playerInventory;
  private JLabel inventoryRoom;
  private JLabel playerStatus;
  private JLabel description;
  private JTextField answerField;
  private ImageIcon roomIcon;
  private JTextField feedbackField;

  public GameView(String caption) {
    super(caption);
    setSize(800, 600);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new FlowLayout());

    // directional buttons
    this.btnNorth = new JButton("North");
    this.btnNorth.setActionCommand("Move North");
    this.btnSouth = new JButton("South");
    this.btnSouth.setActionCommand("Move South");
    this.btnEast = new JButton("East");
    this.btnEast.setActionCommand("Move East");
    this.btnWest = new JButton("West");
    this.btnWest.setActionCommand("Move West");

    // room inventory actions
    this.btnExamineRoom = new JButton("Examine R");
    this.btnExamineRoom.setActionCommand("Examine Room Item");
    this.btnTake = new JButton("Take");
    this.btnTake.setActionCommand("Take Item");
    String[] names = {"josh","ethan"};
    this.roomInventoryList = new JList(names);

    // player inventory actions
    this.btnExaminePlayer = new JButton("Examine P");
    this.btnExaminePlayer.setActionCommand("Examine Player Item");
    this.btnUse = new JButton("Use");
    this.btnUse.setActionCommand("Use Item");
    this.btnDrop = new JButton("Drop");
    this.btnDrop.setActionCommand("Drop Item");
    String[] items = {"hammer", "key"};
    this.playerInventoryList = new JList(items);

    this.btnAnswer = new JButton("Answer");
    this.btnAnswer.setActionCommand("Answer Item");

    this.roomIcon = new ImageIcon("data/images/foyer.png");
    this.view = new JLabel(roomIcon);

    this.direction = new JLabel("Direction");
    this.playerStatus = new JLabel("Player Status");
    this.playerInventory = new JLabel("Player Inventory");
    this.inventoryRoom = new JLabel("Room Inventory");
    this.description = new JLabel("Description");

    this.answerField = new JTextField();

    this.add(this.btnNorth);
    this.add(this.btnSouth);
    this.add(this.btnEast);
    this.add(this.btnWest);
    this.add(this.btnExamineRoom);
    this.add(this.btnTake);
    this.add(this.roomInventoryList);
    this.add(this.btnExaminePlayer);
    this.add(this.btnDrop);
    this.add(this.btnUse);
    this.add(this.playerInventoryList);
    this.add(this.btnAnswer);
    this.add(this.view);
    this.add(this.direction);
    this.add(this.playerStatus);
    this.add(this.playerInventory);
    this.add(this.inventoryRoom);
    this.add(this.description);
    this.add(this.answerField);
    this.setJMenuBar(this.buildMenu());
  }

  @Override
  public void updateImage(String path) {

  }

  @Override
  public void updateDescription(String description) {

  }

  @Override
  public void updateInventory(List<String> playerInventory, List<String> roomInventory) {

  }

  @Override
  public void updateHealth(String health) {

  }

  @Override
  public void updateFeedback(String feedback) {

  }

  public JMenuBar buildMenu() {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem about = new JMenuItem("About...");
    JMenuItem exit = new JMenuItem("Exit");
    menu.add(about);
    menu.add(exit);
    about.addActionListener((event) ->
            JOptionPane
                    .showMessageDialog(null,
                            "This is our class Calculator"));
    exit.addActionListener((event) ->
            System.exit(0));
    menuBar.add(menu);
    return menuBar;
  }

  @Override
  public String getAnswer() {
    return answerField.getText();
  }

  @Override
  public String selectedItemPlayer() {
    if(playerInventoryList.isSelectionEmpty())
      return "";
    return playerInventoryList.getSelectedValue().toString();
  }

  @Override
  public String selectedItemRoom() {
    if(roomInventoryList.isSelectionEmpty())
      return "";
    return roomInventoryList.getSelectedValue().toString();
  }

  @Override
  public void addActionListener(ActionListener listener) {
    this.btnNorth.addActionListener(listener);
    this.btnSouth.addActionListener(listener);
    this.btnEast.addActionListener(listener);
    this.btnWest.addActionListener(listener);
    this.btnExamineRoom.addActionListener(listener);
    this.btnTake.addActionListener(listener);
    this.btnExaminePlayer.addActionListener(listener);
    this.btnDrop.addActionListener(listener);
    this.btnUse.addActionListener(listener);
    this.btnAnswer.addActionListener(listener);
  }

  @Override
  public void display() {
    setVisible(true);
  }

  public static void main(String[] args) {
    GameView view = new GameView("Adventure Game");
    view.setVisible(true);
  }
}
