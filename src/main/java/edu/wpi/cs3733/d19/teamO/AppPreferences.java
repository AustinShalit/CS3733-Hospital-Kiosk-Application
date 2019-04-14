package edu.wpi.cs3733.d19.teamO;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import edu.wpi.cs3733.d19.teamO.entity.pathfinding.GraphSearchAlgorithm;

public final class AppPreferences {

  private final ObjectProperty<GraphSearchAlgorithm> graphSearchAlgorithm
      = new SettingEnumObjectProperty<>(GraphSearchAlgorithm.BFS, GraphSearchAlgorithm.class);

  private final IntegerProperty test = new SimpleIntegerProperty(this, "");

  public PreferencesFx createPreferences() {
    PreferencesFx preferencesFx
        = PreferencesFx.of(AppPreferences.class, Category.of("App Settings",
        Group.of("Path Planning",
            Setting.of("Search Algorithm",
                FXCollections.observableArrayList(GraphSearchAlgorithm.values()),
                graphSearchAlgorithm),
            Setting.of("Test", test)
        )
    ));
    preferencesFx.saveSettings(true);
    return preferencesFx;
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

  public static class SettingEnumObjectProperty<T> extends SimpleObjectProperty<T> {
    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";

    private Class enumClass;

    /**
     * The constructor of {@code SettingEnumObjectProperty}
     *
     * @param initialValue the initial value of the wrapped value
     */
    public SettingEnumObjectProperty(T initialValue, Class<T> enumClass) {
      super(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
      this.enumClass = enumClass;
    }

    /**
     * It's a little bit tricky because to runtime T will be infered to Object, so that passing a
     * String is correct. Here the false T will be converted to a correct enum value.
     */
    @Override
    public void setValue(T value) {
      if (value instanceof String) {
        set((T) Enum.valueOf(enumClass, (String) value));
      }
    }
  }
}
