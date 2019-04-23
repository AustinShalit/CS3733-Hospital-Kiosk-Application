package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.entity.PrescriptionRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "PrescriptionRequestPopup.fxml")
public class PrescriptionRequestPopupController implements Controller {

	private static final Logger logger = Logger.getLogger(
			PrescriptionRequestPopupController.class.getName());

	@FXML
	private JFXTextField patientName;
	@FXML
	private JFXTextField medName;
	@FXML
	private JFXDatePicker patientDOB;
	@FXML
	private JFXTextField medDosage;
	@FXML
	private JFXTextArea medDescription;
	@FXML
	private JFXTextField employeeName;
	@FXML
	private JFXButton submitButton;
	@FXML
	BorderPane root;

	@Inject
	private Database db;

	@FXML
	void initialize() {
		submitButton.disableProperty().bind(
				Bindings.isEmpty(patientName.textProperty())
						.or(Bindings.isEmpty(medName.textProperty()))
						.or(Bindings.isNull(patientDOB.valueProperty()))
						.or(Bindings.isEmpty(medDosage.textProperty()))
						.or(Bindings.isEmpty(medDescription.textProperty()))
						.or(Bindings.isEmpty(employeeName.textProperty()))
		);
	}

	/**
	 * Submit button for a new Prescription Request.
	 */
	public void onSubmitButtonAction() {
		PrescriptionRequest request = parseUserPrescriptionRequest();
		if (request == null) {
			logger.log(Level.WARNING,
					"Unable to parse Prescription Request.",
					"Unable to parse Prescription Request.");
			return;
		}

		patientName.setText(null);
		medName.setText(null);
		patientDOB.setValue(null);
		medDosage.setText(null);
		medName.setText(null);
		medDescription.setText(null);

		if (db.insertPrescriptionRequest(request)) {
			String message = "Successfully submitted request.";
			DialogHelper.showInformationAlert("Success!", message);
		} else {
			DialogHelper.showErrorAlert("Error.",
					"Unable to submit Prescription Request request.");
		}
	}

	private PrescriptionRequest parseUserPrescriptionRequest() {
		// if input is valid, parse it and return a new PrescriptionRequest
		if (!employeeName.getText().isEmpty()
				&& !patientName.getText().isEmpty()
				&& Objects.nonNull(patientDOB.getValue())
				&& !medName.getText().isEmpty()
				&& !medDosage.getText().isEmpty()
				&& !medDescription.getText().isEmpty()

		) {

			if (patientDOB.getValue().isAfter(LocalDate.now())) {
				DialogHelper.showErrorAlert("Error.",
						"Please enter a valid date.");
				return null;
			}

			LocalDateTime now = LocalDateTime.now();
			String empName = employeeName.getText();
			String patName = patientName.getText();
			LocalDate patDOB = patientDOB.getValue();
			String medicationName = medName.getText();
			String medicationDosage = medDosage.getText();
			String medicationDirections = medDescription.getText();

			return new PrescriptionRequest(now, empName, patName, patDOB, medicationName,
					medicationDosage, medicationDirections);
		}

		// otherwise, some input was invalid
		DialogHelper.showErrorAlert("Error.",
				"Please make sure all fields are filled out.");
		return null;
	}

	@Override
	public Parent getRoot() {
		return root;
	}

	public interface Factory {
		PrescriptionRequestPopupController create();
	}
}
