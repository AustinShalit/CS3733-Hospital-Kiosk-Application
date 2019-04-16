package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Urls {

  /**
   *   URLs for doing different things to the API.
   *   This initializes some dicts that contain country-unique information
   *   on how to interact with the API, and some getter methods for getting
   *   to that information. These are handy to pass as a first argument to
   *   pizzapi.utils.request_[xml|json].
   */
  public enum UrlCountryUsa {
    FIND_URL("https://order.dominos.com/power/store-locator?s={line1}&c={line2}&type={type}"),
    MENU_URL("https://order.dominos.com/power/store/{store_id}/profile"),
    PLACE_URL("https://order.dominos.com/power/store/{store_id}/menu?lang={lang}&structured=true"),
    PRICE_URL("https://order.dominos.com/power/place-order"),
    TRACK_BY_ORDER("https://trkweb.dominos.com/orderstorage/GetTrackerData?StoreID={store_id}&OrderKey={order_key}"),
    TRACK_BY_PHONE("https://trkweb.dominos.com/orderstorage/GetTrackerData?Phone={phone}"),
    VALIDATE_URL("https://order.dominos.com/power/validate-order"),
    COUPON_URL("https://order.dominos.com/power/store/{store_id}/coupon/{couponid}?lang={lang}");

    private static final Map<String, UrlCountryUsa> lookup = new ConcurrentHashMap<>();

    static {
      for (UrlCountryUsa url : values()) {
        lookup.put(url.name(), url);
      }
    }

    private final String url;

    UrlCountryUsa(final String name) {
      this.url = name;
    }

    @Override
    public String toString() {
      return url;
    }

    /**
     * Get the NodeType for the given string.
     */
    public static UrlCountryUsa get(final String name) {
      UrlCountryUsa type = lookup.get(name);
      if (type == null) {
        throw new IllegalArgumentException("Unknown node type: " + name);
      }
      return type;
    }

    public String find_url() {
      return FIND_URL.name();
    }

    public String menu_url() {
      return MENU_URL.name();
    }

    public String place_url() {
      return PLACE_URL.name();
    }

    public String price_url() {
      return PRICE_URL.name();
    }

    public String track_by_order() {
      return TRACK_BY_ORDER.name();
    }

    public String track_by_phone() {
      return TRACK_BY_PHONE.name();
    }

    public String validate_url() {
      return VALIDATE_URL.name();
    }

    public String coupon_url() {
      return COUPON_URL.name();
    }
  }

  // todo canada links

//  self.country = country
//  self.urls = {
//    COUNTRY_USA: {
//      'find_url' : 'https://order.dominos.com/power/store-locator?s={line1}&c={line2}&type={type}',
//          'info_url' : 'https://order.dominos.com/power/store/{store_id}/profile',
//          'menu_url' : 'https://order.dominos.com/power/store/{store_id}/menu?lang={lang}&structured=true',
//          'place_url' : 'https://order.dominos.com/power/place-order',
//          'price_url' : 'https://order.dominos.com/power/price-order',
//          'track_by_order' : 'https://trkweb.dominos.com/orderstorage/GetTrackerData?StoreID={store_id}&OrderKey={order_key}',
//          'track_by_phone' : 'https://trkweb.dominos.com/orderstorage/GetTrackerData?Phone={phone}',
//          'validate_url' : 'https://order.dominos.com/power/validate-order',
//          'coupon_url' : 'https://order.dominos.com/power/store/{store_id}/coupon/{couponid}?lang={lang}',
//    },
//    COUNTRY_CANADA: {
//      'find_url' : 'https://order.dominos.ca/power/store-locator?s={line1}&c={line2}&type={type}',
//          'info_url' : 'https://order.dominos.ca/power/store/{store_id}/profile',
//          'menu_url' : 'https://order.dominos.ca/power/store/{store_id}/menu?lang={lang}&structured=true',
//          'place_url' : 'https://order.dominos.ca/power/place-order',
//          'price_url' : 'https://order.dominos.ca/power/price-order',
//          'track_by_order' : 'https://trkweb.dominos.ca/orderstorage/GetTrackerData?StoreID={store_id}&OrderKey={order_key}',
//          'track_by_phone' : 'https://trkweb.dominos.ca/orderstorage/GetTrackerData?Phone={phone}',
//          'validate_url' : 'https://order.dominos.ca/power/validate-order',
//          'coupon_url' : 'https://order.dominos.ca/power/store/{store_id}/coupon/{couponid}?lang={lang}',
//    }
//  }

}
