package edu.wpi.cs3733.d19.teamO;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import edu.wpi.cs3733.d19.teamO.entity.pathfinding.GraphSearchAlgorithm;

public final class AppPreferences {

  private final ObjectProperty<GraphSearchAlgorithm> graphSearchAlgorithm
      = new SimpleObjectProperty<>(this,
      "graphSearchAlgorithm",
      GraphSearchAlgorithm.BFS);

  public PreferencesFx createPreferences() {
    return PreferencesFx.of(AppPreferences.class, Category.of("App Settings",
        Group.of("Path Planning",
            Setting.of("Search Algorithm",
                FXCollections.observableArrayList(GraphSearchAlgorithm.values()),
                graphSearchAlgorithm)
        )
    ));
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
