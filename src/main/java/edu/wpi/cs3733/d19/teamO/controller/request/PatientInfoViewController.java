package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;

import animatefx.animation.Shake;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.PatientInfoRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "PatientInfoView.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class PatientInfoViewController implements Controller {

	@FXML
	public BorderPane root;
	@FXML
	private JFXButton assignButton;
	@FXML
	private JFXButton deleteEntryButton;
	@FXML
	private Label titleLabel;
	@FXML
	private TableView<PatientInfoRequest> requestsTableView;
	@FXML
	private TableColumn<PatientInfoRequest, Integer> idCol;
	@FXML
	private TableColumn<PatientInfoRequest, LocalDateTime> timeRequestedCol;
	@FXML
	private TableColumn<PatientInfoRequest, String> whoCompletedCol;
	@FXML
	private TableColumn<PatientInfoRequest, String> descriptionCol;
	@FXML
	private TableColumn<PatientInfoRequest, String> locationTableCol;
	@FXML
	private TableColumn<PatientInfoRequest, String> patientNameCol;
	@FXML
	private TableColumn<PatientInfoRequest, LocalDate> patientDOBCol;
	@FXML
	private TableColumn<PatientInfoRequest, PatientInfoRequest.PatientInfoSex> patientSexCol;

	@Inject
	private Database db;
	@Inject
	private PatientInfoPopupController.Factory patientInfoPopupFactory;

	private JFXPopup addPopup;

	@FXML
	void initialize() {
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
		whoCompletedCol.setCellValueFactory(new PropertyValueFactory<>("whoCompleted"));
		descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		locationTableCol.setCellValueFactory(new PropertyValueFactory<>("locationNodeIdString"));
		patientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
		patientDOBCol.setCellValueFactory(new PropertyValueFactory<>("patientDOB"));
		patientSexCol.setCellValueFactory(new PropertyValueFactory<>("patientSex"));


		requestsTableView.getItems().setAll(db.getAllPatientInfoRequests());

		// sort by id
		requestsTableView.getSortOrder().add(idCol);

		// make columns auto-resize
		requestsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		for (TableColumn column : requestsTableView.getColumns()) {
			column.setPrefWidth(1000); // must be a value larger than the starting window size
		}

		// Initialize the Add Request Popup
		addPopup = new JFXPopup(patientInfoPopupFactory.create().root);
		addPopup.setOnAutoHide(
				event -> {
					ColorAdjust reset = new ColorAdjust();
					reset.setBrightness(0);
					root.setEffect(reset);
					requestsTableView.getItems().clear();
					requestsTableView.getItems().setAll(db.getAllPatientInfoRequests());
				}
		);

		assignButton.setDisable(true);
		deleteEntryButton.setDisable(true);

		// Disable complete request and assign employee button if no request is selected
		requestsTableView.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> {
					if (newValue == null) {
						assignButton.setDisable(true);
						deleteEntryButton.setDisable(true);
					} else {
						assignButton.setDisable(false);
						deleteEntryButton.setDisable(false);
					}
				}
		);
	}

	@FXML
	void onAddButtonAction() {
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(-0.2);
		root.setEffect(colorAdjust);
		addPopup.show(root);
		addPopup.setX(
				(root.getScene().getWindow().getWidth() - addPopup.getWidth()) / 2
		);
		addPopup.setY(
				(root.getScene().getWindow().getHeight() - addPopup.getHeight()) / 2
		);
	}

	@FXML
	void onAssignButtonAction() {
		List<String> choices = new ArrayList<>();
		Set<Employee> emps = db.getAllEmployee();
		for (Employee employee : emps) {
			if (employee.getEmployeeAttributes().getCanFulfillPatientInfo()) {
				System.out.println(employee.getType());
				choices.add(employee.getName());
			}
		}

		String name = DialogHelper.choiceInputDialog(choices, "Assign",
				"Assign a Patient Info Employee", "Select an Employee");

		PatientInfoRequest selectedItem =
				requestsTableView.getSelectionModel().getSelectedItem();
		selectedItem.setWhoCompleted(name);
		db.updatePatientInfoRequest(selectedItem);

		requestsTableView.getItems().setAll(db.getAllPatientInfoRequests());
		requestsTableView.getSortOrder().add(idCol); //sort
	}

	@FXML
	void onDeleteEntryButtonAction() {
		if (requestsTableView.getSelectionModel().isEmpty()) {
			new Shake(deleteEntryButton).play();
			return;
		}

		PatientInfoRequest selectedItem =
				requestsTableView.getSelectionModel().getSelectedItem();
		requestsTableView.getItems().remove(selectedItem);

		db.deletePatientInfoRequest(selectedItem);

	}

	@Override
	public Parent getRoot() {
		return root;
	}

	public interface Factory {
		PatientInfoViewController create();
	}
}
