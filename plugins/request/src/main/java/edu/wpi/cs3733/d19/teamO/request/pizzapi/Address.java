package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
  private final IntegerProperty zip = new SimpleIntegerProperty();
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
  public Store getNearbyStores() throws IOException {
    HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
    HttpRequest request = requestFactory.buildGetRequest(
        new GenericUrl(Country.USA.getFindUrl(getLineOne(), getLineTwo(), "Delivery")));
    String rawResponse = request.execute().parseAsString();
    return null;
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

  public int getZip() {
    return zip.get();
  }

  public IntegerProperty zipProperty() {
    return zip;
  }

  public void setZip(int zip) {
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
