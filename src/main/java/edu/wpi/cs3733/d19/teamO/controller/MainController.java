package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Locale;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXToolbar;

import animatefx.animation.RotateIn;
import animatefx.animation.RotateOut;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

import static java.nio.charset.StandardCharsets.UTF_8;

@FxmlController(url = "Main.fxml")
@SuppressWarnings({"PMD.TooManyFields", "PMD.ExcessiveImports"})
public class MainController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private JFXToolbar toolbar;
  @FXML
  private StackPane optionsBurger;
  @FXML
  private VBox menu;
  @FXML
  private JFXButton loginbtn;
  @FXML
  private JFXButton navigationbtn;
  @FXML
  private JFXButton requestbtn;
  @FXML
  private JFXButton schedulingbtn;
  @FXML
  private JFXButton securitybtn;
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
  private NavigationController navigationController;
  @Inject
  private SchedulingController schedulingController;
  @Inject
  private RequestController.Factory requestControllerFactory;
  @Inject
  private LoginController.Factory loginControllerFactory;
  @Inject
  private OptionsPopupController.Factory optionsControllerFactory;
  @Inject
  private SettingsController.Factory settingsControllerFactory;
  @Inject
  private Database database;

  private String second;
  private String minute;
  private String hour;
  private int month;
  private int day;
  private int year;
  private String image;

  private LoginController loginController;
  private JFXPopup optionsPopup;
  private JFXPopup loginPopup;
  private JFXPopup settingsPopup;

  @FXML
  void initialize() throws IOException {
    eventBus.register(this);

    SettingsController settingsController = settingsControllerFactory.create();
    settingsController.root.getCenter().setStyle(
        "-fx-background-color: #f1f1f1; -fx-pref-height: 150px; -fx-pref-width: 500px; ");
    settingsPopup = new JFXPopup(settingsController.root);
    settingsPopup.setOnAutoHide(
        event -> new RotateIn(optionsBurger).play()
    );

    optionsPopup = new JFXPopup(optionsControllerFactory.create().list);
    optionsPopup.setOnAutoHide(
        event -> new RotateIn(optionsBurger).play()
    );
    optionsBurger.setOnMouseClicked(
        event -> {
          new RotateOut(optionsBurger).play();
          if ("Login Success".equals(loginController.getLoginFail().getText())) {
            optionsPopup.show(optionsBurger,
                JFXPopup.PopupVPosition.TOP,
                JFXPopup.PopupHPosition.RIGHT,
                -12,
                67);
          } else {
            settingsPopup.show(optionsBurger,
                JFXPopup.PopupVPosition.TOP,
                JFXPopup.PopupHPosition.RIGHT,
                -12,
                67);
          }
        }
    );

    loginController = loginControllerFactory.create();
    loginController.getLoginFail().textProperty().addListener(
        (observable, oldValue, newValue) -> {
          if ("Login Success".equals(newValue)) {
            loginPopup.hide();
            ColorAdjust reset = new ColorAdjust();
            reset.setBrightness(0);
            root.setEffect(reset);
            loginbtn.setText("Logout");
          }
        }
    );
    loginPopup = new JFXPopup(loginController.root);
    loginPopup.setOnAutoHide(
        event -> {
          ColorAdjust reset = new ColorAdjust();
          reset.setBrightness(0);
          root.setEffect(reset);
        }
    );

    menu.heightProperty().addListener(
        (observable, oldValue, newValue) -> {
          navigationbtn.setMinHeight(newValue.doubleValue() / menu.getChildren().size());
          requestbtn.setMinHeight(newValue.doubleValue() / menu.getChildren().size());
          schedulingbtn.setMinHeight(newValue.doubleValue() / menu.getChildren().size());
          securitybtn.setMinHeight(newValue.doubleValue() / menu.getChildren().size());

          navigationbtn.setMaxHeight(newValue.doubleValue() / menu.getChildren().size());
          requestbtn.setMaxHeight(newValue.doubleValue() / menu.getChildren().size());
          schedulingbtn.setMaxHeight(newValue.doubleValue() / menu.getChildren().size());
          securitybtn.setMaxHeight(newValue.doubleValue() / menu.getChildren().size());
        }
    );

    weatherDisplay();
  }

  void weatherDisplay() throws IOException {
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
    image = icon + ".png";

    description.setStyle("-fx-font-size: 15px; -fx-font-style: bold");
    description.setText(discrp.toUpperCase(Locale.ENGLISH));

    Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
      second = (LocalDateTime.now().getSecond() < 10 ? "0" : "")
          + Integer.toString(LocalDateTime.now().getSecond());
      minute = (LocalDateTime.now().getMinute() < 10 ? "0" : "")
          + Integer.toString(LocalDateTime.now().getMinute());
      hour = (LocalDateTime.now().getHour() < 10 ? "0" : "")
          + Integer.toString(LocalDateTime.now().getHour());
      month = LocalDateTime.now().getMonthValue();
      day = LocalDateTime.now().getDayOfMonth();
      year = LocalDateTime.now().getYear();
      timeLabel.setText(hour + ":" + minute + ":" + second
          + "\n" + month + "/" + day + "/" + year);
    }),
        new KeyFrame(Duration.seconds(1))
    );
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();

    new Thread(() -> {
      try {
        try {
          getWeatherData();
        } catch (IOException exception) {
          exception.printStackTrace();
        }
      } catch (APIException exception) {
        exception.printStackTrace();
      }
    }).start();
  }

  void getWeatherData() throws APIException, IOException {
    OWM owm = new OWM("c2711050ed24651e99a523ce6d08ad73");

    CurrentWeather cwd = owm.currentWeatherByCityName("Boston", OWM.Country.UNITED_STATES);

    double max = (cwd.getMainData().getTempMax() - 273.15) * 9.0 / 5.0 + 32.0;
    double min = (cwd.getMainData().getTempMin() - 273.15) * 9.0 / 5.0 + 32.0;

    tempImage.setImage(new Image(getClass().getResource("../component/" + image).openStream()));

    DecimalFormat df = new DecimalFormat("##");

    Platform.runLater(()
        -> weatherLabel.setText(df.format(min) + " F to " + df.format(max) + " F"));
  }

  @FXML
  void navigationButtonAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(navigationController));
  }

  @FXML
  void schedulingButtonAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(schedulingController));
  }

  @FXML
  void requestButtonAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
  }

  @FXML
  void securityButtonAction() {
    if (DialogHelper.showConfirmDialog("Confirmation Dialog",
        "Security Request Notification",
        "Are you sure you want to alert security?")) {
      System.out.println("Notifying");
      // TODO send to database
      Node node = new Node("notExist", 0, 0, "0", "0",
          Node.NodeType.WORKZONE, "not", "existed");
      SecurityRequest sr = new SecurityRequest(LocalDateTime.now(), node);
      database.insertSecurityRequest(sr);
    }
  }

  @FXML
  void loginButtonAction() {
    loginController.setLoginFail("");
    if ("Logout".equals(loginbtn.getText())) {
      loginbtn.setText("Login");
      eventBus.post(new ChangeMainViewEvent(navigationController, false));
    } else {
      ColorAdjust colorAdjust = new ColorAdjust();
      colorAdjust.setBrightness(-0.2);
      root.setEffect(colorAdjust);
      loginPopup.show(root);
      loginPopup.setX(
          (root.getScene().getWindow().getWidth() - loginPopup.getWidth()) / 2
      );
      loginPopup.setY(
          (root.getScene().getWindow().getHeight() - loginPopup.getHeight()) / 2
      );
    }
  }

  @Subscribe
  @SuppressWarnings("PMD.UnusedPrivateMethod")
  private void acceptController(ChangeMainViewEvent event) {
    root.setTop(toolbar);
    root.setCenter(event.getController().getRoot());

    if (event.isMenuVisible()) {
      root.setLeft(menu);
    } else {
      root.setLeft(null);
    }

    if (optionsPopup.isShowing()) {
      optionsPopup.hide();
    }
    if (settingsPopup.isShowing()) {
      settingsPopup.hide();
    }
  }

  @Override
  public Parent getRoot() {
    return root;
  }
}
