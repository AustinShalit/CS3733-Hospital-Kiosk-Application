package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The Customer orders a pizza.
 *
 * You need a Customer to create an Order. The proprietors of the API
 * use this information, presumably for nefarious Pizza Purposes.
 */
public class Customer {

  private final StringProperty firstName = new SimpleStringProperty();
  private final StringProperty lastName = new SimpleStringProperty();
  private final StringProperty email = new SimpleStringProperty();
  private final StringProperty phone = new SimpleStringProperty();

  public String getFirstName() {
    return firstName.get();
  }

  public StringProperty firstNameProperty() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName.set(firstName);
  }

  public String getLastName() {
    return lastName.get();
  }

  public StringProperty lastNameProperty() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName.set(lastName);
  }

  public String getEmail() {
    return email.get();
  }

  public StringProperty emailProperty() {
    return email;
  }

  public void setEmail(String email) {
    this.email.set(email);
  }

  public String getPhone() {
    return phone.get();
  }

  public StringProperty phoneProperty() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone.set(phone);
  }
}
