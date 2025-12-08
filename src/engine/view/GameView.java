package engine.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import javax.swing.*;

import engine.controller.GraphicsController;
import engine.controller.IGameController;
import engine.model.IEngineModel;
import engine.model.JsonBuilder;

/**
 * Graphical user interface for the adventure game using Swing components.
 */
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

  JMenuItem exit;
  JMenuItem save;
  JMenuItem reload;

  /**
   * Constructor for a new game view with the specified window caption.
   * @param caption title for window
   */
  public GameView(String caption) {
    super(caption);
    setSize(1200, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new GridLayout(1, 2));
    this.buildView();
    this.setJMenuBar(this.buildMenu());
  }

  private void buildView() {
    // LEFT PANEL
    JPanel leftPanel = new JPanel(new BorderLayout());

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
    this.add(leftPanel);

    // RIGHT PANEL
    JPanel rightPanel = new JPanel(new BorderLayout());

    // Top right direction panel
    JPanel directionPanel = new JPanel(new BorderLayout());
    JPanel grid = new JPanel(new GridLayout(3, 3));
    this.btnNorth = new JButton(new ImageIcon("data/images/North.png"));
    this.btnNorth.setActionCommand("move north");
    this.btnSouth = new JButton(new ImageIcon("data/images/South.png"));
    this.btnSouth.setActionCommand("move south");
    this.btnEast = new JButton(new ImageIcon("data/images/East.png"));
    this.btnEast.setActionCommand("move east");
    this.btnWest = new JButton(new ImageIcon("data/images/West.png"));
    this.btnWest.setActionCommand("move west");
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
    String[] names = {"josh", "ethan"};
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
    this.add(rightPanel);
  }

  /**
   * Builds and returns the menu bar with File menu options.
   * @return the configured menu bar
   */
  public JMenuBar buildMenu() {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem about = new JMenuItem("About the Engine...");
    save = new JMenuItem("Save Game");
    save.setActionCommand("Save");
    reload = new JMenuItem("Reload Game");
    reload.setActionCommand("Reload");
    exit = new JMenuItem("Exit");
    exit.setActionCommand("Exit");
    menu.add(about);
    menu.add(save);
    menu.add(reload);
    menu.add(exit);
    about.addActionListener(event -> {
      ImageIcon icon = new ImageIcon("data/images/game_engine.png");
      JLabel title = new JLabel("<html><h2>Adventure Game Engine</h2></html>",
              SwingConstants.CENTER);
      JLabel subtitle = new JLabel("<html><div style='text-align:center;'>" +
              "This game was created by Josh, Ethan, and Steven.<br>" +
              "You will embark on adventures through our engine where you can move through<br>" +
              "different rooms, interact with items, solves puzzles, and defeat monsters.<br>" +
              "Make sure you don't fall asleep and have fun!</div></html>", SwingConstants.CENTER);
      JLabel imageLabel = new JLabel(icon, SwingConstants.CENTER);
      title.setAlignmentX(Component.CENTER_ALIGNMENT);
      subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
      imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.add(title);
      panel.add(subtitle);
      panel.add(Box.createVerticalStrut(10));
      panel.add(imageLabel);
      JOptionPane.showMessageDialog(null, panel, "About Game Engine",
              JOptionPane.PLAIN_MESSAGE);
    });
    menuBar.add(menu);
    return menuBar;
  }

  @Override
  public void gameOver(String status, String score, String rank) {
    ImageIcon endImage = new ImageIcon("data/images/nighty_night.png");
    JLabel thanks = new JLabel("<html><h2>Thanks for playing!</h2></html>",
            SwingConstants.CENTER);
    JLabel nameStatus = new JLabel("<html><div style='text-align:center;'>" +
            "The status will go here.<br>" +
            "The score will go here.<br>" +
            "The rank will go here.<br>" +
            "Come back soon!</div></html>", SwingConstants.CENTER);
    JLabel endImageLabel = new JLabel(endImage, SwingConstants.CENTER);
    thanks.setAlignmentX(Component.CENTER_ALIGNMENT);
    nameStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
    endImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(thanks);
    panel.add(nameStatus);
    panel.add(Box.createVerticalStrut(10));
    panel.add(endImageLabel);
    JOptionPane.showMessageDialog(null, panel,
            "Game Over!", JOptionPane.PLAIN_MESSAGE);
    System.exit(0);
  }

  /**
   *
   * @return
   */
  public String startView() {
    String playerName = JOptionPane.showInputDialog(null,
            "Enter a name for your Adventurer:", "Player Name",
            JOptionPane.QUESTION_MESSAGE);
    if (playerName == null || playerName.trim().isEmpty()) {
      System.exit(0);
    }
    return playerName;
  }

  @Override
  public void updateImage(String path) {
    this.view.setIcon(new ImageIcon(path));
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
    this.feedbackField.setText(feedback);
  }

  @Override
  public void appendFeedback(String append) {
    updateFeedback(feedbackField.getText() + append);
  }

  @Override
  public String getAnswer() {
    return answer.getText();
  }

  @Override
  public String selectedItemPlayer() {
    if (playerInventoryList.isSelectionEmpty())
      return "";
    return playerInventoryList.getSelectedValue().toString();
  }

  @Override
  public String selectedItemRoom() {
    if (roomInventoryList.isSelectionEmpty())
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
    this.exit.addActionListener(listener);
    this.save.addActionListener(listener);
    this.reload.addActionListener(listener);
  }

  @Override
  public void display() {
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public static void main(String[] args) throws Exception {
    JsonBuilder builder;
    try {
      builder = new JsonBuilder(new FileReader("data/alignquest.json"));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    IGameView view = new GameView("Adventure Game");
    String playerName = ((GameView) view).startView();
    IEngineModel model = builder.createModel(playerName);

    IGameController controller = new GraphicsController(model, view);
    controller.go();
  }
}
