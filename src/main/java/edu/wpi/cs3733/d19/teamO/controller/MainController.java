package edu.wpi.cs3733.d19.teamO.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
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

@FxmlController(url = "Main.fxml")
public class MainController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private StackPane optionsBurger;
  @FXML
  private VBox menu;
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
  void loginButtonAction() {
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

  @Subscribe
  @SuppressWarnings("PMD.UnusedPrivateMethod")
  private void acceptController(ChangeMainViewEvent event) {
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
