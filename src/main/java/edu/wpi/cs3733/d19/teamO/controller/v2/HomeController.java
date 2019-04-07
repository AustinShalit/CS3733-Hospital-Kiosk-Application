package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import com.google.common.eventbus.EventBus;
import com.jfoenix.controls.JFXButton;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class HomeController implements ContentPane {

  @FXML
  private JFXButton navigationButton;
  @FXML
  private JFXButton requestButton;
  @FXML
  private JFXButton scheduleButton;
  @FXML
  private JFXButton securityButton;
  @FXML
  private Label timeLabel;
  @FXML
  private Label weatherLabel;

  private Optional<EventBus> eventBus = Optional.empty();

  @FXML
  void initialize()throws IOException {
    Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
      int second = LocalDateTime.now().getSecond();
      int minute = LocalDateTime.now().getMinute();
      int hour = LocalDateTime.now().getHour();
      timeLabel.setText(hour + ":" + (minute) + ":" + second);
    }),
        new KeyFrame(Duration.seconds(1))
    );
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();
  }

  @FXML
  void navigationOnAction(){
    eventBus.ifPresent(bus -> bus.post("../NavigationWindow.fxml"));
  }

  @FXML
  void requestOnAction(){
    eventBus.ifPresent(bus -> bus.post("../NavigationWindow.fxml"));
  }

  @FXML
  void scheduleOnAction(){
    eventBus.ifPresent(bus -> bus.post("../SchedulingWindow.fxml"));
  }

  @FXML
  void securityOnAction(){
    eventBus.ifPresent(bus -> bus.post("../SecurityWindow.fxml"));
  }

  @Override
  public void setEventBus(EventBus eventBus) {
    this.eventBus = Optional.of(eventBus);
  }
}