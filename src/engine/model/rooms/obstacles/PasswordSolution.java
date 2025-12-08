package engine.model.rooms.obstacles;

/**
 * Solution that requires a password string.
 */
public class PasswordSolution implements ISolution {
  private final String password;

  /**
   * Constructs a password solution.
   *
   * @param password the required password
   */
  public PasswordSolution(String password) {
    this.password = password;
  }

  @Override
  public boolean checkSolution(ISolution solution) {
    if (!(solution instanceof PasswordSolution)) return false;
    PasswordSolution other = (PasswordSolution) solution;
    return password.equalsIgnoreCase(other.password);
  }
}
