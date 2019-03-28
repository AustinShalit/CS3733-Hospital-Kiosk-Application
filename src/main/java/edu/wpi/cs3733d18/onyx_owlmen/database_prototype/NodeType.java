package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum NodeType {
  CONF("Conference"),
  HALL("Hall"),
  DEPT("Department"),
  INFO("Information"),
  LABS("Lab"),
  REST("Restroom"),
  SERV("Service"),
  STAI("Stair Case");

  private static final Map<String, NodeType> lookup = new ConcurrentHashMap<>();

  static {
    for (NodeType type : values()) {
      lookup.put(type.name(), type);
    }
  }

  private final String name;

  NodeType(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  public static NodeType get(String name) {
    return lookup.get(name);
  }
}

