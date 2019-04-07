package edu.wpi.cs3733.d19.teamO.controller.v2.event;

import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChangeMainViewEvent {

  private final URL fxml;
  private final boolean framed;

  public ChangeMainViewEvent(URL fxml) {
    this(fxml, true);
  }

  public ChangeMainViewEvent(URL fxml, boolean framed) {
    this.fxml = checkNotNull(fxml);
    this.framed = framed;
  }

  public URL getFxml() {
    return fxml;
  }

  public boolean isFramed() {
    return framed;
  }
}
