package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import edu.wpi.cs3733.d19.teamO.request.pizzapi.Store;
import edu.wpi.cs3733.d19.teamO.request.pizzapi.Utils;
import edu.wpi.cs3733.d19.teamO.request.pizzapi.Urls;

public class Address {
  /**
   *   Create an address, for finding stores and placing orders.
   *   The Address object describes a street address in North America (USA or
   *   Canada, for now). Callers can use the Address object's methods to find
   *   the closest or nearby stores from the API.
   *
   *   Attributes:
   *         street (String): Street address
   *         city (String): North American city
   *         region (String): North American region (state, province, territory)
   *         zip (String): North American ZIP code
   *         urls (String): Country-specific URLs
   *         country (String): Country
   */
  private final StringProperty street = new SimpleStringProperty();
  private final StringProperty city = new SimpleStringProperty();
  private final StringProperty region = new SimpleStringProperty();
  private final IntegerProperty zip = new SimpleIntegerProperty();
  private final Urls urls = new Urls();
  private final StringProperty country = new SimpleStringProperty();

  public String getStreet() {
    return street.get();
  }

  public StringProperty streetProperty() {
    return street;
  }

  public void setStreet(String street) {
    this.street.set(street);
  }

  public String getCity() {
    return city.get();
  }

  public StringProperty cityProperty() {
    return city;
  }

  public void setCity(String city) {
    this.city.set(city);
  }

  public String getRegion() {
    return region.get();
  }

  public StringProperty regionProperty() {
    return region;
  }

  public void setRegion(String region) {
    this.region.set(region);
  }

  public int getZip() {
    return zip.get();
  }

  public IntegerProperty zipProperty() {
    return zip;
  }

  public void setZip(int zip) {
    this.zip.set(zip);
  }

  public Urls getUrls() {
    return urls;
  }

  public String getCountry() {
    return country.get();
  }

  public StringProperty countryProperty() {
    return country;
  }

  public void setCountry(String country) {
    this.country.set(country);
  }
}
