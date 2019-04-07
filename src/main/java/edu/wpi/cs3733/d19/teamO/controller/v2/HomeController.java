package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.util.Optional;

import com.google.common.eventbus.EventBus;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;

public class HomeController implements ContentPane {

  @FXML
  private JFXButton navigationButton;
  @FXML
  private JFXButton requestButton;
  @FXML
  private JFXButton scheduleButton;
  @FXML
  private JFXButton securityButton;

  private Optional<EventBus> eventBus = Optional.empty();

  @FXML
  void navigationOnAction(){
    eventBus.ifPresent(bus -> bus.post("../NavigationWindow.fxml"));
  }

  @FXML
  void requestOnAction(){

  }

  @FXML
  void scheduleOnAction(){

  }

  @FXML
  void securityOnAction(){

  }

  @Override
  public void setEventBus(EventBus eventBus) {
    this.eventBus = Optional.of(eventBus);
  }
}
