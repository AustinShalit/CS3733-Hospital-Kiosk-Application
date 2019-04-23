package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.component.FuzzyWuzzyComboBox;
import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.entity.PatientInfoRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "PatientInfoPopup.fxml")
public class PatientInfoPopupController implements Controller {

	private static final Logger logger = Logger.getLogger(PatientInfoPopupController.class.getName());

	@FXML
	BorderPane root;
	@FXML
	private JFXTextField nametxt;
	@FXML
	private JFXDatePicker patientDOB;
	@FXML
	private JFXComboBox<PatientInfoRequest.PatientInfoSex> sexbox;
	@FXML
	private FuzzyWuzzyComboBox locationComboBox;
	@FXML
	private JFXTextArea descriptiontxt;
	@FXML
	private JFXButton submitbtn;

	@Inject
	private Database db;

	@FXML
	void initialize() {
		sexbox.getItems().setAll(PatientInfoRequest.PatientInfoSex.values());
		locationComboBox.setNodes(db.getAllNodes());
		locationComboBox.setupAutoRefresh();
		locationComboBox.refresh();
		patientDOB.setEditable(true);
		submitbtn.disableProperty().bind(
				Bindings.isEmpty(nametxt.textProperty())
						.or(Bindings.isNull(patientDOB.valueProperty()))
						.or(Bindings.isNull(locationComboBox.valueProperty()))
						.or(Bindings.isNull(sexbox.valueProperty()))
		);
	}

	@FXML
	void onSubmitButtonAction() {
		PatientInfoRequest req = parse();
		if (Objects.nonNull(req)) {
			db.insertPatientInfoRequest(req);
			DialogHelper.showInformationAlert("Success!",
					"Inserted Patient Info into Database!");
		} else {
			logger.log(Level.WARNING,
					"Unable to parse patient info Request.",
					"Unable to parse Patient Info Request.");
			DialogHelper.showErrorAlert("Error.",
					"Please make sure all fields are filled out.");
		}

		nametxt.setText(null);
		patientDOB.setValue(null);
		sexbox.getSelectionModel().clearSelection();
		sexbox.setValue(null);
		locationComboBox.getSelectionModel().clearSelection();
		locationComboBox.setValue(null);
		descriptiontxt.setText("");

	}

	/**
	 * Parse input the user has inputted for the sanitation request.
	 *
	 * @return If valid input, A PatientInfoRequest representing the users input. Otherwise null.
	 */
	private PatientInfoRequest parse() {
		// validation
		if (nametxt.getText().isEmpty() // check that the name field is filled out
				|| Objects.isNull(patientDOB.getValue()) // check if patientDOB is filled out
				|| Objects.isNull(locationComboBox.getNodeValue()) // check location field is filled out
				|| Objects.isNull(sexbox.getValue())) { // check if patient sex is selected
			return null;
		}

		if (patientDOB.getValue().isAfter(LocalDate.now())) {
			DialogHelper.showErrorAlert("Error.",
					"Please enter valid date.");
			return null;
		}

		return new PatientInfoRequest(LocalDateTime.now(),
				locationComboBox.getNodeValue(),
				descriptiontxt.getText(),
				nametxt.getText(),
				patientDOB.getValue(),
				sexbox.getValue());
	}

	@Override
	public Parent getRoot() {
		return root;
	}

	public interface Factory {
		PatientInfoPopupController create();
	}

}
