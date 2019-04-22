package edu.wpi.cs3733.d19.teamO.request.pizzapi.response;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import edu.wpi.cs3733.d19.teamO.request.pizzapi.Order;

public class OrderResponse extends GenericJson {

  @Key("Order")
  private Order order;

  public Order getOrder() {
    return order;
  }
}
