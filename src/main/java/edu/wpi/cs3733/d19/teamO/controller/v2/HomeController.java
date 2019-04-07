package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;

import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "Home.fxml")
public class HomeController extends Controller {

  @FXML
  private JFXButton navigationButton;
  @FXML
  private JFXButton requestButton;
  @FXML
  private JFXButton scheduleButton;
  @FXML
  private JFXButton securityButton;

  @Inject
  private ContentSwitcher contentSwitcher;

  Database database;

  @FXML
  void navigationOnAction(){

  }

  @FXML
  void requestOnAction(){
    contentSwitcher.accept(RequestController.class,
        RequestController.class.getDeclaredAnnotation(FxmlController.class).url());
  }

  @FXML
  void scheduleOnAction(){

  }

  @FXML
  void securityOnAction(){
    if(securityDialog("Security", "Security will be called. Are you sure?", null)) {
//      Node node = new Node();
//      database.insertSecurityRequest(new SecurityRequest(LocalDateTime.now(), node));
    }
  }
}
