package edu.wpi.cs3733.d19.teamO.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXToolbar;

import animatefx.animation.RotateIn;
import animatefx.animation.RotateOut;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import edu.wpi.cs3733.d19.teamO.AppPreferences;
import edu.wpi.cs3733.d19.teamO.LiveWeather;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "Main.fxml")
@SuppressWarnings({"PMD.TooManyFields", "PMD.ExcessiveImports"})
public class MainController implements Controller {

	private static final DateTimeFormatter DATE_TIME_FORMATTER
			= DateTimeFormatter.ofPattern("HH:mm:ss\nMM/dd/yyyy");

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
	private NavigationController.Factory navigationControllerFactory;
	@Inject
	private SchedulingController.Factory schedulingControllerFactory;
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
	@Inject
	AppPreferences appPreferences;

	private final LiveWeather liveWeather = new LiveWeather();

	private LoginController loginController;
	private JFXPopup optionsPopup;
	private JFXPopup loginPopup;
	private JFXPopup settingsPopup;
	private Timeline idleTimeline;
	// private static IdleLogoutCareTaker careTaker;
	// private static IdleLogoutOriginator originator;

	@FXML
	void initialize() {
		eventBus.register(this);

		SettingsController settingsController = settingsControllerFactory.create();
		settingsPopup = new JFXPopup(settingsController.root);
		settingsPopup.setOnHiding(
				event -> new RotateIn(optionsBurger).play()
		);

		optionsPopup = new JFXPopup(optionsControllerFactory.create().list);
		optionsPopup.setOnHiding(
				event -> new RotateIn(optionsBurger).play()
		);
		optionsBurger.disableProperty().bind(optionsPopup.showingProperty()
				.or(settingsPopup.showingProperty()));
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

		liveWeather.start();
		liveWeather.imageProperty().addListener((observable, oldValue, newValue)
				-> tempImage.setImage(newValue));
		weatherLabel.textProperty().bind(Bindings.createStringBinding(()
						-> String.format("%.1f°F to %.1f°F", liveWeather.getMinTemp(), liveWeather.getMaxTemp()),
				liveWeather.minTempProperty(), liveWeather.maxTempProperty()));
		description.textProperty().bind(liveWeather.descriptionProperty());
		description.setStyle("-fx-font-size: 15px; -fx-font-style: bold");

		final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(250), event
				-> timeLabel.setText(LocalDateTime.now().format(DATE_TIME_FORMATTER))));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

		// Initialize idleTimeline value
		//
		idleTimeline = new Timeline(new KeyFrame(
				Duration.millis(appPreferences.getAutoLogoutTime() * 1000),
				ae -> loginButtonAction()));

		// Unused Memento stuff
        /*careTaker = new IdleLogoutCareTaker();
        originator = new IdleLogoutOriginator(idleTimeline, careTaker);*/

		// Add event handlers to reset timeline on activity on any window
		//
		root.addEventHandler(MouseEvent.MOUSE_MOVED, ae -> resetTimeline());
		root.addEventHandler(MouseEvent.MOUSE_CLICKED, ae -> resetTimeline());
		root.addEventHandler(KeyEvent.KEY_PRESSED, ae -> resetTimeline());
		menu.addEventHandler(MouseEvent.MOUSE_MOVED, ae -> resetTimeline());
		menu.addEventHandler(MouseEvent.MOUSE_CLICKED, ae -> resetTimeline());
		menu.addEventHandler(KeyEvent.KEY_PRESSED, ae -> resetTimeline());
		optionsBurger.addEventHandler(MouseEvent.MOUSE_MOVED, ae -> resetTimeline());
		optionsBurger.addEventHandler(MouseEvent.MOUSE_CLICKED, ae -> resetTimeline());
		optionsBurger.addEventHandler(KeyEvent.KEY_PRESSED, ae -> resetTimeline());
		toolbar.addEventHandler(MouseEvent.MOUSE_MOVED, ae -> resetTimeline());
		toolbar.addEventHandler(MouseEvent.MOUSE_CLICKED, ae -> resetTimeline());
		toolbar.addEventHandler(KeyEvent.KEY_PRESSED, ae -> resetTimeline());


	}

	/**
	 * Reset the idleTimer if the user is logged in.
	 */
	void resetTimeline() {
		idleTimeline.pause();
		updateDuration();
		if ("Logout".equals(loginbtn.getText())) {
			idleTimeline.playFromStart();
		}
	}

	/**
	 * Update the duration of the idleTimer to the current autoLogoutTime.
	 */
	private void updateDuration() {
		Duration temp = new Duration(appPreferences.getAutoLogoutTime() * 1000);
		idleTimeline.setDelay(temp);
	}

	@FXML
	void navigationButtonAction(ActionEvent event) {
		eventBus.post(new ChangeMainViewEvent(navigationControllerFactory.create()));
	}

	@FXML
	void schedulingButtonAction(ActionEvent event) {
		eventBus.post(new ChangeMainViewEvent(schedulingControllerFactory.create()));
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
			eventBus.post(new ChangeMainViewEvent(navigationControllerFactory.create(), false));
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
