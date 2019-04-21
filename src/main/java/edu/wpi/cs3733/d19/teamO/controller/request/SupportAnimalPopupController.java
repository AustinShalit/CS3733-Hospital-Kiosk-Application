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
import edu.wpi.cs3733.d19.teamO.entity.SupportAnimalRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@FxmlController(url = "SupportAnimalPopup.fxml")
public class SupportAnimalPopupController implements Controller {

    private static final Logger logger =
            Logger.getLogger(SupportAnimalPopupController.class.getName());

    @FXML
    BorderPane root;
    @FXML
    private JFXTextField nametxt;
    @FXML
    private JFXComboBox<Node> locationbox;
    @FXML
    private JFXComboBox<SupportAnimalRequest.SupportAnimalRequestType> categorybox;
    @FXML
    private JFXTextArea descriptiontxt;
    @FXML
    private JFXButton submitbtn;

    @Inject
    private Database db;

    @FXML
    void initialize() {
        DialogHelper.populateComboBox(db, locationbox);
        categorybox.getItems().setAll(SupportAnimalRequest.SupportAnimalRequestType.values());

        submitbtn.disableProperty().bind(
                Bindings.isEmpty(nametxt.textProperty())
                        .or(Bindings.isNull(locationbox.valueProperty()))
                        .or(Bindings.isNull(categorybox.valueProperty()))
        );
    }

    @FXML
    void onSubmitButtonAction() {
        SupportAnimalRequest request = parseRequest();
        if (request == null) {
            logger.log(Level.WARNING,
                    "Unable to parse support animal Request.",
                    "Unable to parse support animal Request.");
            return;
        }

        nametxt.setText(null);
        locationbox.getSelectionModel().clearSelection();
        locationbox.setValue(null);
        categorybox.getSelectionModel().clearSelection();
        categorybox.setValue(null);
        descriptiontxt.setText(null);

        if (db.insertSupportAnimalRequest(request)) {
            String message = "Successfully submitted support animal request.";
            DialogHelper.showInformationAlert("Success!", message);
        } else {
            DialogHelper.showErrorAlert("Error.",
                    "Unable to submit support animal request.");
        }
    }

    /**
     * Parse input the user has inputted for the request.
     *
     * @return If valid input, A SupportAnimalRequest representing the users input. Otherwise null.
     */
    private SupportAnimalRequest parseRequest() {
        // if input is valid, parse it and return a new Request
        if (!nametxt.getText().isEmpty()
                && Objects.nonNull(locationbox.getValue())
                && Objects.nonNull(categorybox.getValue())) {

            Node node = locationbox.getValue();
            String description = descriptiontxt.getText();
            String name = nametxt.getText();

            return new SupportAnimalRequest(LocalDateTime.now(),
                    node,
                    categorybox.getValue(),
                    description,
                    name);
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
        SupportAnimalPopupController create();
    }
}
