package edu.wpi.cs3733.d19.teamO.request.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;

public class OrderController {
  /**
   * Customer Information.
   */
  @FXML
  private TitledPane customerInformationPane;
  @FXML
  private JFXTextField firstName;
  @FXML
  private JFXTextField lastName;
  @FXML
  private JFXTextField emailAddress;
  @FXML
  private JFXTextField phoneNumber;
  @FXML
  private JFXTextField streetAddress;
  @FXML
  private JFXTextField city;
  @FXML
  private JFXTextField zipCode;
  @FXML
  private JFXComboBox<String> state;

  /**
   * Menu.
   */
  @FXML
  private TitledPane menuPane;

  /**
   * Place Order.
   */
  @FXML
  private TitledPane placeOrderPane;
  @FXML
  private Text carryoutOrDeliveryToAddressText;
  @FXML
  private JFXTextField deliveryInstructions;
  @FXML
  private TableView<String> orderSummaryTableView;
  @FXML
  private TableColumn<String, String> itemColumn;
  @FXML
  private TableColumn<String, Integer> qtyColumn;
  @FXML
  private TableColumn<String, Double> priceColumn;
  @FXML
  private Text subtotalText;
  @FXML
  private Text taxText;
  @FXML
  private Text totalText;
  @FXML
  private Text emailAddressText;
  @FXML
  private Text phoneNumberText;
  @FXML
  private Text firstNameText;
  @FXML
  private Text lastNameText;
  @FXML
  private JFXTextField creditCardNumber;
  @FXML
  private JFXComboBox expirationMonth;
  @FXML
  private JFXComboBox expirationYear;
  @FXML
  private JFXTextField securityCode;
  @FXML
  private JFXButton submitOrderButton;

  @FXML
  void initialize() {
    customerInformationPane.setExpanded(true);
    menuPane.setExpanded(false);
    placeOrderPane.setExpanded(false);
  }

}
