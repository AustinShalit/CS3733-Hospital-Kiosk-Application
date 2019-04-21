package edu.wpi.cs3733.d19.teamO.controller.event;

import edu.wpi.cs3733.d19.teamO.controller.Controller;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChangeMainViewEvent {

  private final Controller controller;
  private final boolean menuVisible;
  private final boolean adminView;

  public ChangeMainViewEvent(Controller controller) {
    this.controller = checkNotNull(controller);
    this.menuVisible = true;
    this.adminView = false;
  }

  public ChangeMainViewEvent(Controller controller, boolean menuVisible) {
    this.controller = controller;
    this.menuVisible = menuVisible;
    this.adminView = false;
  }

  public ChangeMainViewEvent(Controller controller, boolean menuVisible, boolean adminView) {
    this.controller = controller;
    this.menuVisible = menuVisible;
    this.adminView = adminView;
  }

  public Controller getController() {
    return controller;
  }

  public boolean isMenuVisible() {
    return menuVisible;
  }

  public boolean isAdminView() {
    return adminView;
  }
}

