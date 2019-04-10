package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.component.MapView;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
public class MapEditController extends Controller {

  Database database = new Database();

  @FXML
  private Button add;
  @FXML
  private Button delete;
  @FXML
  private Button connect;
  @FXML
  private Button update;
  @FXML
  private Button backButton;
  @FXML
  private Button cancel;
  private ComboBox<Node.NodeType> nodeType;
  @FXML
  private MapView map;

  public MapEditController() throws SQLException {
  }

  @FXML
  void initialize() throws IOException {
    if (map != null) {
      Image image = new Image(getClass().getResource("01_thefirstfloor.png").openStream());
      map.setMapImage(image);
      map.addNodesToPane(database.getAllNodes());
      map.addEdgesToPane(database.getAllEdges());
    }
  }

  @FXML
  private void comboBox() {
    nodeType.getItems().addAll(Node.NodeType.values());
  }

  @FXML
  void mainButtonAction(ActionEvent event) throws IOException {
    Stage stage;
    Parent root;
    if (event.getSource() == add) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditAdd.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Team O Kiosk Application");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(add.getScene().getWindow());
      stage.showAndWait();
      map.addNodesToPane(database.getAllNodes());
    } else if (event.getSource() == delete) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditDelete.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Team O Kiosk Application");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(delete.getScene().getWindow());
      stage.showAndWait();
      map.clearNodes();
      map.addNodesToPane(database.getAllNodes());
    } else if (event.getSource() == connect) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditConnect.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Team O Kiosk Application");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(connect.getScene().getWindow());
      stage.showAndWait();
      map.clearEdges();
      map.addEdgesToPane(database.getAllEdges());
    } else if (event.getSource() == update) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditUpdate.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Team O Kiosk Application");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(update.getScene().getWindow());
      stage.showAndWait();
      map.clearNodes();
      map.addNodesToPane(database.getAllNodes());
    }
  }


  @FXML
  void onBackButtonAction(ActionEvent event) {
    switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
  }

  @FXML
  void cancelButtonAction() {
    Stage stage = (Stage) cancel.getScene().getWindow();
    stage.close();
  }




}
