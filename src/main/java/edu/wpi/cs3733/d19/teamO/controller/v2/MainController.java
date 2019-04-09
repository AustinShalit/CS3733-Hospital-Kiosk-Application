package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXToolbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;

@FxmlController(url = "Main.fxml")
public class MainController implements Controller {

  @FXML
  private StackPane root;

  @FXML
  private BorderPane contentPane;

  @FXML
  private StackPane optionsBurger;

  @FXML
  private JFXToolbar toolbar;

  @Inject
  private EventBus eventBus;
  @Inject
  private HomeController.Factory homeControllerFactory;
  @Inject
  private OptionsPopupController.Factory optionsPopupControllerFactory;

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
  }

  @FXML
  void onHomeButtonAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(homeControllerFactory.create()));
  }

  @Subscribe
  @SuppressWarnings("PMD.UnusedPrivateMethod")
  private void acceptController(ChangeMainViewEvent event) {
    contentPane.setCenter(event.getController().getRoot());
    if (event.isFramed()) {
      contentPane.setTop(toolbar);
    } else {
      contentPane.setTop(null);
    }
    // toolbar.setVisible(event.isFramed());
    optionsPopup.hide();
  }

  @Override
  public Parent getRoot() {
    return root;
  }
}
