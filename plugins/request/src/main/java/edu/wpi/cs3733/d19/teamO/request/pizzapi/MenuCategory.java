package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class MenuCategory {

  private final ListProperty<MenuCategory> subcategories = new SimpleListProperty<>();
  private final ListProperty<MenuItem> products = new SimpleListProperty<>();
  private final ObjectProperty<MenuCategory> parent = new SimpleObjectProperty<>();
  private final StringProperty code = new SimpleStringProperty();
  private final StringProperty name = new SimpleStringProperty();

  public String getCategoryPath() {
    String path = parent.get() == null ? "" : parent.get().getCategoryPath();
    return path + code.get();
  }

  public ObservableList<MenuCategory> getSubcategories() {
    return subcategories.get();
  }

  public ListProperty<MenuCategory> subcategoriesProperty() {
    return subcategories;
  }

  public void setSubcategories(ObservableList<MenuCategory> subcategories) {
    this.subcategories.set(subcategories);
  }

  public ObservableList<MenuItem> getProducts() {
    return products.get();
  }

  public ListProperty<MenuItem> productsProperty() {
    return products;
  }

  public void setProducts(ObservableList<MenuItem> products) {
    this.products.set(products);
  }

  public MenuCategory getParent() {
    return parent.get();
  }

  public ObjectProperty<MenuCategory> parentProperty() {
    return parent;
  }

  public void setParent(MenuCategory parent) {
    this.parent.set(parent);
  }

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
}
