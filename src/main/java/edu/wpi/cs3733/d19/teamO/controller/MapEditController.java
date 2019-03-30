package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MapEditController extends Controller{
  @FXML
  private Button Add;
  @FXML
  private Button Delete;
  @FXML
  private Button Connect;
  @FXML
  private Button Update;
  @FXML
  private Button backButton;
  @FXML
  private Button Cancel;

  @FXML
  void MainButtonAction(ActionEvent event) throws IOException {
    Stage stage;
    Parent root;
    if (event.getSource() == Add) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditAdd.fxml"));
      stage.setScene(new Scene(root));
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(Add.getScene().getWindow());
      stage.showAndWait();
    }
   else if (event.getSource() == Delete) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditDelete.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("My modal window");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(Add.getScene().getWindow());
      stage.showAndWait();
    }
    else if (event.getSource() == Connect) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditConnect.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("My modal window");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(Add.getScene().getWindow());
      stage.showAndWait();
    }
    else if (event.getSource() == Update) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditUpdate.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("My modal window");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(Add.getScene().getWindow());
      stage.showAndWait();
    }

    else {
      stage=(Stage)Cancel.getScene().getWindow();
      stage.close();
    }
  }

  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchtoMain(backButton.getScene().getWindow());
    }
  }


}
