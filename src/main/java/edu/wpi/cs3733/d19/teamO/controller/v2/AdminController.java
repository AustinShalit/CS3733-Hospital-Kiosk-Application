package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

@FxmlController(url = "AdminWindow.fxml")
public class AdminController implements Controller {

  @FXML
  private VBox root;
  @FXML
  private JFXButton mapeditButton;
  @FXML
  private JFXButton employeeButton;
  @FXML
  private JFXButton editdatabaseButton;

  @FXML
  void initialize() {
    System.out.println("weiogeigbiawoegbaoweigbaweiogbaewogbaewogiwe");
  }

  @FXML
  void mapeditOnAction() {

  }

  @FXML
  void employeeOnAction() {

  }

  @FXML
  void editdatabaseOnAction() {

  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    AdminController create();
  }
}
