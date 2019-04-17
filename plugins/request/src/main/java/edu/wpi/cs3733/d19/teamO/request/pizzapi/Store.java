package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import java.io.IOException;
import java.util.stream.Collectors;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.common.base.MoreObjects;

import edu.wpi.cs3733.d19.teamO.request.pizzapi.response.FindResponse;

/**
 * The interface to the Store API.
 *
 * You can use this to find store information about stores near an
 * address, or to find the closest store to an address.
 */
public class Store extends GenericJson {

  @Key("StoreID")
  private String id;
  @Key("IsOnlineNow")
  private boolean open;

  public Menu getMenu() throws IOException {
    Menu reponse = Utilities.sendRequest(
        Country.USA.getMenuUrl(id),
        Menu.class);

    return reponse;
  }

  public String getId() {
    return id;
  }

  public boolean isOpen() {
    return open;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("open", open)
        .toString();
  }
}
