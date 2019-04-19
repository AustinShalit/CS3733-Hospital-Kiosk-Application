package edu.wpi.cs3733.d19.teamO.controller.event;

import edu.wpi.cs3733.d19.teamO.controller.Controller;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChangeMainViewEvent {

  private final Controller controller;

  public ChangeMainViewEvent(Controller controller) {
    this.controller = checkNotNull(controller);
  }

  public Controller getController() {
    return controller;
  }
}
