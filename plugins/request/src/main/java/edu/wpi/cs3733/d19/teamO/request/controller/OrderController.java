package edu.wpi.cs3733.d19.teamO.request.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.request.pizzapi.Address;
import edu.wpi.cs3733.d19.teamO.request.pizzapi.Customer;
import edu.wpi.cs3733.d19.teamO.request.pizzapi.Menu;
import edu.wpi.cs3733.d19.teamO.request.pizzapi.Order;
import edu.wpi.cs3733.d19.teamO.request.pizzapi.Product;
import edu.wpi.cs3733.d19.teamO.request.pizzapi.Store;

import static edu.wpi.cs3733.d19.teamO.request.controller.ControllerHelper.populateStatesCombobox;

@FxmlController(url = "Order.fxml")
public class OrderController implements Controller {

  /**
   * Temporary vars used to test the app
   */
//  Address dummyAddress = new Address(
//      "100 Insitiute Road",
//      "Worcester",
//      "MA",
//      "01609"
//  );
//
//  Store dummyStore;
//
//  {
//    try {
//      dummyStore = dummyAddress.getClosestStore().get();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  ObservableList dummyProducts;
//  {
//    try {
//       dummyProducts =
//          FXCollections.observableArrayList(dummyStore.getMenu().getProducts());
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  @Inject
  private EventBus eventBus;
  @Inject
  private DominosHomeController.Factory dominosHomeControllerFactory;
  @Inject
  private Database db; // todo put order info in database to refresh table view

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton backButton;

  /**
   * Menu.
   */
  @FXML
  private TitledPane menuPane;
  @FXML
  private JFXComboBox<Product> cart;
  private LinkedList<Product> customersCart = new LinkedList<>();

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
   * Place Order.
   */
  @FXML
  private TitledPane placeOrderPane;
  @FXML
  private Text carryoutOrDeliveryToAddressText;
  @FXML
  private JFXTextField deliveryInstructions;
  @FXML
  private Text orderingFromText;
  @FXML
  private TableView<Product> orderSummaryTableView;
  @FXML
  private TableColumn<Product, String> itemColumn;
  //  @FXML
  //  private TableColumn<Product, Integer> qtyColumn;
  //  @FXML
  //  private TableColumn<Product, Double> priceColumn;
  @FXML
  private Text subtotalText;
  @FXML
  private Text taxText;
  @FXML
  private Text surchargeText;
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
  //  @FXML
  //  private JFXTextField creditCardNumber;
  //  @FXML
  //  private JFXComboBox<Integer> expirationMonth;
  //  @FXML
  //  private JFXComboBox<Integer> expirationYear;
  //  @FXML
  //  private JFXTextField securityCode;
  @FXML
  private JFXButton submitOrderButton;

  @FXML
    void initialize() {
    // show/expand panes to start
    menuPane.setExpanded(false);
    customerInformationPane.setExpanded(true);
    placeOrderPane.setExpanded(false);

    // only 1 should be expanded at once
    titledPaneListeners();

    // set comboboxes
    populateStatesCombobox(state);
    //    populateMonthCombobox(expirationMonth);
    //    populateYearCombobox(expirationYear);

    // initialize table view
    itemColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    //    qtyColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
    //    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    // make columns auto-resize
    orderSummaryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    for (TableColumn column : orderSummaryTableView.getColumns()) {
      column.setPrefWidth(1000); // must be a value larger than the starting window size
    }

    refreshTableView(new LinkedList<>());
  }

  @FXML
  void backAction() {
    eventBus.post(new ChangeMainViewEvent(dominosHomeControllerFactory.create()));
  }

  @FXML
  void onSubmitAction() throws IOException {
    DialogHelper.showErrorAlert("Error", "Complete order has not been implemented yet.");
  }

  @FXML
  private void populateOrderInfo() throws IOException {
    Address address = parseCustomerAddress();
    Customer customer = parseCustomerInfo();

    Store store = address.getClosestStore().get();
    Menu menu = store.getMenu();
    List<Product> products = menu.getProducts();

    Order order = new Order(address, store, menu, customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getPhone());

    if (cart.getItems().isEmpty()) {
      cart.getItems().addAll(products);
    }

    System.out.println(cart.getValue());

    if (cart.getValue() != null) {
      order.addProduct(cart.getValue());
      Order priced = order.getOrder();

      refreshTableView(order.getProducts());

      // set text fields
      carryoutOrDeliveryToAddressText.setText("Delivery to " + address.getStreet() + ", " + address.getCity() + ", " + address.getRegion() + " " + address.getZip());
//    orderingFromText.setText("Ordering from " + store.getId());
      emailAddressText.setText("Email Address: " + customer.getEmail());
      phoneNumberText.setText("Phone Number: " + customer.getPhone());
      firstNameText.setText("First Name: " + customer.getFirstName());
      lastNameText.setText("Last Name: " + customer.getLastName());
      subtotalText.setText("Subtotal: " + priced.getAmounts().getMenu());
      taxText.setText("Tax: " + priced.getAmounts().getTax());
      surchargeText.setText("Surcharge: " + priced.getAmounts().getSurcharge());
      totalText.setText("Total: " + priced.getAmounts().getPayment());
    }
  }

  /**
   * Parse customer information.
   *
   * @return If valid input, A Customer representing the users input. Otherwise null.
   */
  private Customer parseCustomerInfo() {
    if (!firstName.getText().isEmpty()
    && !lastName.getText().isEmpty()
    && !emailAddress.getText().isEmpty()
    && !phoneNumber.getText().isEmpty()) {
      return new Customer(firstName.getText(), lastName.getText(), emailAddress.getText(), phoneNumber.getText());
    }

    // Invalid Input
    DialogHelper.showErrorAlert("Error.", "Please make sure all customer are filled out.");
    return null;
  }

  /**
   * Parse customer address.
   *
   * @return If valid input, An Address representing the users input. Otherwise null.
   */
  private Address parseCustomerAddress() {
    if (!streetAddress.getText().isEmpty()
    && !city.getText().isEmpty()
    && !zipCode.getText().isEmpty()
    && Objects.nonNull(state.getValue())) {
      return new Address(streetAddress.getText(), city.getText(), zipCode.getText(), state.getValue());
    }
    // Invalid Input
    DialogHelper.showErrorAlert("Error.", "Please make sure address fields are filled out.");
    return null;
  }

  /**
   * Allow for only one TitledPane to be expanded at a time. Also handles program behavior
   * when a user clicks on a tile pane.
   */
  void titledPaneListeners() {
    menuPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
      if(menuPane.isExpanded()) {
        customerInformationPane.setExpanded(false);
        placeOrderPane.setExpanded(false);
      }
    });

    customerInformationPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
      if(customerInformationPane.isExpanded()) {
        menuPane.setExpanded(false);
        placeOrderPane.setExpanded(false);
      }

      try {
        populateOrderInfo();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    placeOrderPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
      if(placeOrderPane.isExpanded()) {
        menuPane.setExpanded(false);
        customerInformationPane.setExpanded(false);
      }
      try {
        populateOrderInfo();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  void refreshTableView(List<Product> products) {
    orderSummaryTableView.getItems().setAll(products);
  }


  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    OrderController create();
  }
}
