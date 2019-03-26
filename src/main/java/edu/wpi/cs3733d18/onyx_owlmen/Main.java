package edu.wpi.cs3733d18.onyx_owlmen;

import edu.wpi.cs3733d18.onyx_owlmen.databasePrototype.DatabasePrototype;
import javafx.application.Application;

public final class Main {

  private Main() {
    throw new UnsupportedOperationException("This is a utility class!");
  }

  public static void main(String... args) {
    Application.launch(DatabasePrototype.class, args);
  }
}
