package edu.wpi.cs3733.d19.teamO.controller;

import javafx.animation.Timeline;

//Memento
public class IdleLogoutMemento {
	private Timeline idleTimer;

	public IdleLogoutMemento(Timeline idleTimer) {
		this.idleTimer = idleTimer;
	}

	public Timeline getIdleTimer() {
		return idleTimer;
	}
}
