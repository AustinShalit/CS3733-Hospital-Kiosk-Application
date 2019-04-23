package edu.wpi.cs3733.d19.teamO;

import com.sun.javafx.application.LauncherImpl;

public final class Main {

	private Main() {
		throw new UnsupportedOperationException("This is a utility class!");
	}

	public static void main(final String... args) {
		LauncherImpl.launchApplication(Project.class, ProjectPreloader.class, args);
	}
}
