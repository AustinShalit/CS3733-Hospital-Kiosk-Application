package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.common.base.MoreObjects;

public class Product extends GenericJson {

  @Key("Code")
  private String code;
  @Key("Name")
  private String name;

  public static String removeOption;

  public Product() {

  }

  /**
   * Create a Product object.
   */
  public Product(String code, String name) {
    this.code = code;
    this.name = name;
    this.removeOption = "Remove Item";
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

  public String getRemoveOption() {
    return removeOption;
  }

  public void setRemoveOption(String removeOption) {
    this.removeOption = removeOption;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("code", code)
        .add("name", name)
        .toString();
  }
}
