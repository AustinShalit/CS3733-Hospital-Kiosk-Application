package edu.wpi.cs3733.d19.teamO.entity;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.MoreObjects;

public class Login {

  public enum EmployeeType {
    DEFAULT("Default"),
    ADMIN("Admin"),
    SANITATION("Sanitation"),
    SECURITY("Security");

    private static final Map<String, EmployeeType> lookup
        = new ConcurrentHashMap<>();

    static {
      for (Login.EmployeeType type : values()) {
        lookup.put(type.name(), type);
      }
    }

    private final String name;

    EmployeeType(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

    public static Login.EmployeeType get(final String name) {
      return lookup.get(name);
    }
  }

  private final Integer id;
  private final String username;
  private final String password;
  private final String user;
  private final EmployeeType type;

  /**
   * Constructor with associated employee known.
   *
   * @param id       Login's integer ID
   * @param username Username
   * @param password Password
   * @param user     User's Name
   * @param type     Position Type
   */
  public Login(Integer id, String username, String password, String user, EmployeeType type) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.user = user;
    this.type = type;
  }

  /**
   * Constructor with unknown employee.
   *
   * @param username Username
   * @param password Password
   * @param user     User's Name
   * @param type     Position Type
   */
  public Login(String username, String password, String user, EmployeeType type) {
    this.id = -1;
    this.username = username;
    this.password = password;
    this.user = user;
    this.type = type;
  }

  public Integer getId() { return id; }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getUser() { return user; }

  public EmployeeType getType() { return type; }


  @Override
  public String toString() {
    return "Login{"
        + "id=" + getId()
        + ", username=" + getUsername()
        + ", password=" + getPassword()
        + ", user=" + getUser()
        + ", type='" + type + '\''
        + '}';
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
    return getId() == that.getId()
        && username.equals(that.username)
        && password.equals(that.password)
        && user.equals(that.user)
        && type == that.type;
  }

  public boolean equalsLog(Object o) {
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
    return Objects.hash(id, username, password, user, type);
  }
}