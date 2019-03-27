package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

public enum NodeType {
  CONF("Conference"),
  HALL("Hall"),
  DEPT("Department"),
  INFO("Information"),
  LABS("Lab"),
  REST("Restroom"),
  SERV("Service"),
  STAI("Stai");

  final String name;

  NodeType(final String name) {
    this.name = name;
  }

  public String getPrintableName() {
    return name;
  }
}
