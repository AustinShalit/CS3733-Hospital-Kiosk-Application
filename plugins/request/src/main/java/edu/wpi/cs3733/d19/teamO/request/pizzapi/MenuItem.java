package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class MenuItem {

  private final StringProperty code = new SimpleStringProperty();
  private final StringProperty name = new SimpleStringProperty();
  private final ListProperty<MenuCategory> categories = new SimpleListProperty<>();

  public String getCode() {
    return code.get();
  }

  public StringProperty codeProperty() {
    return code;
  }

  public void setCode(String code) {
    this.code.set(code);
  }

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public Object getCategories() {
    return categories.get();
  }

  public ListProperty categoriesProperty() {
    return categories;
  }

  public void setCategories(ObservableList<MenuCategory> categories) {
    this.categories.set(categories);
  }
}
