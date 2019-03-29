package edu.wpi.cs3733.d19.teamO;

import javafx.application.Application;

public final class Main {

  private Main() {
    throw new UnsupportedOperationException("This is a utility class!");
  }

  public static void main(String... args) {
    Application.launch(Project.class, args);
  }
}
