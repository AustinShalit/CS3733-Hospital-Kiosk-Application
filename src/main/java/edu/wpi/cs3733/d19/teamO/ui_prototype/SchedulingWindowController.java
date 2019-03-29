package edu.wpi.cs3733.d19.teamO.ui_prototype;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.sql.Time;

public class SchedulingWindowController {

    @FXML
    private Button back;

    @FXML
    private Button submit;

    @FXML
    ComboBox<String> roomComboBox;

    @FXML
    ChoiceBox<Time> startChoiceBox;

    @FXML
    ChoiceBox<Time> endChoiceBox;

    @FXML
    DatePicker datePicker;

}
