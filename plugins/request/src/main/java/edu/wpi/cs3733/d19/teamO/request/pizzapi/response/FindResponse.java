package edu.wpi.cs3733.d19.teamO.request.pizzapi.response;

import java.util.List;

import com.google.api.client.util.Key;

import edu.wpi.cs3733.d19.teamO.request.pizzapi.Store;

public class FindResponse {

  @Key("Stores")
  private List<Store> stores;

  public List<Store> getStores() {
    return stores;
  }
}
