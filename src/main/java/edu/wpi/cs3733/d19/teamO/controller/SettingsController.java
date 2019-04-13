package edu.wpi.cs3733.d19.teamO.controller;

import com.dlsc.preferencesfx.PreferencesFx;
import com.google.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import edu.wpi.cs3733.d19.teamO.AppPreferences;

@FxmlController(url = "Settings.fxml")
public final class SettingsController implements Controller {

  @FXML
  private StackPane root;

  @Inject
  private AppPreferences appPreferences;

  @FXML
  void initialize() {
    PreferencesFx preferencesFx
        = PreferencesFx.of(AppPreferences.class, appPreferences.getSettings());

    root.getChildren().add(preferencesFx.getView());
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    SettingsController create();
  }
}
