package edu.wpi.cs3733.d19.teamO.controller;

import com.google.inject.Inject;
import edu.wpi.cs3733.d19.teamO.AppPreferences;
import edu.wpi.cs3733.d19.teamO.component.ExtendedPropertySheet;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

@FxmlController(url = "Settings.fxml")
public final class SettingsController implements Controller {

    @FXML
    private BorderPane root;

    @Inject
    private AppPreferences appPreferences;


    @FXML
    void initialize() {
        root.setCenter(new ExtendedPropertySheet(appPreferences.getSettings()));
    }

    @Override
    public Parent getRoot() {
        return root;
    }

    public interface Factory {
        SettingsController create();
    }

    public AppPreferences getAppPreferences() {
        return appPreferences;
    }
}
