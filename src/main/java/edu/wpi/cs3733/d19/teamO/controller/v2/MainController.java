package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MainController {

  @FXML
  private StackPane root;

  @FXML
  private BorderPane content;

  @FXML
  void initialize() throws IOException {
    Parent view = FXMLLoader.load(MainController.class.getResource("Home.fxml"));
    content.setCenter(view);
  }
}
