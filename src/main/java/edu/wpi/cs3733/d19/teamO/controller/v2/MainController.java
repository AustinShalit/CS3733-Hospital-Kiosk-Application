package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.IOException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.jfoenix.controls.JFXPopup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

@FxmlController(url = "Home.fxml")
public class MainController implements ContentSwitcher {

  @FXML
  private StackPane root;

  @FXML
  private BorderPane contentPane;

  @FXML
  private StackPane optionsBurger;

  private JFXPopup optionsPopup;

  private Injector injector;

  @FXML
  void initialize() throws IOException {
    injector = Guice.createInjector(
        (Module) binder -> binder.bind(ContentSwitcher.class).toInstance(this));
    accept(HomeController.class, HomeController.class.getAnnotation(FxmlController.class).url());

    FXMLLoader loader = new FXMLLoader(MainController.class.getResource("OptionsPopup.fxml"));
    optionsPopup = new JFXPopup(loader.load());

    optionsBurger.setOnMouseClicked(e ->
        optionsPopup.show(optionsBurger,
            JFXPopup.PopupVPosition.TOP,
            JFXPopup.PopupHPosition.RIGHT,
            -12,
            15));
  }
  
  @Override
  public void accept(Class clazz, String fxml) {
    FXMLLoader loader = getFxmlLoader();
    loader.setLocation(clazz.getResource(fxml));
    try {
      Node view = loader.load();
      contentPane.setCenter(view);
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
