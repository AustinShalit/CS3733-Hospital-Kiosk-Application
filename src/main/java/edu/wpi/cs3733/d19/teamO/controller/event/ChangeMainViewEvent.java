package edu.wpi.cs3733.d19.teamO.controller.event;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.ControllerCareTaker;
import edu.wpi.cs3733.d19.teamO.controller.ControllerOriginator;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChangeMainViewEvent {

    private final Controller controller;
    private final boolean framed;
    ControllerOriginator idleTracker;

    public ChangeMainViewEvent(Controller controller) {
        this(controller, true);
        idleTracker = new ControllerOriginator(controller, new ControllerCareTaker());
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

    private class IdleUserException extends Exception {
        public IdleUserException() {
        }
    }
}
