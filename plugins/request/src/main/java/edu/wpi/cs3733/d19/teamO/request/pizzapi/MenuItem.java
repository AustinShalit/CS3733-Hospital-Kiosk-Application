package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.common.base.MoreObjects;

public class MenuItem extends GenericJson {

  @Key("Code")
  private String code;
  @Key("Name")
  private String name;

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("code", code)
        .add("name", name)
        .toString();
  }
}
