package edu.wpi.cs3733.d19.teamO;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import edu.wpi.cs3733.d19.teamO.entity.Employee;

public final class GlobalState {
  private final ObjectProperty<Employee> loggedInEmployee = new SimpleObjectProperty<>();

  public Employee getLoggedInEmployee() {
    return loggedInEmployee.get();
  }

  public ObjectProperty<Employee> loggedInEmployeeProperty() {
    return loggedInEmployee;
  }

  public void setLoggedInEmployee(Employee loggedInEmployee) {
    this.loggedInEmployee.set(loggedInEmployee);
  }
}
