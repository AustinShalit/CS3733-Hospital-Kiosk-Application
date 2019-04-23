package edu.wpi.cs3733.d19.teamO.controller;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;

// Originator
public class IdleLogoutOriginator {
    private Timeline idleTimer;
    private String lastUndoSavepoint;
    private IdleLogoutCareTaker careTaker;

    public IdleLogoutOriginator(Timeline idleTimer, IdleLogoutCareTaker careTaker) {
        this.idleTimer = idleTimer;
        this.careTaker = careTaker;
        createSavepoint("INITIAL");
    }

    public void createSavepoint(String savepointName) {
        careTaker.saveMemento(new IdleLogoutMemento(idleTimer), savepointName);
        lastUndoSavepoint = savepointName;
    }

    public void undo() {
        setOriginatorState(lastUndoSavepoint);
    }

    public void undo(String savepointName) {
        setOriginatorState(savepointName);
    }

    public void undoAll() {
        setOriginatorState("INITIAL");
        careTaker.clearSavepoints();
    }

    private void setOriginatorState(String savepointName) {
        IdleLogoutMemento mem = careTaker.getMemento(savepointName);
        idleTimer = mem.getIdleTimer();
    }


}
