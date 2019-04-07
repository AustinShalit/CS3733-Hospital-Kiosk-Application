package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.IOException;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXToolbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;

@FxmlController(url = "Home.fxml")
public class MainController {

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

  private JFXPopup optionsPopup;

  private Injector injector;

  @FXML
  void initialize() throws IOException {
    injector = Guice.createInjector(
        (Module) binder -> binder.bind(EventBus.class).asEagerSingleton());
    injector.injectMembers(this);
    eventBus.register(this);

    eventBus.post(new ChangeMainViewEvent(
        HomeController.class.getResource(HomeController.class.getAnnotation(FxmlController.class).url())));

    FXMLLoader loader = getFxmlLoader();
    loader.setLocation(OptionsPopupController.class.getResource("OptionsPopup.fxml"));
    optionsPopup = new JFXPopup(loader.load());

    optionsBurger.setOnMouseClicked(e ->
        optionsPopup.show(optionsBurger,
            JFXPopup.PopupVPosition.TOP,
            JFXPopup.PopupHPosition.RIGHT,
            -12,
            15));
  }

  @FXML
  void onHomeButtonAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(
        HomeController.class.getResource(HomeController.class.getAnnotation(FxmlController.class).url())));
  }
  
  @Subscribe
  private void accept(ChangeMainViewEvent event) {
    FXMLLoader loader = getFxmlLoader();
    loader.setLocation(event.getFxml());
    try {
      Node view = loader.load();
      contentPane.setCenter(view);
      toolbar.setVisible(event.isFramed());
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private FXMLLoader getFxmlLoader() {
    FXMLLoader loader = new FXMLLoader();
    loader.setControllerFactory(injector::getInstance);
    return loader;
  }
}
