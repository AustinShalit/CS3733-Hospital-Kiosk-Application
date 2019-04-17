package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class AddressTest {

  @Test
  void getNearbyStoresTest() throws IOException {
    Address address = new Address(
        "100 Insitiute Road",
        "Worcester",
        "MA",
        "01609"
    );

    System.out.println(address.getClosestStore().get().getMenu().getProducts().toString());
  }

  @Test
  void orderTest() throws IOException {
    Address address = new Address(
        "100 Insitiute Road",
        "Worcester",
        "MA",
        "01609"
    );

    Store store = address.getClosestStore().get();
    Menu menu = store.getMenu();
    List<Product> products = menu.getProducts();
    Order order = new Order(address, store, menu, "Austin", "Shalit", "auschase@aol.com", "8184244692");
    order.addProduct(products.get(0));
    Order priced = order.getOrder();
    System.out.println(priced.toPrettyString());
    System.out.println(order.getOrder().getAmounts());
  }
}
