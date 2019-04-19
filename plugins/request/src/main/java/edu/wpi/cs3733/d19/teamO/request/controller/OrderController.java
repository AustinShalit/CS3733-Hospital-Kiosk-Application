package edu.wpi.cs3733.d19.teamO.request.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
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
   * Variables that maintain stateComboBox about the customers current order
   */
  private Customer customer;
  private Address address;
  private Store store;
  private Menu menu;
  private List<Product> cart2;

  /**
   * For scene switching
   */
  @FXML
  private BorderPane root;

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    OrderController create();
  }

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
  private JFXComboBox<String> stateComboBox;
  @FXML
  private JFXTextField zipCode;

  /**
   * Menu.
   */
  @FXML
  private TitledPane menuPane;
  @FXML
  private TableView<Product> menuTableView;
  @FXML
  private TableColumn<Product, String> menuItemCol;

  /**
   * Place Order.
   */
  @FXML
  private TitledPane placeOrderPane;
  @FXML
  private Text orderSettingsText;
  @FXML
  private Text customerInformationText;
  @FXML
  private Text paymentInformationText;
  @FXML
  private Text orderSummaryText;
  @FXML
  private TableView<Product> orderSummaryTableView;
  @FXML
  private TableColumn<Product, String> orderItemColumn;
  @FXML
  private JFXCheckBox enableRealTransactionCheckbox;

  @Inject
  private EventBus eventBus;
  @Inject
  private ReorderController.Factory reorderControllerFactory;

  @FXML
  void initialize() {
    // show/hide panes to start
    customerInformationPane.setExpanded(true);
    menuPane.setExpanded(false);
    placeOrderPane.setExpanded(false);

    // only 1 should be expanded at once
    titledPaneListeners();

    // set comboboxes
    //todo

    // initialize table views
    orderItemColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    menuItemCol.setCellValueFactory(new PropertyValueFactory<>("name"));

    // make columns auto-resize
    enableResizableColumns(menuTableView);
    enableResizableColumns(orderSummaryTableView);
  }


  /**
   * Parse a new Customer object from their entered information. Displays an error message and
   * returns false if their input was invalid.
   */
  @FXML
  Boolean setCustomer() {
    if (!firstName.getText().isEmpty()
        && !lastName.getText().isEmpty()
        && !emailAddress.getText().isEmpty()
        && !phoneNumber.getText().isEmpty()) {

      customer = new Customer(firstName.getText(), lastName.getText(), emailAddress.getText(),
          phoneNumber.getText());

      return true;
    }

    // Invalid Input
    DialogHelper.showErrorAlert("Error.",
        "Please make sure all customer fields are filled out.");
    return false;
  }

  /**
   * Parse a new Address object from their entered information. Returns false if their input was
   * invalid.
   */
  @FXML
  Boolean setAddress() {
    if (!streetAddress.getText().isEmpty()
        && !city.getText().isEmpty()
        && !zipCode.getText().isEmpty()
        && Objects.nonNull(stateComboBox.getValue())) {

      address = new Address(streetAddress.getText(),
          city.getText(), zipCode.getText(), stateComboBox.getValue());

      return true;
    }
    return false;
  }

  /**
   * When a user clicks on the menu pane, make sure customer info is parsed. Then, populate the
   * menu for them to select from.
   */
  @FXML
  void onMenuPaneClicked() throws IOException {
//    if(setCustomer() && setAddress()) {
//      menuPane.setExpanded(true);
//      if(setStore()) {
//        setMenu();
//        refreshTableView(menuTableView, menu.getProducts());
//      }
//    } else {
      menuPane.setExpanded(false);
      customerInformationPane.setExpanded(true);
//    }
  }

  /**
   * Set the closest store based on the customer information. Return true if successful, false
   * otherwise.
   */
  Boolean setStore() throws IOException {
    if(setCustomer() && setAddress()) {
      store = address.getClosestStore().get();
      return true;
    }
    return false;
  }

  /**
   * Fill the menu with available items at the customers store. Return true if successful,
   * false otherwise.
   */
  Boolean setMenu() throws IOException {
    if(setStore()) {
      menu = store.getMenu();
      return true;
    }
    return false;
  }

  /**
   * When a user clicks on the order pane, make sure they have entered their information and
   * selected something from the menu
   */
  @FXML
  void onPlaceOrderPaneClicked() {
    if(!cart2.isEmpty()) {
      refreshTableView(orderSummaryTableView, cart2);
      placeOrderPane.setExpanded(true);
    } else {
      DialogHelper.showErrorAlert("Error", "Please select at least one item from the menu.");
      placeOrderPane.setExpanded(false);
    }
  }

  /**
   * Fill the text fields and the order summary table with information.
   */
  @FXML
  void fillOrderInformation() {
//      order.addProduct(cart.getValue());
//
//      refreshTableView(order.getProducts());

      // set text fields
//      carryoutOrDeliveryToAddressText.setText("Delivery to " + address.getStreet() + ", "
//          + address.getCity() + ", " + address.getRegion() + " " + address.getZip());
//      //    orderingFromText.setText("Ordering from " + store.getId());
//      emailAddressText.setText("Email Address: " + customer.getEmail());
//      phoneNumberText.setText("Phone Number: " + customer.getPhone());
//      firstNameText.setText("First Name: " + customer.getFirstName());
//      lastNameText.setText("Last Name: " + customer.getLastName());
//
//      Order priced = order.getOrder();
//      subtotalText.setText("Subtotal: " + priced.getAmounts().getMenu());
//      taxText.setText("Tax: " + priced.getAmounts().getTax());
//      surchargeText.setText("Surcharge: " + priced.getAmounts().getSurcharge());
//      totalText.setText("Total: " + priced.getAmounts().getPayment());

  }

  /**
   * Submit the order to the dominos API
   */
  @FXML
  void onSubmitAction() {
  }

  /**
   * Toggle the submit button enable/disable
   */
  @FXML
  void toggleSubmitButton() {
  }

  /**
   * Allow for only one TitledPane to be expanded at a time. Also handles program behavior
   * when a user clicks on a tile pane.
   */
  void titledPaneListeners() {
    customerInformationPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
      if (customerInformationPane.isExpanded()) {
        menuPane.setExpanded(false);
        placeOrderPane.setExpanded(false);
      }
    });

    menuPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
      if (menuPane.isExpanded()) {
        customerInformationPane.setExpanded(false);
        placeOrderPane.setExpanded(false);
      }
    });

    placeOrderPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
      if (placeOrderPane.isExpanded()) {
        menuPane.setExpanded(false);
        customerInformationPane.setExpanded(false);
      }
    });
  }

  /**
   * Enable resizable, dynamic columns for the tableview.
   */
  <T> void enableResizableColumns(TableView<T> tableView) {
    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    for (TableColumn column : tableView.getColumns()) {
      column.setPrefWidth(1000); // must be a value larger than the starting window size
    }
  }

  /**
   * Refresh the given tableview.
   */
  <T> void refreshTableView(TableView<T> tableView, List<T> items) {
    tableView.getItems().setAll(items);
  }

  private void addButtonToTable() {
    TableColumn<Product, Void> colBtn = new TableColumn<Product, Void>("Add/Remove");

    Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
      @Override
      public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
        final TableCell<Product, Void> cell = new TableCell<Product, Void>() {

          private final Button btn = new Button("Add");

          {
            btn.setOnAction((ActionEvent event) -> {
              Product product = getTableView().getItems().get(getIndex());
              System.out.println("selected Product: " + product);
              // todo add to cart
            });
          }

          @Override
          public void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setGraphic(null);
            } else {
              setGraphic(btn);
            }
          }
        };
        return cell;
      }
    };

    colBtn.setCellFactory(cellFactory);

    menuTableView.getColumns().add(colBtn);

  }
}
