package edu.wpi.cs3733.d19.teamO.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.jfoenix.controls.JFXComboBox;

import me.xdrop.fuzzywuzzy.FuzzySearch;

import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.entity.Node;

public class FuzzyWuzzyComboBox extends JFXComboBox<String> {
  private Collection<Node> nodes;

  public void setNodes(Collection<Node> nodes) {
    this.nodes = nodes;
  }

  public void setupAutoRefresh() {
    this.setOnKeyReleased(event -> refresh());
  }

  public void refresh() {
    String query = this.getValue();
    if (query == null) {
      query = "";
    }

    ArrayList<Pair> unsorted = new ArrayList<>();
    for (Node n : nodes) {
      unsorted.add(
          new Pair(
              n.getLongName(),
              n.getFloor(),
              FuzzySearch.ratio(n.getLongName(), query)
          )
      );
    }
    Collections.sort(unsorted);
    ArrayList<String> sortedStrings = new ArrayList<>();
    for (Pair p : unsorted) {
      sortedStrings.add(toDisplayName(p));
    }
    DialogHelper.populateComboBox2(null, this, sortedStrings);
  }

  public Node getNodeValue() {
    String query = this.getValue();
    if (query == null) {
      query = "";
    }

    for (Node n: nodes) {
      if (toDisplayName(n).equals(query)) {
        return n;
      }
    }
    return null;
  }

  private String toDisplayName(Pair p) {
    return String.format("%s -- FLOOR %s", p.longname, p.floor);
  }

  private String toDisplayName(Node n) {
    return String.format("%s -- FLOOR %s", n.getLongName(), n.getFloor());
  }

  class Pair implements Comparable<Pair> {
    String longname;
    String floor;
    int rating;

    Pair(String longname, String floor, int rating) {
      this.longname = longname;
      this.floor = floor;
      this.rating = rating;
    }

    @Override
    public int compareTo(Pair p) {
      return -1 * Integer.compare(this.rating, p.rating);
    }
  }
}
