package edu.wpi.cs3733.d19.teamO.request.controller;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.request.Request;

public class OrderTableController {

  @FXML
  private BorderPane root;
  @FXML
  private TableView<Request> orderTable;
  @FXML
  private TableColumn<Request, String> firstNameCol;
  @FXML
  private TableColumn<Request, String> lastNameCol;
  @FXML
  private TableColumn<Request, Integer> idCol;
  @FXML
  private TableColumn<Request, String> locationCol;
  @FXML
  private TableColumn<Request, String> phoneCol;
  @FXML
  private TableColumn<Request, String> timeDateCol;
  @FXML
  private TableColumn<Request, Double> totalCol;
  @FXML
  private TableColumn<Request, String> descriptionCol;


}
