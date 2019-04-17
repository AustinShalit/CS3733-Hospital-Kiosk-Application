package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

import edu.wpi.cs3733.d19.teamO.component.MapView;

import static java.nio.charset.StandardCharsets.UTF_8;

@FxmlController(url = "GuestWindow.fxml")
@SuppressWarnings({"PMD.TooManyFields", "PMD.RedundantFieldInitializer", "PMD.ExcessiveImports",
    "PMD.UseLocaleWithCaseConversions"})

public class GuestViewController implements Controller {

  @FXML
  BorderPane root;
  @FXML
  private JFXButton loginbtn;
  @FXML
  private Label timeLabel;
  @FXML
  private Label weatherLabel;
  @FXML
  private Label description;
  @FXML
  private ImageView tempImage;
  @FXML
  MapView map;

  @Inject
  private LoginController.Factory loginFactory;

  private JFXPopup login;
  private String second;
  private String minute;
  private String hour;
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
    JsonElement jsonElement = jp.parse(new InputStreamReader(
        (InputStream) request.getContent(), UTF_8));
    JsonObject rootobj = jsonElement.getAsJsonObject();
    System.out.println(jsonElement.toString());
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
    description.setText(discrp.toUpperCase());

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
          + " \n" + month + "/" + day + "/" + year);
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

    LoginController loginController = loginFactory.create();
    loginController.getLoginFail().textProperty().addListener(
        (observable, oldValue, newValue) -> {
          if ("Login Success".equals(newValue)) {
            login.hide();
          }
        }
    );
    login = new JFXPopup(loginController.root);
    login.setOnAutoHide(
        event -> {
          ColorAdjust reset = new ColorAdjust();
          reset.setBrightness(0);
          root.setEffect(reset);
        }
    );
  }

  @FXML
  void onLoginAction() {
    ColorAdjust colorAdjust = new ColorAdjust();
    colorAdjust.setBrightness(-0.2);
    root.setEffect(colorAdjust);
    login.show(root);
    login.setX(
        (root.getScene().getWindow().getWidth() - login.getWidth()) / 2
    );
    login.setY(
        (root.getScene().getWindow().getHeight() - login.getHeight()) / 2
    );
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

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    GuestViewController create();
  }
  /*
  @FXML
  @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops", "UseStringBufferForStringAppends"})
  void onGoButtonAction() throws IOException {

    if (toComboBox.getValue().equals(fromComboBox.getValue())) {
      DialogHelper.showInformationAlert("Must Select Different Start/End Destinations",
          "Please select different start and end destinations to generate a valid path.");
      return;
    }

    IGraphSearchAlgorithm<Node> algorithm = appPreferences.getGraphSearchAlgorithm().getAlgorithm();
    MutableGraph<Node> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    database.getAllNodes().forEach(graph::addNode);
    database.getAllEdges().forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

    List<Node> path = algorithm.getPath(ImmutableGraph.copyOf(graph),
        fromComboBox.getValue(),
        toComboBox.getValue());

    ArrayList<String> list = stepByStep.getStepByStep(path);
    String instruction;
    StringBuilder stringBuilder = new StringBuilder();
    for (String s: list) {
      stringBuilder.append(s);
      stringBuilder.append('\n');
    }
    instruction = stringBuilder.toString();
    instructions.setText(instruction);
    map.zoomTo(fromComboBox.getValue());
    map.setPath(path);
    map.drawPath();

  }
*/

}
