package edu.wpi.cs3733.d19.teamO.theme;

import java.util.Objects;

import javafx.collections.ObservableList;

import edu.wpi.cs3733.d19.teamO.util.Registry;

/**
 * Keeps track of the themes available to the application.
 */
public final class Themes extends Registry<Theme> {

  // TODO replace with DI eg Guice
  private static Themes defaultInstance;

  public static final Theme BW_THEME = new Theme("B&W Theme",
      "/edu/wpi/cs3733/d19/teamO/controller/style/light.css");

  public static final Theme INITIAL_THEME = BW_THEME;

  /**
   * Gets the default themes instance.
   */
  public static Themes getDefault() {
    synchronized (Themes.class) {
      if (defaultInstance == null) {
        defaultInstance = new Themes(BW_THEME);
      }
    }
    return defaultInstance;
  }

  /**
   * Creates a new theme registry.
   *
   * @param initial the initial themes
   */
  public Themes(Theme... initial) {
    registerAll(initial);
  }

  /**
   * Gets the theme with the given name. If there is no theme with that name, returns
   * {@link #INITIAL_THEME} instead.
   *
   * @param name the name of the theme to get
   */
  public Theme forName(String name) {
    return getItems().stream()
        .filter(t -> t.getName().equals(name))
        .findFirst()
        .orElse(INITIAL_THEME);
  }

  @Override
  public void register(Theme theme) {
    Objects.requireNonNull(theme, "theme");
    if (isRegistered(theme)) {
      throw new IllegalArgumentException("Theme " + theme + " is already registered");
    }
    addItem(theme);
  }

  @Override
  public void unregister(Theme theme) {
    removeItem(theme);
  }

  /**
   * Gets an observable list of the registered themes.
   */
  public ObservableList<Theme> getThemes() {
    return getItems();
  }
}
