package edu.wpi.cs3733.d19.teamO;

import java.util.prefs.Preferences;

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import edu.wpi.cs3733.d19.teamO.entity.pathfinding.GraphSearchAlgorithm;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.GraphSearchAlgorithms;
import edu.wpi.cs3733.d19.teamO.entity.setting.PreferencesUtilities;

public final class AppPreferences {

  private final ObjectProperty<GraphSearchAlgorithm> graphSearchAlgorithm
      = new SimpleObjectProperty<>();

  private final Category settings = Category.of("App Settings",
      Group.of("Path Planning",
          Setting.of("Search Algorithm", GraphSearchAlgorithms.getItems(), graphSearchAlgorithm)
      )
  );

  public AppPreferences() {
    Preferences preferences = Preferences.userNodeForPackage(getClass());
    PreferencesUtilities.read(graphSearchAlgorithm, preferences, GraphSearchAlgorithms::forName);

    graphSearchAlgorithm.addListener(observable -> PreferencesUtilities.save(graphSearchAlgorithm,
        preferences,
        algorithm -> algorithm.getClass().getName()));
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
}
