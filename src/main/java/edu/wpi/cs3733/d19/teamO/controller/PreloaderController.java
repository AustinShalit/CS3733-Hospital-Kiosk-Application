package edu.wpi.cs3733.d19.teamO.controller;

import edu.wpi.cs3733.d19.teamO.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.controlsfx.tools.Utils;

public class PreloaderController {

    @FXML
    private Label versionLabel;
    @FXML
    private Label stateLabel;
    @FXML
    private ProgressBar progressBar;

    @FXML
    void initialize() {
        progressBar.setProgress(-1);
        versionLabel.setText(Project.getVersion());
    }

    public void setStateText(String text) {
        stateLabel.setText(text);
    }

    public void setProgress(double progress) {
        progressBar.setProgress(Utils.clamp(0, progress, 1));
    }
}
