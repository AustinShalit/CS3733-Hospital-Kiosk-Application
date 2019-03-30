package edu.wpi.cs3733.d19.teamO;

import java.io.IOException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("UI")
class ProjectTest extends ApplicationTest {
  private Stage stage;

  @Override
  public void start(Stage stage) throws IOException {
    new Project().start(stage);
    this.stage = stage;
  }

  @Test
  void showingTest() {
    assertTrue(stage.isShowing());
  }

  @Test
  void titleTest() {
    assertEquals("Team O Kiosk Application", stage.getTitle());
  }
}
