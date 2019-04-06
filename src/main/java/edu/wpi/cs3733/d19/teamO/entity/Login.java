package edu.wpi.cs3733.d19.teamO.entity;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class Login {
  private final String username;
  private final String password;
  private final Employee employee;

  /**
   * Constructor with associated employee known.
   *
   * @param username Username
   * @param password Password
   * @param employee Employee
   */
  public Login(String username, String password, Employee employee) {
    this.username = username;
    this.password = password;
    this.employee = employee;
  }

  /**
   * Constructor with unknown employee.
   *
   * @param username Username
   * @param password Password
   */
  public Login(String username, String password) {
    this.username = username;
    this.password = password;
    this.employee = null;
  }


  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Employee getEmployee() {
    return employee;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("username", username)
        .add("password", password)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Login that = (Login) o;
    return username.equals(that.username)
        && password.equals(that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, employee);
  }
}
