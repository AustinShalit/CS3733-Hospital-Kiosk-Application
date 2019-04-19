package edu.wpi.cs3733.d19.teamO.controller;

import java.time.LocalDateTime;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXToolbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "Main.fxml")
@SuppressWarnings("PMD.TooManyFields")
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

  @Inject
  private EventBus eventBus;
  @Inject
  private NavigationController.Factory navigationControllerFactory;
  @Inject
  private SchedulingController.Factory schedulingControllerFactory;
  @Inject
  private RequestController.Factory requestControllerFactory;
  @Inject
  private OptionsPopupController.Factory optionsPopupControllerFactory;
  @Inject
  private LoginController.Factory loginControllerFactory;
  @Inject
  private Database database;

  private JFXPopup loginPopup;
  private JFXPopup optionsPopup;

  @FXML
  void initialize() {
    eventBus.register(this);

    optionsPopup = new JFXPopup(optionsPopupControllerFactory.create().list);
    optionsBurger.setOnMouseClicked(e ->
        optionsPopup.show(optionsBurger,
            JFXPopup.PopupVPosition.TOP,
            JFXPopup.PopupHPosition.RIGHT,
            -12,
            15));

    LoginController loginController = loginControllerFactory.create();
    loginController.getLoginFail().textProperty().addListener(
        (observable, oldValue, newValue) -> {
          if ("Login Success".equals(newValue)) {
            loginPopup.hide();
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
          navigationbtn.setMinHeight(newValue.doubleValue() / 4);
          requestbtn.setMinHeight(newValue.doubleValue() / 4);
          schedulingbtn.setMinHeight(newValue.doubleValue() / 4);
          securitybtn.setMinHeight(newValue.doubleValue() / 4);
          navigationbtn.setMaxHeight(newValue.doubleValue() / 4);
          requestbtn.setMaxHeight(newValue.doubleValue() / 4);
          schedulingbtn.setMaxHeight(newValue.doubleValue() / 4);
          securitybtn.setMaxHeight(newValue.doubleValue() / 4);
        }
    );
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
  }

  @Override
  public Parent getRoot() {
    return root;
  }
}
