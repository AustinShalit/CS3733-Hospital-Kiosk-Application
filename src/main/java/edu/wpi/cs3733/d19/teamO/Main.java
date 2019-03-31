package edu.wpi.cs3733.d19.teamO;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;
import javafx.application.Application;

public final class Main {

  private Main() {
    throw new UnsupportedOperationException("This is a utility class!");
  }

  public static void main(final String... args) throws IOException {
    LogManager.setupLoggers();
    Application.launch(Project.class, args);
  }
}
