package edu.wpi.cs3733.d19.teamO.controller;

import com.dlsc.preferencesfx.PreferencesFx;
import com.google.inject.Inject;

import javafx.scene.Parent;

import edu.wpi.cs3733.d19.teamO.AppPreferences;

public final class SettingsController implements Controller {

  private Parent root;

  @Inject
  private AppPreferences appPreferences;

  public SettingsController() {
    PreferencesFx preferencesFx
        = PreferencesFx.of(AppPreferences.class, appPreferences.getSettings());

    root = preferencesFx.getView();
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    SettingsController create();
  }
}
