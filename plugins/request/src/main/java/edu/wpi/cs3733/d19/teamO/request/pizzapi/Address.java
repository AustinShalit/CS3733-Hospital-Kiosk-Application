package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.util.Key;

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

  @Key("Street")
  private String street;
  @Key("City")
  private String city;
  @Key("Region")
  private String region;
  @Key("PostalCode")
  private String zip;
  @Key("Type")
  private String type = "House";

  public Address() {

  }

  public Address(String street, String city, String region, String zip) {
    this.street = street;
    this.city = city;
    this.region = region;
    this.zip = zip;
  }

  public String getLineOne() {
    return street;
  }

  public String getLineTwo() {
    return String.format("%s, %s, %s", city, region, zip);
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * Query the API to find nearby stores.
   *
   * Filters the information from the API to exclude stores that are not
   * currently online (!['IsOnlineNow']), and stores that are not currently
   * in service (!['ServiceIsOpen']).
   */
  public List<Store> getNearbyStores() throws IOException {
    FindResponse response = Utilities.sendGetRequest(
        Country.USA.getFindUrl(getLineOne(), getLineTwo(), "Delivery"),
        FindResponse.class);

    return response.getStores().stream()
        //.filter(Store::isOpen)
        .collect(Collectors.toList());
  }

  public Optional<Store> getClosestStore() throws IOException {
    List<Store> stores = getNearbyStores();

    if (stores.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(stores.get(0));
  }
}
