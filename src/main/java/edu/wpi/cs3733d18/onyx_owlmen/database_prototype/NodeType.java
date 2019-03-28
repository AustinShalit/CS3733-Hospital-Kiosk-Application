package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

public enum NodeType {
  CONF("Conference", "conf"),
  HALL("Hall", "hall"),
  DEPT("Department", "dept"),
  INFO("Information", "info"),
  LABS("Lab", "labs"),
  REST("Restroom", "rest"),
  SERV("Service", "serv"),
  STAI("Stai", "stai");

  final String name;
  final String shortName;

  NodeType(final String name, final String shortName) {
    this.name = name;
    this.shortName = shortName;
  }

  public String getPrintableName() {
    return name;
  }

  public String getShortName() {
    return shortName;
  }
}
