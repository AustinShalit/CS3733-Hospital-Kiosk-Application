package edu.wpi.cs3733.d19.teamO.controller.v2;

import javax.inject.Inject;
import com.google.common.eventbus.EventBus;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;

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

  @Inject
  private EventBus eventBus;

  @Inject
  private NodeTableController.Factory nodeTableControllerFactory;

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
    eventBus.post(new ChangeMainViewEvent(nodeTableControllerFactory.create()));
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    AdminController create();
  }
}
