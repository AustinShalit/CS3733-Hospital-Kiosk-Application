package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * The Menu is our primary interface with the API.
 *
 * This is far and away the most complicated class - it wraps up most of
 * the logic that parses the information we get from the API.
 */
public class Menu extends GenericJson {

  @Key("PreconfiguredProducts")
  private GenericJson productsJson;

  public List<Product> getProducts() {
    List<Product> products = new LinkedList<>();
    productsJson.forEach((key, value) -> {
      Map product = (Map) value;
      products.add(new Product(product.get("Code").toString(),
          product.get("Name").toString()));
    });
    return products;
  }
}
