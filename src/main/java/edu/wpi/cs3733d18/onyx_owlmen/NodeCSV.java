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

    public String getNodeID() {
        return nodeID;
    }

    public int getXcoord() {
        return xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

    public String getFloor() {
        return floor;
    }

    public String getBuilding() {
        return building;
    }

    public String getNodeType() {
        return nodeType;
    }

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
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
