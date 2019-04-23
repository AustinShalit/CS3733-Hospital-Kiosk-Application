package edu.wpi.cs3733.d19.teamO.controller;

import java.util.HashMap;
import java.util.Map;

// Care Taker
public class IdleLogoutCareTaker {


	private final Map<String, IdleLogoutMemento> savepointStorage = new HashMap<String, IdleLogoutMemento>();

	public void saveMemento(IdleLogoutMemento memento, String savepointName) {
		System.out.println("Saving state..." + savepointName);
		savepointStorage.put(savepointName, memento);
	}

	public IdleLogoutMemento getMemento(String savepointName) {
		System.out.println("Undo at ..." + savepointName);
		return savepointStorage.get(savepointName);
	}

	public void clearSavepoints() {
		System.out.println("Clearing all save points...");
		savepointStorage.clear();
	}

}
