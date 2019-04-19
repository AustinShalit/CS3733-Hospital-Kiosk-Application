package edu.wpi.cs3733.d19.teamO.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;

import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class FuzzyWuzzyComboBox extends JFXComboBox<String> {
  private Collection<Node.NodeType> nodeTypesToInclude;

  @Inject
  private Database database;

  public void refresh() {
    DialogHelper.populateComboBox2(database, this, Arrays.asList("BTM Security Desk"));
  }
}
