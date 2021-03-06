package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * The Menu is our primary interface with the API.
 *
 * <p>This is far and away the most complicated class - it wraps up most of
 * the logic that parses the information we get from the API.
 * </p>
 */
public class Menu extends GenericJson {

  @Key("Variants")
  private GenericJson variants;
  @Key("PreconfiguredProducts")
  private GenericJson productsJson;

  /**
   * Get all the products on this menu.
   */
  public List<Product> getProducts() {
    List<Product> products = new LinkedList<>();
    productsJson.forEach((key, value) -> {
      Map productJson = (Map) value;
      Product product = new Product(productJson.get("Code").toString(),
          productJson.get("Name").toString());
      if (variants.get(product.getCode()) != null) {
        products.add(product);
      }
    });
    return products;
  }

  /**
   * Get a product in a list given its name.
   */
  public Product getProduct(List<Product> products, String name) {
    for (Product p : products) {
      if (p.getName().equals(name)) {
        return p;
      }
    }
    return null;
  }

  public GenericJson getVariants() {
    return variants;
  }

  public GenericJson getProductsJson() {
    return productsJson;
  }
}
