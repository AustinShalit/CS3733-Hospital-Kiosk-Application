package edu.wpi.cs3733.d19.teamO.controller;

import java.util.HashMap;
import java.util.Map;

// Care Taker
public class ControllerCareTaker {


    private final Map<String, ControllerMemento> savepointStorage = new HashMap<>();

    public void saveMemento(ControllerMemento memento, String savepointName) {
        System.out.println("Saving state..." + savepointName);
        savepointStorage.put(savepointName, memento);
    }

    public ControllerMemento getMemento(String savepointName) {
        System.out.println("Undo at ..." + savepointName);
        return savepointStorage.get(savepointName);
    }

    public void clearSavepoints() {
        System.out.println("Clearing all save points...");
        savepointStorage.clear();
    }

}
