package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * The interface to the Store API.
 *
 * You can use this to find store information about stores near an
 * address, or to find the closest store to an address.
 */
public class Store extends GenericJson {
/*
    def __init__(self, data={}, country=COUNTRY_USA):
        self.id = str(data.get('StoreID', -1))
        self.country = country
        self.urls = Urls(country)
        self.data = data

    def get_details(self):
        details = request_json(self.urls.info_url(), store_id=self.id)
        return details

    def get_menu(self, lang='en'):
        response = request_json(self.urls.menu_url(), store_id=self.id, lang=lang)
        menu = Menu(response, self.country)
        return menu
 */

  @Key
  private String id;

}
