package edu.wpi.cs3733.d19.teamO.controller.v2.event;

import edu.wpi.cs3733.d19.teamO.controller.v2.Controller;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChangeMainViewEvent {

  private final Controller controller;
  private final boolean framed;

  public ChangeMainViewEvent(Controller controller) {
    this(controller, true);
  }

  public ChangeMainViewEvent(Controller controller, boolean framed) {
    this.controller = checkNotNull(controller);
    this.framed = framed;
  }

  public Controller getController() {
    return controller;
  }

  public boolean isFramed() {
    return framed;
  }
}
