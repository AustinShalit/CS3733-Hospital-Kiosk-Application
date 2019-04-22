package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import java.util.Objects;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.common.base.MoreObjects;

public class Product extends GenericJson {

  @Key("Code")
  private String code;
  @Key("Name")
  private String name;

  public Product() {

  }

  /**
   * Create a Product object.
   */
  public Product(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("code", code)
        .add("name", name)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Product)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Product product = (Product) o;
    return code.equals(product.code)
        &&
        name.equals(product.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), code, name);
  }
}
