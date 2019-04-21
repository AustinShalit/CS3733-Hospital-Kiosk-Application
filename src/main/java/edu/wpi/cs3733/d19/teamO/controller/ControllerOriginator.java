package edu.wpi.cs3733.d19.teamO.controller;

// Originator
public class ControllerOriginator {

    private Controller controller;
    private String lastUndoSavepoint;
    ControllerCareTaker careTaker;

    public ControllerOriginator(Controller controller, ControllerCareTaker careTaker) {
        this.controller = controller;
        this.careTaker = careTaker;

        createSavepoint("INITIAL");
    }


    public void createSavepoint(String savepointName) {
        careTaker.saveMemento(new ControllerMemento(this.controller), savepointName);
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
        ControllerMemento mem = careTaker.getMemento(savepointName);
        this.controller = mem.getState();
    }

}
