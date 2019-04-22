package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * Loose representation of a coupon - no logic.
 * This is a coupon - you can add it to an Order (order.add_item) and,
 * if it fits, get some money off your purchase. I think.
 * This is another thing that's worth exploring - there are some sweet
 * coupons that would be awful without the coupon.
 */
public class Coupon {

  private final StringProperty code = new SimpleStringProperty();
  private final IntegerProperty quantity = new SimpleIntegerProperty(1);
  private final IntegerProperty id = new SimpleIntegerProperty(1);
  private final BooleanProperty isNew = new SimpleBooleanProperty(true);

  public String getCode() {
    return code.get();
  }

  public StringProperty codeProperty() {
    return code;
  }

  public void setCode(String code) {
    this.code.set(code);
  }

  public int getQuantity() {
    return quantity.get();
  }

  public IntegerProperty quantityProperty() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity.set(quantity);
  }

  public int getId() {
    return id.get();
  }

  public IntegerProperty idProperty() {
    return id;
  }

  public void setId(int id) {
    this.id.set(id);
  }

  public boolean isIsNew() {
    return isNew.get();
  }

  public BooleanProperty isNewProperty() {
    return isNew;
  }

  public void setIsNew(boolean isNew) {
    this.isNew.set(isNew);
  }
}
