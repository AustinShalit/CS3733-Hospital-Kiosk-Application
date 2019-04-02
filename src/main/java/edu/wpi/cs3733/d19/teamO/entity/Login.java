package edu.wpi.cs3733.d19.teamO.entity;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class Login {
  private String username;
  private String password;

  //constructor
  public Login(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
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
    return Objects.hash(username, password);
  }
}
