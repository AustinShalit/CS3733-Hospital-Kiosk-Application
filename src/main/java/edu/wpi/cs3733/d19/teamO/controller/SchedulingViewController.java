package edu.wpi.cs3733.d19.teamO.controller;

import java.time.LocalDateTime;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;

@FxmlController(url = "SchedulingViewWindow.fxml")
public class SchedulingViewController implements Controller {

	@FXML
	private AnchorPane root;
	@FXML
	private TableView<SchedulingRequest> table;
	@FXML
	private TableColumn<SchedulingRequest, Integer> idTableCol;
	@FXML
	private TableColumn<SchedulingRequest, LocalDateTime> startTimeCol;
	@FXML
	private TableColumn<SchedulingRequest, LocalDateTime> endTimeCol;
	@FXML
	private TableColumn<SchedulingRequest, LocalDateTime> timeRequestedCol;
	@FXML
	private TableColumn<SchedulingRequest, LocalDateTime> timeCompletedCol;
	@FXML
	private TableColumn<SchedulingRequest, String> whoRequestedCol;
	@FXML
	private TableColumn<SchedulingRequest, String> roomCol;

	@FXML
	void initialize() {
		idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
		endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
		timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
		timeCompletedCol.setCellValueFactory(new PropertyValueFactory<>("timeCompleted"));
		whoRequestedCol.setCellValueFactory(new PropertyValueFactory<>("whoReserved"));
		roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));

		// sort by id
		table.getSortOrder().add(idTableCol);

		// make columns auto-resize
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		for (TableColumn column : table.getColumns()) {
			column.setPrefWidth(1000); // must be a value larger than the starting window size
		}
	}

	public void setRequests(ObservableList<SchedulingRequest> requests) {
		table.setItems(requests);
	}

	public interface Factory {
		SchedulingViewController create();
	}

	@Override
	public Parent getRoot() {
		return root;
	}
}
