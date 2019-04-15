package edu.wpi.cs3733.d19.teamO;

import java.util.prefs.Preferences;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

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

  private final Property<Theme> theme
      = new SimpleObjectProperty<>(this, "Theme", Themes.INITIAL_THEME);

  private final Category settings = Category.of("App Settings",
      Group.of("Theme",
          Setting.of("Theme", "The theme to display Shuffleboard with", theme)
      )
  );

  /**
   * Creates a new app preferences instance.
   */
  public AppPreferences() {
    Preferences preferences = Preferences.userNodeForPackage(getClass());
    PreferencesUtils.read(theme, preferences, Themes.getDefault()::forName);

    theme.addListener(__ -> PreferencesUtils.save(theme, preferences, Theme::getName));
  }

  public Category getSettings() {
    return settings;
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
}