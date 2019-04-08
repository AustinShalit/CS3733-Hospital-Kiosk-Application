package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;

import com.google.common.eventbus.EventBus;
import com.jfoenix.controls.JFXButton;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.model.CurrentWeather;

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
  @FXML
  private WebView weather;

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

    try{
      getWeatherData();
    }catch(APIException e){
      e.printStackTrace();
    }
  }

  void getWeatherData() throws APIException{
    OWM owm = new OWM("c2711050ed24651e99a523ce6d08ad73");

    CurrentWeather cwd = owm.currentWeatherByCityName("Worcester", OWM.Country.UNITED_STATES);

    DecimalFormat df = new DecimalFormat("#.##");

    double max = (cwd.getMainData().getTempMax() - 273.15) * 9.0 / 5.0 + 32.0;
    double min = (cwd.getMainData().getTempMin() - 273.15) * 9.0 / 5.0 + 32.0;
//    double cloud = cwd.getCloudData().getCloud();
//    double wind = cwd.getWindData().getSpeed() * 3.6f;
//    double rain = cwd.getRainData().getPrecipVol3h();
    weatherLabel.setText("The weather at Worcester is from " + df.format(min) + " Fahrenheit to " + df.format(max) + " Fahrenheit" );
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