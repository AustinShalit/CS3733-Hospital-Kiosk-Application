package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;

@FxmlController(url = "Home.fxml")
public class HomeController {

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

  }
}
