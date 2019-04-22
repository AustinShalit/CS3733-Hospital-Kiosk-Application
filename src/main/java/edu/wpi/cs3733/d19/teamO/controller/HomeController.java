package edu.wpi.cs3733.d19.teamO.controller;

import com.google.common.eventbus.EventBus;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d19.teamO.AppPreferences;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javafx.scene.input.MouseEvent.MOUSE_MOVED;

@SuppressWarnings({"PMD.TooManyFields", "PMD.ExcessiveImports", "PMD.UseLocaleWithCaseConversions"})

@FxmlController(url = "Home.fxml")
public class HomeController implements Controller {

    @FXML
    private BorderPane root;
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
    private Database database;
    @Inject
    private RequestController.Factory requestControllerFactory;
    @Inject
    private SchedulingController.Factory schedulingControllerFactory;
    @Inject
    private NavigationController.Factory navigationControllerFactory;
    @Inject
    private SettingsController.Factory settingsControllerFactory;


    private String second;
    private String minute;
    private String hour;
    private int month;
    private int day;
    private int year;
    private String image;

    private Timer timer;
    private TimerTask task;
    private ControllerCareTaker careTaker = new ControllerCareTaker();
    private IdleTimer idle;

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
        image = icon + ".png";

        description.setStyle("-fx-font-size: 15px; -fx-font-style: bold");
        description.setText(discrp.toUpperCase());

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            second = (LocalDateTime.now().getSecond() < 10 ? "0" : "")
                    + LocalDateTime.now().getSecond();
            minute = (LocalDateTime.now().getMinute() < 10 ? "0" : "")
                    + LocalDateTime.now().getMinute();
            hour = (LocalDateTime.now().getHour() < 10 ? "0" : "")
                    + LocalDateTime.now().getHour();
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

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Controller guest = careTaker.getMemento("INITIAL").getState();
                eventBus.post(guest);
            }
        };
        timer.schedule(task,Math.round(settingsControllerFactory.create().getAppPreferences().getAutoLogoutTime()));

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
            Node node = new Node("notExist", 0, 0, "0", "0",
                    Node.NodeType.WORKZONE, "not", "existed");
            SecurityRequest sr = new SecurityRequest(LocalDateTime.now(), node);
            database.insertSecurityRequest(sr);
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
