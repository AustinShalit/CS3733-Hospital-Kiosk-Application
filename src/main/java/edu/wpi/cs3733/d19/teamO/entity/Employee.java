package edu.wpi.cs3733.d19.teamO.entity;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Employee {

  public enum EmployeeType {
    DEFAULT("Default"),
    ADMIN("Admin"),
    SANITATION("Sanitation"),
    SECURITY("Security");

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

  private final Integer id;
  private final String name;
  private final EmployeeType type;
  private EmployeeAttributes employeeAttributes;

  /**
   * Constructor for Employee.
   *
   * @param id   Employee's integer ID
   * @param name Employee name
   * @param type Permissions type
   */
  public Employee(Integer id,
                  String name,
                  EmployeeType type,
                  EmployeeAttributes employeeAttributes) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.employeeAttributes = employeeAttributes;
  }

  /**
   * Constructor for Employee.
   *
   * @param id   Employee's integer ID
   * @param name Employee name
   * @param type Permissions type
   */
  public Employee(Integer id,
                  String name,
                  EmployeeType type) {
    this(id, name, type, new EmployeeAttributes(false));
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public EmployeeType getType() {
    return type;
  }

  public EmployeeAttributes getEmployeeAttributes() {
    return employeeAttributes;
  }

  @Override
  public String toString() {
    return this.name + ": " + this.type.toString();
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
    return id.equals(that.id)
        && name.equals(that.name)
        && type.equals(that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, type);
  }
}
