package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.util.List;

public class Database {
  List<Node> ll;

  public Database() {
    CSVReader csvReader = new CSVReader();
    ll = csvReader.getNodesInCSV();
  }


  List<Node> getNodes() {
    return ll;
  }

  void updateNode(String nodeId, Node newNode) {
    //
  }

  void deleteNode(String nodeId) {
    //
  }
}
