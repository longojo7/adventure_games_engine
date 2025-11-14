package engine.model.rooms.obstacles;

public class PasswordSolution implements ISolution {
  private final String password;

  public PasswordSolution(String password) {
    this.password = password;
  }

  @Override
  public boolean checkSolution(ISolution solution) {
    if (!(solution instanceof PasswordSolution)) return false;
    PasswordSolution other = (PasswordSolution) solution;
    return password.equals(other.password);
  }
}
