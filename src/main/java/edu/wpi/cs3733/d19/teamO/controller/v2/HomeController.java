package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

import com.google.common.eventbus.EventBus;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;

import static java.nio.charset.StandardCharsets.UTF_8;

@SuppressWarnings({"PMD.TooManyFields"})

@FxmlController(url = "Home.fxml")
public class HomeController implements Controller {

  @FXML
  private VBox root;
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
  private Label description;
  @FXML
  private ImageView tempImage;
  @Inject
  private EventBus eventBus;
  @Inject
  private RequestController.Factory requestControllerFactory;
  @Inject
  private MapEditController.Factory schedulingControllerFactory;
  @Inject
  private NavigationController.Factory navigationControllerFactory;

  private int second;
  private int minute;
  private int hour;
  private int month;
  private int day;
  private int year;
  private String image;

  @FXML
  void initialize() throws IOException {
    String surl = "http://api.openweathermap.org/data/2.5/weather?q=Boston&APPID=c2711050ed24651e99a523ce6d08ad73";
    URL url = new URL(surl);
    URLConnection request = url.openConnection();
    request.connect();

    // Convert to a JSON object  using GSON
    JsonParser jp = new JsonParser();
    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent(), UTF_8));
    JsonObject rootobj = root.getAsJsonObject();
    System.out.println(root.toString());
    String icon = rootobj.get("weather")
        .getAsJsonArray()
        .get(0)
        .getAsJsonObject()
        .get("icon")
        .getAsString(); //.get("weather.icon").getAsString(); // Get the icon
    String discrp = rootobj.get("weather")
        .getAsJsonArray()
        .get(0)
        .getAsJsonObject()
        .get("description")
        .getAsString(); //.get("weather.icon").getAsString(); // Get the description
    image = "http://openweathermap.org/img/w/" + icon + ".png";

    description.setText(discrp);

    Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
      second = LocalDateTime.now().getSecond();
      minute = LocalDateTime.now().getMinute();
      hour = LocalDateTime.now().getHour();
      month = LocalDateTime.now().getMonthValue();
      day = LocalDateTime.now().getDayOfMonth();
      year = LocalDateTime.now().getYear();
      timeLabel.setText(hour + ":" + minute + ":" + second
          + "  " + month + "/" + day + "/" + year);
    }),
        new KeyFrame(Duration.seconds(1))
    );
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();

    new Thread(() -> {
      try {
        getWeatherData();
      } catch (APIException exception) {
        exception.printStackTrace();
      }
    }).start();
  }

  void getWeatherData() throws APIException {
    OWM owm = new OWM("c2711050ed24651e99a523ce6d08ad73");

    CurrentWeather cwd = owm.currentWeatherByCityName("Boston", OWM.Country.UNITED_STATES);

    double max = (cwd.getMainData().getTempMax() - 273.15) * 9.0 / 5.0 + 32.0;
    double min = (cwd.getMainData().getTempMin() - 273.15) * 9.0 / 5.0 + 32.0;

    Image weather = new Image(image);
    tempImage.setImage(weather);

    DecimalFormat df = new DecimalFormat("#.##");

    Platform.runLater(()
        -> weatherLabel.setText("From " + df.format(min) + " F to " + df.format(max) + " F"));
  }

  @FXML
  void navigationOnAction() {
    eventBus.post(new ChangeMainViewEvent(navigationControllerFactory.create()));
  }

  @FXML
  void requestOnAction() {
    eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
  }

  @FXML
  void scheduleOnAction() {
    eventBus.post(new ChangeMainViewEvent(schedulingControllerFactory.create()));
  }

  @FXML
  void securityOnAction() {
    if (DialogHelper.showConfirmDialog("Confirmation Dialog",
        "Security Request Notification",
        "Are you sure you want to alert security?")) {
      System.out.println("Notifying");
      // TODO send to database
    }
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    HomeController create();
  }
}
