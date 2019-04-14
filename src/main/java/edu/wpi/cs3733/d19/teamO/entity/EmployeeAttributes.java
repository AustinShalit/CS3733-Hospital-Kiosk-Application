package edu.wpi.cs3733.d19.teamO.entity;

import com.google.gson.Gson;

public class EmployeeAttributes {
  boolean canFulfillSupportAnimal;

  public EmployeeAttributes(boolean canFulfillSupportAnimal) {
    this.canFulfillSupportAnimal = canFulfillSupportAnimal;
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
