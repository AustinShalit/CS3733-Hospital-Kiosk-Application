package edu.wpi.cs3733.d19.teamO.controller;

import com.dlsc.preferencesfx.PreferencesFx;
import com.google.inject.Inject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.AppPreferences;

@FxmlController(url = "Settings.fxml")
public final class SettingsController implements Controller {

  @FXML
  private BorderPane root;
  @Inject
  private AppPreferences appPreferences;

  private PreferencesFx preferencesFx;

  @FXML
  void initialize() {
    preferencesFx = appPreferences.createPreferences();

    preferencesFx.show();
    //root.setCenter(preferencesFx.getView());
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  @FXML
  void save(ActionEvent event) {
    preferencesFx.saveSettings();
  }

  public interface Factory {
    SettingsController create();
  }
}
