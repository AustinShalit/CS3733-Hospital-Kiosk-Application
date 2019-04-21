package edu.wpi.cs3733.d19.teamO.controller.request;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@FxmlController(url = "SanitationPopup.fxml")
public class SanitationPopupController implements Controller {

    private static final Logger logger = Logger.getLogger(
            SanitationPopupController.class.getName());

    @FXML
    BorderPane root;
    @FXML
    private JFXTextField nametxt;
    @FXML
    private JFXComboBox<Node> locationbox;
    @FXML
    private JFXComboBox<SanitationRequest.SanitationRequestType> categorybox;
    @FXML
    private JFXTextArea descriptiontxt;
    @FXML
    private JFXButton submitbtn;

    @Inject
    private Database db;

    @FXML
    void initialize() {
        DialogHelper.populateComboBox(db, locationbox);
        categorybox.getItems().setAll(SanitationRequest.SanitationRequestType.values());

        submitbtn.disableProperty().bind(
                Bindings.isEmpty(nametxt.textProperty())
                        .or(Bindings.isNull(locationbox.valueProperty()))
                        .or(Bindings.isNull(categorybox.valueProperty()))
        );
    }

    @FXML
    void onSubmitButtonAction() {
        SanitationRequest sanitation = parseSanitationRequest();
        if (sanitation == null) {
            logger.log(Level.WARNING,
                    "Unable to parse Sanitation Request.",
                    "Unable to parse Sanitation Request.");
            return;
        }

        nametxt.setText(null);
        locationbox.getSelectionModel().clearSelection();
        locationbox.setValue(null);
        categorybox.getSelectionModel().clearSelection();
        categorybox.setValue(null);
        descriptiontxt.setText(null);

        if (db.insertSanitationRequest(sanitation)) {
            String message = "Successfully submitted Sanitation request.";
            DialogHelper.showInformationAlert("Success!", message);
        } else {
            DialogHelper.showErrorAlert("Error.",
                    "Unable to submit Sanitation request.");
        }
    }

    /**
     * Parse input the user has inputted for the sanitation request.
     *
     * @return If valid input, A SanitationRequest representing the users input
     * Otherwise null.
     */
    private SanitationRequest parseSanitationRequest() {
        // if input is valid, parse it and return a new SanitationRequest
        if (!nametxt.getText().isEmpty()
                && Objects.nonNull(locationbox.getValue())
                && Objects.nonNull(categorybox.getValue())) {

            LocalDateTime now = LocalDateTime.now();
            Node node = locationbox.getValue();

            String type = categorybox.getValue().toString().toUpperCase(new Locale("EN"));
            SanitationRequest.SanitationRequestType sanitationRequestType =
                    SanitationRequest.SanitationRequestType.valueOf(type);

            String description = descriptiontxt.getText();
            String name = nametxt.getText();

            return new SanitationRequest(now, node, sanitationRequestType, description, name);
        }

        // otherwise, some input was invalid
        DialogHelper.showErrorAlert("Error.", "Please make sure all fields are filled out.");
        return null;
    }

    @Override
    public Parent getRoot() {
        return root;
    }

    public interface Factory {
        SanitationPopupController create();
    }
}
