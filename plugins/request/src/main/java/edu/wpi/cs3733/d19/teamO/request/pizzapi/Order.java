package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.common.base.MoreObjects;

import edu.wpi.cs3733.d19.teamO.request.pizzapi.response.OrderResponse;

/**
 * Core interface to the payments API.
 *
 * <p>The Order is perhaps the second most complicated class - it wraps
 * up all the logic for actually placing the order, after we've
 * determined what we want from the Menu.
 * </p>
 */
public class Order extends GenericJson {

  public static class Amounts extends GenericJson {
    @Key("Menu")
    private double menu;
    @Key("Surcharge")
    private double surcharge;
    @Key("Tax")
    private double tax;
    @Key("Payment")
    private double payment;

    public double getMenu() {
      return menu;
    }

    public double getSurcharge() {
      return surcharge;
    }

    public double getTax() {
      return tax;
    }

    public double getPayment() {
      return payment;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("menu", menu)
          .add("surcharge", surcharge)
          .add("tax", tax)
          .add("payment", payment)
          .toString();
    }
  }

  @Key("Address")
  private Address address;
  private List<Product> products = new LinkedList<>();
  @Key("Amounts")
  private Amounts amounts;
  @Key("FirstName")
  private String firstName;
  @Key("LastName")
  private String lastName;
  @Key("Email")
  private String email;
  @Key("Phone")
  private String phone;

  private Store store;
  private Menu menu;

  /**
   * Default constructor.
   */
  public Order() {

  }

  /**
   * Create an Order object.
   */
  public Order(Address address, Store store, Menu menu, String firstName, String lastName,
               String email, String phone) {
    //    put("Address", address);
    put("Coupons", new ArrayList<>());
    put("CustomerID", "");
    put("Extension", "");
    put("OrderChannel", "OLO");
    put("OrderID", "");
    put("NoCombine", "True");
    put("OrderMethod", "Web");
    put("OrderTaker", "None");
    put("Payments", new ArrayList<>());
    put("Products", new ArrayList());
    put("Market", "");
    put("Currency", "");
    put("ServiceMethod", "Delivery");
    put("Tags", new Object());
    put("Version", "1.0");
    put("SourceOrganizationURI", "order.dominos.com");
    put("LanguageCode", "en");
    put("Partners", new Object());
    put("NewUser", "True");
    put("metaData", new Object());
    put("BusinessDate", "");
    put("EstimatedWaitMinutes", "");
    put("PriceOrderTime", "");
    put("AmountsBreakdown", new Object());

    GenericUrl payments = new GenericUrl();
    payments.put("Type", "Cash");
    put("Payments", payments);

    this.address = address;
    this.store = store;
    this.menu = menu;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.email = email;
  }

  /**
   * Add a product to the order.
   */
  public void addProduct(Product product) {
    Map json = (Map) menu.getVariants().get(product.getCode());
    products.add(product);
    json.put("ID", "1");
    json.put("isNew", "True");
    json.put("Qty", "1");
    json.put("AutoRemove", "False");
    ((List) get("Products")).add(json);
  }

  /**
   * Get an order.
   */
  public Order getOrder() throws IOException {
    put("StoreID", store.getId());
    put("Email", email);
    put("FirstName", firstName);
    put("LastName", lastName);
    put("Phone", phone);
    Map<String, Object> order = new HashMap<>();
    order.put("Order", this);
    JsonHttpContent content = new JsonHttpContent(Utilities.getJsonFactory(), order);
    return Utilities.sendPostRequest(
        Country.USA.getPriceUrl(),
        httpHeaders -> {
          httpHeaders.set("Referer", "https://order.dominos.com/en/pages/order/");
          httpHeaders.setContentType("application/json");
        }, content, OrderResponse.class).getOrder();
  }

  public Order placeOrder() throws IOException {
    put("StoreID", store.getId());
    put("Email", email);
    put("FirstName", firstName);
    put("LastName", lastName);
    put("Phone", phone);
    Map<String, Object> order = new HashMap<>();
    order.put("Order", this);
    JsonHttpContent content = new JsonHttpContent(Utilities.getJsonFactory(), order);
    return Utilities.sendPostRequest(
        Country.USA.getPlaceUrl(),
        httpHeaders -> {
          httpHeaders.set("Referer", "https://order.dominos.com/en/pages/order/");
          httpHeaders.setContentType("application/json");
        }, content, OrderResponse.class).getOrder();
  }

  public Amounts getAmounts() {
    return amounts;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  public void setAmounts(Amounts amounts) {
    this.amounts = amounts;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Store getStore() {
    return store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  public Menu getMenu() {
    return menu;
  }

  public void setMenu(Menu menu) {
    this.menu = menu;
  }
}
