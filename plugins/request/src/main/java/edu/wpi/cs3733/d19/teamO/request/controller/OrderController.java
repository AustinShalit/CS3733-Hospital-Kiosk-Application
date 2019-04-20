package edu.wpi.cs3733.d19.teamO.request.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

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

@SuppressWarnings({"PMD.TooManyMethods", "PMD.TooManyFields"})
@FxmlController(url = "Order.fxml")
public class OrderController implements Controller {

  /**
   * Variables that maintain stateComboBox about the customers current order.
   */
  private Customer customer;
  private Address address;
  private Store store;
  private Menu menu;
  private Order order;

  /**
   * For scene switching.
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
  private StackPane menuStackPane;

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
  void initialize() {
    // show/hide panes to start
    customerInformationPane.setExpanded(true);
    menuPane.setExpanded(false);
    placeOrderPane.setExpanded(false);

    // only 1 should be expanded at once
    titledPaneListeners();

    // set comboboxes
    populateStatesCombobox(stateComboBox);

    // initialize table views
    orderItemColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    // make columns auto-resize
    enableResizableColumns(orderSummaryTableView);
  }


  /**
   * Parse a new Customer object from their entered information. Returns false if their input was
   * invalid.
   */
  @FXML
  boolean setCustomer() {
    if (!firstName.getText().isEmpty()
        && !lastName.getText().isEmpty()
        && !emailAddress.getText().isEmpty()
        && !phoneNumber.getText().isEmpty()) {

      customer = new Customer(firstName.getText(), lastName.getText(), emailAddress.getText(),
          phoneNumber.getText());

      return true;
    }
    return false;
  }

  /**
   * Parse a new Address object from their entered information. Returns false if their input was
   * invalid.
   */
  @FXML
  boolean setAddress() {
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
  void onMenuPaneExpanded() throws IOException {
    if (setCustomer() && setAddress()) {
      if (setStore()) {
        setMenu();
        refreshMenu(menu.getProducts());
        order = new Order(address, store, menu, customer.getFirstName(), customer.getLastName(),
            customer.getEmail(), customer.getPhone());
        menuPane.setExpanded(true);
      }
    } else {
      menuPane.setExpanded(false);
      customerInformationPane.setExpanded(true);
      invalidCustomerInfo();
    }
  }

  /**
   * Set the closest store based on the customer information. Return true if successful, false
   * otherwise.
   */
  boolean setStore() throws IOException {
    if (setCustomer() && setAddress()) {
      store = address.getClosestStore().get();
      return true;
    } else {
      return false;
    }
  }

  /**
   * Fill the menu with available items at the customers store. Return true if successful,
   * false otherwise.
   */
  boolean setMenu() throws IOException {
    if (setStore()) {
      menu = store.getMenu();
      return true;
    }
    return false;
  }

  /**
   * When a user clicks on the order pane, make sure they have entered their information and
   * selected something from the menu.
   */
  @FXML
  void onPlaceOrderPaneExpanded() throws IOException {
    if (setCustomer() && setAddress()) {
      if (order != null && !order.getProducts().isEmpty()) {
        fillOrderInformation();
        placeOrderPane.setExpanded(true);
      } else {
        placeOrderPane.setExpanded(false);
        menuPane.setExpanded(true);
        invalidMenuSelection();
      }
    } else {
      placeOrderPane.setExpanded(false);
      customerInformationPane.setExpanded(true);
      invalidCustomerInfo();
    }
  }

  /**
   * Fill the text fields and the order summary table with information.
   */
  @FXML
  void fillOrderInformation() throws IOException {
    refreshTableView(orderSummaryTableView, order.getProducts());

    orderSettingsText.setText("Delivery to " + address.getStreet() + ", "
        + address.getCity() + ", " + address.getRegion() + " " + address.getZip() + "\n"
        + "Delivery from Store #" + store.getId());
    customerInformationText.setText("Email Address: " + customer.getEmail() + "\n"
        + "Phone Number: " + customer.getPhone() + "\n"
        + "First Name: " + customer.getFirstName() + "\n"
        + "Last Name: " + customer.getLastName());
    paymentInformationText.setText("Paying with cash.");

    Order priced = order.getOrder();
    orderSummaryText.setText("Subtotal: " + priced.getAmounts().getMenu() + "\n"
        + "Tax: " + priced.getAmounts().getTax() + "\n"
        + "Surcharge: " + priced.getAmounts().getSurcharge() + "\n"
        + "Total: " + priced.getAmounts().getPayment());
  }

  /**
   * Submit the order to dominos.
   */
  @FXML
  void onSubmitAction() {
    DialogHelper.showErrorAlert("", "Ordering has not yet been configured. Please see the source code.");
  }

  /**
   * Allow for only one TitledPane to be expanded at a time. Also handles program behavior
   * when a user clicks on a tile pane.
   */
  void titledPaneListeners() {
    customerInformationPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
      if (isNowExpanded) {
        if (customerInformationPane.isExpanded()) {
          menuPane.setExpanded(false);
          placeOrderPane.setExpanded(false);
        }
      }
    });

    menuPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
      if (isNowExpanded) {
        try {
          onMenuPaneExpanded();
        } catch (IOException e) {
          e.printStackTrace();
        }
        if (menuPane.isExpanded()) {
          customerInformationPane.setExpanded(false);
          placeOrderPane.setExpanded(false);
        }
      }
    });

    placeOrderPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
      if (isNowExpanded) {
        try {
          onPlaceOrderPaneExpanded();
        } catch (IOException e) {
          e.printStackTrace();
        }
        if (placeOrderPane.isExpanded()) {
          menuPane.setExpanded(false);
          customerInformationPane.setExpanded(false);
        }
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

  void invalidCustomerInfo() {
    DialogHelper.showErrorAlert("Error.",
        "Please make sure all customer info is filled out correctly.");
  }

  void invalidMenuSelection() {
    DialogHelper.showErrorAlert("Error.",
        "Please make sure to select at least one menu item.");
  }

  /**
   * Refresh the menu given a list of products.
   */
  void refreshMenu(List<Product> products) {
    ListView<Product> listView = new ListView<>(FXCollections.observableArrayList(products));
    listView.setCellFactory(CheckBoxListCell.forListView(item -> {
      BooleanProperty observable = new SimpleBooleanProperty();
      observable.addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
              order.addProduct(item);
            } else {
              order.removeProduct(item);
            }
          }

      );
      return observable;
    }, new StringConverter<Product>() {
      @Override
      public String toString(Product object) {
        return object.getName();
      }

      @Override
      public Product fromString(String string) {
        return products.stream()
            .filter(product -> string.equals(product.getName()))
            .findFirst()
            .orElse(null);
      }
    }));
    menuStackPane.getChildren().removeAll();
    menuStackPane.getChildren().add(listView);
  }
}
