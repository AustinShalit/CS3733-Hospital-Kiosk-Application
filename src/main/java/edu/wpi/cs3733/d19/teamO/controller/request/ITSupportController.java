package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.RequestController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.ITSupportRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "ITSupport.fxml")
public class ITSupportController implements Controller {

	private static final Logger logger = Logger.getLogger(ITSupportController.class.getName());

	@FXML
	private BorderPane root;
	@FXML
	private JFXTextField nametxt;
	@FXML
	private JFXComboBox<Node> locationbox;
	@FXML
	private JFXComboBox<ITSupportRequest.ITSupportRequestType> categorybox;
	@FXML
	private JFXTextArea descriptiontxt;
	@FXML
	private JFXButton submitbtn;
	@FXML
	private JFXButton backbtn;

	@Inject
	private EventBus eventBus;
	@Inject
	private Database db;
	@Inject
	private RequestController.Factory requestControllerFactory;

	@FXML
	void initialize() {
		DialogHelper.populateComboBox(db, locationbox);
		categorybox.getItems().setAll(ITSupportRequest.ITSupportRequestType
				.values());
	}

	@FXML
	void onbackButtonAction() {
		eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
	}

	@FXML
	void onSubmitButtonAction() {
		ITSupportRequest itSupport = parseUserITSupportRequest();
		if (itSupport == null) {
			logger.log(Level.WARNING,
					"Unable to parse IT Support Request.",
					"Unable to parse IT Support Request.");
			return;
		}

		if (db.insertITSupportRequest(itSupport)) {
			String message = "Successfully submitted IT Support request.";
			DialogHelper.showInformationAlert("Success!", message);
		} else {
			DialogHelper.showErrorAlert("Error.",
					"Unable to submit IT Support request.");
		}
	}

	/**
	 * Parse input the user has inputted for the sanitation request.
	 *
	 * @return If valid input, A SanitationRequest representing the users input. Otherwise null.
	 */
	private edu.wpi.cs3733.d19.teamO.entity.ITSupportRequest parseUserITSupportRequest() {
		// if input is valid, parse it and return a new SanitationRequest
		if (!descriptiontxt.getText().isEmpty()
				&& !nametxt.getText().isEmpty()
				&& Objects.nonNull(locationbox.getValue())
				&& Objects.nonNull(categorybox.getValue())) {

			LocalDateTime now = LocalDateTime.now();
			Node node = locationbox.getValue();

			String type = categorybox.getValue().toString().toUpperCase(new Locale("EN"));
			ITSupportRequest.ITSupportRequestType itSupportRequestType =
					ITSupportRequest.ITSupportRequestType.valueOf(type);

			String description = descriptiontxt.getText();
			String name = nametxt.getText();

			return new ITSupportRequest(now, node, itSupportRequestType, description, name);
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
		ITSupportController create();
	}
}
