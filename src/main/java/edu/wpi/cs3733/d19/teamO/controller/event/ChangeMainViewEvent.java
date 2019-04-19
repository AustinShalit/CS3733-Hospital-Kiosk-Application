package edu.wpi.cs3733.d19.teamO.controller.event;

import edu.wpi.cs3733.d19.teamO.controller.Controller;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChangeMainViewEvent {

  private final Controller controller;
  private boolean menuVisible;

  public ChangeMainViewEvent(Controller controller) {
    this.controller = checkNotNull(controller);
    this.menuVisible = true;
  }

  public ChangeMainViewEvent(Controller controller, boolean menuVisible) {
    this.controller = controller;
    this.menuVisible = menuVisible;
  }

  public Controller getController() {
    return controller;
  }

  public boolean isMenuVisible() {
    return menuVisible;
  }
}

