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
  private JLabel playerStatus;
  private JTextArea descriptionText;
  private ImageIcon roomIcon;
  private JTextField answer;
  private JLabel score;
  private JLabel rank;
  private JTextArea feedbackField;

  public GameView(String caption) {
    super(caption);
    setSize(1200, 800);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    // LEFT PANEL
    JPanel leftPanel = new JPanel(new BorderLayout());
    leftPanel.setPreferredSize(new Dimension(600, getHeight()));

    // Top left image panel
    JPanel image = new JPanel(new BorderLayout());
    this.roomIcon = new ImageIcon("data/images/foyer.png");
    this.view = new JLabel(roomIcon);
    image.add(view, BorderLayout.CENTER);
    image.setBorder(BorderFactory.createTitledBorder("View"));
    image.setPreferredSize(new Dimension(600, 400));
    leftPanel.add(image, BorderLayout.NORTH);

    // Bottom left description panel
    JPanel description = new JPanel(new BorderLayout());
    this.descriptionText = new JTextArea();
    descriptionText.setText("This is a very very very very very very long test string to check " +
            "wrapping. If the wrapping doesn't work Prof K is going to be really pissed off");
    this.descriptionText.setLineWrap(true);
    this.descriptionText.setWrapStyleWord(true);
    JScrollPane scroll = new JScrollPane(descriptionText);
    scroll.setBorder(BorderFactory.createTitledBorder("Description"));
    description.add(scroll, BorderLayout.CENTER);
    leftPanel.add(description, BorderLayout.CENTER);

    JPanel feedback = new JPanel(new BorderLayout());
    this.feedbackField = new JTextArea();
    this.feedbackField.setText("Feedback for using, picking up, dropping, examining, and solving" +
            "will be displayed here");
    this.feedbackField.setLineWrap(true);
    this.feedbackField.setWrapStyleWord(true);
    JScrollPane feedScroll = new JScrollPane(feedbackField);
    feedScroll.setBorder(BorderFactory.createTitledBorder("Feedback"));
    feedback.add(feedScroll, BorderLayout.CENTER);
    feedback.setPreferredSize(new Dimension(600, 100));
    leftPanel.add(feedback, BorderLayout.SOUTH);

    // Add left to frame
    this.add(leftPanel, BorderLayout.WEST);

    // RIGHT PANEL
    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setPreferredSize(new Dimension(600, getHeight()));

    // Top right direction panel
    JPanel directionPanel = new JPanel(new BorderLayout());
    JPanel grid = new JPanel(new GridLayout(3, 3));
    this.btnNorth = new JButton(new ImageIcon("data/images/North.png"));
    this.btnSouth = new JButton(new ImageIcon("data/images/South.png"));
    this.btnEast = new JButton(new ImageIcon("data/images/East.png"));
    this.btnWest = new JButton(new ImageIcon("data/images/West.png"));
    btnNorth.setBorderPainted(false);
    btnNorth.setContentAreaFilled(false);
    btnSouth.setBorderPainted(false);
    btnSouth.setContentAreaFilled(false);
    btnEast.setBorderPainted(false);
    btnEast.setContentAreaFilled(false);
    btnWest.setBorderPainted(false);
    btnWest.setContentAreaFilled(false);
    grid.add(new JLabel());
    grid.add(btnNorth);
    grid.add(new JLabel());
    grid.add(btnWest);
    grid.add(new JLabel());
    grid.add(btnEast);
    grid.add(new JLabel());
    grid.add(btnSouth);
    grid.add(new JLabel());
    directionPanel.add(grid, BorderLayout.CENTER);
    directionPanel.setPreferredSize(new Dimension(600, 200));
    directionPanel.setBorder(BorderFactory.createTitledBorder("Direction"));
    rightPanel.add(directionPanel, BorderLayout.NORTH);

    // middle right panel for
    JPanel inventoryPanel = new JPanel(new GridLayout(1, 2));

    // player inventory list
    JPanel player = new JPanel(new BorderLayout());
    String[] items = {"hammer", "key"};
    this.playerInventoryList = new JList(items);
    player.add(playerInventoryList, BorderLayout.CENTER);
    player.setBorder(BorderFactory.createTitledBorder("Player Inventory"));

    // player inventory buttons
    this.btnExaminePlayer = new JButton("Inspect");
    this.btnExaminePlayer.setActionCommand("Examine Player Item");
    this.btnUse = new JButton("Use");
    this.btnUse.setActionCommand("Use Item");
    this.btnDrop = new JButton("Drop");
    this.btnDrop.setActionCommand("Drop Item");
    JPanel playerButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
    playerButtons.add(btnExaminePlayer);
    playerButtons.add(btnUse);
    playerButtons.add(btnDrop);
    player.add(playerButtons, BorderLayout.SOUTH);

    // add everything to inventory panel
    inventoryPanel.add(player);

    // room inventory list
    JPanel room = new JPanel(new BorderLayout());
    String[] names = {"josh","ethan"};
    this.roomInventoryList = new JList(names);
    room.add(roomInventoryList, BorderLayout.CENTER);
    room.setBorder(BorderFactory.createTitledBorder("Room Contents"));

    // room inventory buttons
    this.btnExamineRoom = new JButton("Examine");
    this.btnExamineRoom.setActionCommand("Examine Room Item");
    this.btnTake = new JButton("Take");
    this.btnTake.setActionCommand("Take Item");
    JPanel roomButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
    roomButtons.add(btnExamineRoom);
    roomButtons.add(btnTake);
    room.add(roomButtons, BorderLayout.SOUTH);

    // add everything to inventory panel
    inventoryPanel.add(room);

    // add inventory panel to the right panel
    rightPanel.add(inventoryPanel, BorderLayout.CENTER);

    // bottom right panel to show health status and answer
    JPanel bottomPanel = new JPanel(new GridLayout(1, 2));

    // left side for status
    JPanel statusPanel = new JPanel(new BorderLayout());
    this.playerStatus = new JLabel("Player is wide awake.");
    statusPanel.add(playerStatus, BorderLayout.CENTER);
    statusPanel.setBorder(BorderFactory.createTitledBorder("Player Status"));

    // right side for input
    JPanel answerPanel = new JPanel(new BorderLayout());

    // text field for typing puzzle answers
    this.answer = new JTextField();
    answerPanel.add(answer, BorderLayout.CENTER);

    // Button beside the text field
    JPanel answerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    this.btnAnswer = new JButton("Answer");
    this.btnAnswer.setActionCommand("Answer Item");
    answerButtonPanel.add(btnAnswer);
    answerPanel.add(answerButtonPanel, BorderLayout.SOUTH);
    answerPanel.setBorder(BorderFactory.createTitledBorder("Enter Answer"));

    // Add both panels to wrapper
    bottomPanel.add(statusPanel);
    bottomPanel.add(answerPanel);
    bottomPanel.setPreferredSize(new Dimension(600, 100));

    // Add wrapper to the RIGHT panel
    rightPanel.add(bottomPanel, BorderLayout.SOUTH);

    // add right panel to frame
    this.add(rightPanel, BorderLayout.EAST);
    this.setJMenuBar(this.buildMenu());
  }

  @Override
  public void updateImage(String path) {
    ImageIcon image = new ImageIcon(path);
    this.view.setIcon(image);
    this.view.repaint();
    this.view.revalidate();
  }

  @Override
  public void updateDescription(String description) {
    this.descriptionText.setText(description);
  }

  @Override
  public void updateInventory(List<String> playerInventory, List<String> roomInventory) {
    this.playerInventoryList.setListData(playerInventory.toArray(new String[0]));
    this.roomInventoryList.setListData(roomInventory.toArray(new String[0]));
  }

  @Override
  public void updateHealth(String health) {
    this.playerStatus.setText(health);
  }

  @Override
  public void updateScore(String score) {
    this.score.setText(score);
  }

  @Override
  public void updateRank(String rank) {
    this.rank.setText(rank);
  }

  @Override
  public void updateFeedback(String feedback) {

  }

  public JMenuBar buildMenu() {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem about = new JMenuItem("About the Engine...");
    JMenuItem save = new JMenuItem("Save Game");
    JMenuItem reload = new JMenuItem("Reload Game");
    JMenuItem exit = new JMenuItem("Exit");
    menu.add(about);
    menu.add(save);
    menu.add(reload);
    menu.add(exit);
    about.addActionListener((event) ->
            JOptionPane
                    .showMessageDialog(null,
                            "This is our Adventure Game. It's pretty cool "));
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
