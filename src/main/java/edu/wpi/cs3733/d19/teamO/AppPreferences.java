package edu.wpi.cs3733.d19.teamO;

import java.util.prefs.Preferences;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import edu.wpi.cs3733.d19.teamO.entity.pathfinding.GraphSearchAlgorithm;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.GraphSearchAlgorithms;
import edu.wpi.cs3733.d19.teamO.entity.setting.Category;
import edu.wpi.cs3733.d19.teamO.entity.setting.Group;
import edu.wpi.cs3733.d19.teamO.entity.setting.PreferencesUtils;
import edu.wpi.cs3733.d19.teamO.entity.setting.Setting;
import edu.wpi.cs3733.d19.teamO.theme.Theme;
import edu.wpi.cs3733.d19.teamO.theme.Themes;


/**
 * Contains the user preferences for the app.
 */
public final class AppPreferences {

	private final Property<GraphSearchAlgorithm> graphSearchAlgorithm
			= new SimpleObjectProperty<>(this, "Graph Search Algorithm",
			GraphSearchAlgorithms.INITIAL_ALGORITHM);
	private final Property<Theme> theme
			= new SimpleObjectProperty<>(this, "Theme", Themes.INITIAL_THEME);
	private final DoubleProperty autoLogoutTime =
			new SimpleDoubleProperty(this, "autoLogoutTime", 30.0);

	private final Category settings = Category.of("App Settings",
			Group.of("Global",
					Setting.of("Graph Search Algorithm",
							"The search algorithm to use while navigating",
							graphSearchAlgorithm),
					Setting.of("Theme", "The theme to display with", theme),
					Setting.of("Idle Logout Time",
							"The amount of time before an idle kiosk returns to the default screen",
							autoLogoutTime)
			)
	);

	/**
	 * Creates a new app preferences instance.
	 */
	public AppPreferences() {
		Preferences preferences = Preferences.userNodeForPackage(getClass());
		PreferencesUtils.read(graphSearchAlgorithm, preferences,
				GraphSearchAlgorithms.getDefault()::forName);
		PreferencesUtils.read(theme, preferences, Themes.getDefault()::forName);
		PreferencesUtils.read(autoLogoutTime, preferences);

		graphSearchAlgorithm.addListener(__ -> PreferencesUtils.save(graphSearchAlgorithm, preferences,
				GraphSearchAlgorithm::getName));
		theme.addListener(__ -> PreferencesUtils.save(theme, preferences, Theme::getName));
		autoLogoutTime.addListener(__ -> PreferencesUtils.save(autoLogoutTime, preferences));
	}

	public Category getSettings() {
		return settings;
	}

	public GraphSearchAlgorithm getGraphSearchAlgorithm() {
		return graphSearchAlgorithm.getValue();
	}

	public Property<GraphSearchAlgorithm> graphSearchAlgorithmProperty() {
		return graphSearchAlgorithm;
	}

	public void setGraphSearchAlgorithm(GraphSearchAlgorithm graphSearchAlgorithm) {
		this.graphSearchAlgorithm.setValue(graphSearchAlgorithm);
	}

	public Property<Theme> themeProperty() {
		return theme;
	}

	public Theme getTheme() {
		return theme.getValue();
	}

	public void setTheme(Theme theme) {
		this.theme.setValue(theme);
	}

	public double getAutoLogoutTime() {
		return autoLogoutTime.getValue();
	}

	public DoubleProperty autoLogoutTimeProperty() {
		return autoLogoutTime;
	}

	public void setAutoLogoutTime(double time) {
		this.autoLogoutTime.setValue(time);
	}
}
