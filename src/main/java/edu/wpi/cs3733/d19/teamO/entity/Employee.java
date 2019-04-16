package edu.wpi.cs3733.d19.teamO.entity;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Employee {

  private final int id;
  private String username;
  private String password;
  private String name;
  private EmployeeAttributes employeeAttributes;

  /**
   * Constructor for Employee, Create a Employee.
   *
   * @param id       Employee's integer ID
   * @param username Username
   * @param password Password
   * @param name     User's Name
   */
  public Employee(int id,
                  String username,
                  String password,
                  String name,
                  EmployeeAttributes employeeAttributes) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.name = name;
    this.employeeAttributes = employeeAttributes;
  }

  /**
   * Constructor for Employee.
   *
   * @param id       Employee's integer ID
   * @param username Username
   * @param password Password
   * @param name     User's Name
   */
  public Employee(int id,
                  String username,
                  String password,
                  String name) {
    this(id, username, password, name, new EmployeeAttributes());
  }

  /**
   * Constructor for Employee, Initialization of Employee.
   *
   * @param username Username
   * @param password Password
   * @param name     User's Name
   */
  public Employee(String username,
                  String password,
                  String name) {
    this(-1, username, password, name, new EmployeeAttributes());
  }

  /**
   * Constructor for Employee, Initialization of Employee.
   *
   * @param username     Username
   * @param password     Password
   * @param name         User's Name
   * @param employeeType The type of the employee
   */
  public Employee(String username,
                  String password,
                  String name,
                  EmployeeType employeeType) {
    this(-1, username, password, name, new EmployeeAttributes(employeeType));
  }

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getName() {
    return name;
  }

  public EmployeeType getType() {
    return employeeAttributes.employeeType;
  }

  public EmployeeAttributes getEmployeeAttributes() {
    return employeeAttributes;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setType(EmployeeType type) {
    this.employeeAttributes.employeeType = type;
  }

  @Override
  public String toString() {
    return "Employee{"
        + "id=" + getId()
        + ", username=" + getUsername()
        + ", password=" + getPassword()
        + ", user=" + getName()
        + ", attr=" + getEmployeeAttributes().toString()
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
    Employee that = (Employee) o;
    return getId() == that.getId()
        && username.equals(that.username)
        && password.equals(that.password)
        && name.equals(that.name)
        && employeeAttributes.equals(that.employeeAttributes);
  }

  /**
   * Function to check the username and password.
   *
   * @param o Object
   */
  public boolean loginEquals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Employee that = (Employee) o;
    return username.equals(that.username)
        && password.equals(that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, name, employeeAttributes);
  }

  public enum EmployeeType {
    DEFAULT("Default"),
    ADMIN("Admin");

    private static final Map<String, EmployeeType> lookup
        = new ConcurrentHashMap<>();

    static {
      for (Employee.EmployeeType type : values()) {
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

    public static Employee.EmployeeType get(final String name) {
      return lookup.get(name);
    }
  }
}
