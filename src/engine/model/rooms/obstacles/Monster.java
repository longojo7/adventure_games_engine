package engine.model.rooms.obstacles;

public class Monster extends Puzzle {
  private final int damage;
  private final String attack;

  public Monster(String name, ISolution correctSolution, String effect, String description,
                 int damage, String attack, int value, String messageOnSolve, boolean isSolved) {
    super(name, correctSolution, effect, description, value, messageOnSolve, isSolved);
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
