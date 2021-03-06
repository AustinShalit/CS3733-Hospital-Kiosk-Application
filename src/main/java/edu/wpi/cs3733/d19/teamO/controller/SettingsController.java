package edu.wpi.cs3733.d19.teamO.controller;

import com.google.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.AppPreferences;
import edu.wpi.cs3733.d19.teamO.component.ExtendedPropertySheet;

@FxmlController(url = "Settings.fxml")
public final class SettingsController implements Controller {

  @FXML
  BorderPane root;
  @Inject
  private AppPreferences appPreferences;

  @FXML
  void initialize() {
    root.setCenter(new ExtendedPropertySheet(appPreferences.getSettings()));
    root.getCenter().setStyle(
        "-fx-background-color: #f1f1f1; -fx-pref-height: 150px; -fx-pref-width: 500px; ");
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    SettingsController create();
  }
}
