package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import com.google.api.client.json.GenericJson;
import com.google.common.base.MoreObjects;

public class Product extends GenericJson {

  private String code;
  private String name;

  public Product(String code, String name) {
    this.code = code;
    this.name = name;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("code", code)
        .add("name", name)
        .toString();
  }
}
