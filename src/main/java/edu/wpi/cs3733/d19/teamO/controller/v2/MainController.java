package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.IOException;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXPopup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MainController {

  @FXML
  private StackPane root;

  @FXML
  private BorderPane contentPane;

  @FXML
  private StackPane optionsBurger;

  private final EventBus contentEventBus = new EventBus("Main Content EventBus");

  private JFXPopup optionsPopup;

  public MainController() {
    contentEventBus.register(this);
  }

  @FXML
  void initialize() throws IOException {
    Node view = FXMLLoader.load(MainController.class.getResource("Home.fxml"));
    contentPane.setCenter(view);

    FXMLLoader loader = new FXMLLoader(MainController.class.getResource("OptionsPopup.fxml"));
    optionsPopup = new JFXPopup(loader.load());

    optionsBurger.setOnMouseClicked(e ->
        optionsPopup.show(optionsBurger,
            JFXPopup.PopupVPosition.TOP,
            JFXPopup.PopupHPosition.RIGHT,
            -12,
            15));
  }

  @Subscribe
  public void onContentChangeRequest(String r) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(MainController.class.getResource(r));
    ContentPane c = loader.getController();
    c.setEventBus(contentEventBus);
    Node view = loader.load();
    contentPane.setCenter(view);
  }
}
