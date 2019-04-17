package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import edu.wpi.cs3733.d19.teamO.request.pizzapi.response.FindResponse;

/**
 * Create an address, for finding stores and placing orders.
 *
 * The Address object describes a street address in North America (USA or
 * Canada, for now). Callers can use the Address object's methods to find
 * the closest or nearby stores from the API.
 *
 * Attributes:
 *   street (String): Street address
 *   city (String): North American city
 *   region (String): North American region (state, province, territory)
 *   zip (String): North American ZIP code
 *   country (String): Country
 */
public class Address {

  private final StringProperty street = new SimpleStringProperty();
  private final StringProperty city = new SimpleStringProperty();
  private final StringProperty region = new SimpleStringProperty();
  private final StringProperty zip = new SimpleStringProperty();
  private final StringProperty country = new SimpleStringProperty();

  public String getLineOne() {
    return street.get();
  }

  public String getLineTwo() {
    return String.format("%s, %s, %s", city.get(), region.get(), zip.get());
  }

  /**
   * Query the API to find nearby stores.
   *
   * Filters the information from the API to exclude stores that are not
   * currently online (!['IsOnlineNow']), and stores that are not currently
   * in service (!['ServiceIsOpen']).
   */
  public List<Store> getNearbyStores() throws IOException {
    FindResponse response = Utilities.sendRequest(
        Country.USA.getFindUrl(getLineOne(), getLineTwo(), "Delivery"),
        FindResponse.class);

    return response.getStores().stream()
        .filter(Store::isOpen)
        .collect(Collectors.toList());
  }

  public Optional<Store> getClosestStore() throws IOException {
    List<Store> stores = getNearbyStores();

    if (stores.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(stores.get(0));
  }

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

  public String getZip() {
    return zip.get();
  }

  public StringProperty zipProperty() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip.set(zip);
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
