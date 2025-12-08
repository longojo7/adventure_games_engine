package engine.model.rooms.obstacles;

/**
 * Monster obstacle that attacks and deals damage.
 */
public class Monster extends Puzzle {
  private final int damage;
  private final String attack;

  /**
   * Constructs a monster obstacle.
   *
   * @param name the monster name
   * @param correctSolution the solution to defeat the monster
   * @param effect the effects description
   * @param description the monster description
   * @param damage the damage dealt per turn
   * @param attack the attack description
   * @param value the point value for defeating the monster
   * @param messageOnSolve the message when defeated
   * @param isSolved whether the monster is already defeated
   */
  public Monster(String name, ISolution correctSolution, String effect, String description,
                 int damage, String attack, int value,
                 String messageOnSolve, boolean isSolved, String imagePath) {
    super(name, correctSolution, effect, description, value, messageOnSolve, isSolved,
            imagePath.endsWith("png") ? imagePath : "data/images/generic_monster.png");
    this.damage = damage;
    this.attack = attack;
  }

  @Override
  public int getDamage() {
    return damage;
  }

  @Override
  public String getAttack() {
    return attack;
  }
}
