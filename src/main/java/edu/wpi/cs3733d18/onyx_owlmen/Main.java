package edu.wpi.cs3733d18.onyx_owlmen;

import com.opencsv.bean.CsvToBeanBuilder;
import javafx.application.Application;

import java.io.FileReader;

public final class Main {

  private Main() {
    throw new UnsupportedOperationException("This is a utility class!");
  }

  public static void main(String... args) {
    Application.launch(Project.class, args);


  }
}
