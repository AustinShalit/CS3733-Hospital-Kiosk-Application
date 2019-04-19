package edu.wpi.cs3733.d19.teamO.request.pizzapi;

/**
 * The Customer orders a pizza.
 *
 * <p>You need a Customer to create an Order. The proprietors of the API
 * use this information, presumably for nefarious Pizza Purposes.
 * </p>
 */
public class Customer {

  private String firstName;
  private String lastName;
  private String email;
  private String phone;

  /**
   * Create a customer object.
   */
  public Customer(String firstName, String lastName, String email, String phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Override
  public String toString() {
    return "Customer{"
        + "firstName='" + firstName + '\''
        + ", lastName='" + lastName + '\''
        + ", email='" + email + '\''
        + ", phone='" + phone + '\''
        + '}';
  }
}
