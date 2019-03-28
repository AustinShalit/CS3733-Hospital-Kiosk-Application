package edu.wpi.cs3733d18.onyx_owlmen;

import com.opencsv.bean.CsvBindByName;

public class NodeCSV {
  @CsvBindByName
  private String nodeID;
  @CsvBindByName
  private int xcoord;
  @CsvBindByName
  private int ycoord;
  @CsvBindByName
  private String floor;
  @CsvBindByName
  private String building;
  @CsvBindByName
  private String nodeType;
  @CsvBindByName
  private String longName;
  @CsvBindByName
  private String shortName;

  String getNodeID() {
    return nodeID;
  }

  int getXcoord() {
    return xcoord;
  }

  int getYcoord() {
    return ycoord;
  }

  String getFloor() {
    return floor;
  }

  String getBuilding() {
    return building;
  }

  String getNodeType() {
    return nodeType;
  }

  String getLongName() {
    return longName;
  }

  String getShortName() {
    return shortName;
  }

  @Override
  public String toString() {
    return "NodeCSV{" +
        "nodeID='" + nodeID + '\'' +
        ", xcoord=" + xcoord +
        ", ycoord=" + ycoord +
        ", floor='" + floor + '\'' +
        ", building='" + building + '\'' +
        ", nodeType='" + nodeType + '\'' +
        ", longName='" + longName + '\'' +
        ", shortName='" + shortName + '\'' +
        '}';
  }
}
