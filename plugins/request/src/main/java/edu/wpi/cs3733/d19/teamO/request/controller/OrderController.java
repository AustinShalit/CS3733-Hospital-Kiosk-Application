package edu.wpi.cs3733.d19.teamO.request.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

import static edu.wpi.cs3733.d19.teamO.request.controller.ControllerHelper.populateMonthCombobox;
import static edu.wpi.cs3733.d19.teamO.request.controller.ControllerHelper.populateStatesCombobox;
import static edu.wpi.cs3733.d19.teamO.request.controller.ControllerHelper.populateYearCombobox;

@FxmlController(url = "Order.fxml")
public class OrderController implements Controller {

  @Inject
  private EventBus eventBus;
  @Inject
  private DominosHomeController.Factory dominosHomeControllerFactory;
  @Inject
  private Database db;

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton backButton;

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
  private JFXComboBox<Integer> expirationMonth;
  @FXML
  private JFXComboBox<Integer> expirationYear;
  @FXML
  private JFXTextField securityCode;
  @FXML
  private JFXButton submitOrderButton;

  @FXML
    void initialize() {
    // show/expand panes to start
    customerInformationPane.setExpanded(true);
    menuPane.setExpanded(false);
    placeOrderPane.setExpanded(false);

    // set comboboxes
    populateStatesCombobox(state);
    populateMonthCombobox(expirationMonth);
    populateYearCombobox(expirationYear);
  }

  @FXML
  void backAction() {
    eventBus.post(new ChangeMainViewEvent(dominosHomeControllerFactory.create()));
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    OrderController create();
  }
}
