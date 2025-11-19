package engine.model.rooms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFixture {

  private Fixture f;

  @BeforeEach
  public void setup() {
    f = new Fixture("Bookshelf",
            "A tall bookshelf that too heavy to move.");
  }

  /**
   * Tests getting the name of the fixture
   */
  @Test
  public void testGetName() {
    assertEquals("Bookshelf", f.getName());
  }

  /**
   * Tests getting the description of the fixture
   */
  @Test
  public void testGetDescription() {
    assertEquals("A tall bookshelf that too heavy to move.", f.getDescription());
  }
}
