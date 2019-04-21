package edu.wpi.cs3733.d19.teamO.controller;

//Memento
public class ControllerMemento {
    private final Controller state;
    private int elapsedTime;


    public ControllerMemento(Controller state) {
        this.state = state;
        elapsedTime = 0;
    }

    public Controller getState() {
        return state;
    }
}
