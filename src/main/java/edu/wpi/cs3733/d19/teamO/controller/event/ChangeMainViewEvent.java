package edu.wpi.cs3733.d19.teamO.controller.event;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.ControllerCareTaker;
import edu.wpi.cs3733.d19.teamO.controller.ControllerOriginator;

import java.util.Timer;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChangeMainViewEvent {

    private final Controller controller;
    private final boolean framed;
    ControllerOriginator idleTracker;
    static long start_time;
    static long end_time;
    static long elapsed_time;

    public ChangeMainViewEvent(Controller controller) {
        this(controller, true);
        idleTracker = new ControllerOriginator(controller, new ControllerCareTaker());
        idleTracker.createSavepoint(controller.getClass().getName());
        start_time = System.currentTimeMillis();
        end_time = System.currentTimeMillis();
        elapsed_time = 0;

    }

    public ChangeMainViewEvent(Controller controller, boolean framed) {
        this.controller = checkNotNull(controller);
        this.framed = framed;
        start_time = System.currentTimeMillis();
        end_time = System.currentTimeMillis();
        elapsed_time = 0;
    }

    public Controller getController() {
        return controller;
    }

    public boolean isFramed() {
        return framed;
    }

}
