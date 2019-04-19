package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import com.google.api.client.http.GenericUrl;

/**
 * URLs for doing different things to the API.
 * This initializes some dicts that contain country-unique information
 * on how to interact with the API, and some getter methods for getting
 * to that information. These are handy to pass as a first argument to
 * pizzapi.utils.request_[xml|json].
 */
public enum Country {
  USA("https://order.dominos.com/power/store-locator",
      "https://order.dominos.com/power/store/",
      "https://order.dominos.com/power/price-order",
      "https://order.dominos.com/power/place-order");

  private final String findBaseUrl;
  private final String menuBaseUrl;
  private final String priceUrl;
  private final String placeUrl;

  /**
   * Create a country object.
   */
  Country(String findBaseUrl, String menuBaseUrl, String priceUrl, String placeUrl) {
    this.findBaseUrl = findBaseUrl;
    this.menuBaseUrl = menuBaseUrl;
    this.priceUrl = priceUrl;
    this.placeUrl = placeUrl;
  }

  /**
   * Get the find url.
   */
  public GenericUrl getFindUrl(final String lineOne, final String lineTwo, final String type) {
    GenericUrl url = new GenericUrl(findBaseUrl);
    url.put("s", lineOne);
    url.put("c", lineTwo);
    url.put("type", type);
    return url;
  }

  /**
   * Get the url of the menu.
   */
  public GenericUrl getMenuUrl(final String storeId) {
    GenericUrl url = new GenericUrl(menuBaseUrl);
    url.appendRawPath(storeId);
    url.appendRawPath("/menu");
    url.put("structured", true);
    return url;
  }

  /**
   * Get the price url.
   */
  public GenericUrl getPriceUrl() {
    return new GenericUrl(priceUrl);
  }

  public GenericUrl getPlaceUrl() {
    return new GenericUrl(placeUrl);
  }
}
