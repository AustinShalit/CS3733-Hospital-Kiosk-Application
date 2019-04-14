package edu.wpi.cs3733.d19.teamO.entity;

import java.util.Objects;

import com.google.gson.Gson;

public class EmployeeAttributes {
  boolean canFulfillSupportAnimal;
  Employee.EmployeeType employeeType;

  public EmployeeAttributes(boolean canFulfillSupportAnimal,
                            Employee.EmployeeType employeeType) {
    this.canFulfillSupportAnimal = canFulfillSupportAnimal;
    this.employeeType = employeeType;
  }

  public EmployeeAttributes() {
    this(false, Employee.EmployeeType.DEFAULT);
  }

  public EmployeeAttributes(Employee.EmployeeType employeeType) {
    this(false, employeeType);
  }

  public static EmployeeAttributes fromString(String from) {
    return new Gson().fromJson(from, EmployeeAttributes.class);
  }

  public boolean getCanFulfillSupportAnimal() {
    return canFulfillSupportAnimal;
  }

  public void setCanFulfillSupportAnimal(boolean canFulfillSupportAnimal) {
    this.canFulfillSupportAnimal = canFulfillSupportAnimal;
  }

  public Employee.EmployeeType getEmployeeType() {
    return employeeType;
  }

  public void setEmployeeType(Employee.EmployeeType employeeType) {
    this.employeeType = employeeType;
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmployeeAttributes that = (EmployeeAttributes) o;
    return canFulfillSupportAnimal == that.canFulfillSupportAnimal
        && employeeType.equals(that.employeeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(canFulfillSupportAnimal, employeeType);
  }
}
