package edu.wpi.cs3733d18.onyx_owlmen;

public class Node {
    String nodeID;
    int xcoord;
    int ycoord;
    int floor;
    String building;
    NodeType nodeType;
    String longName;
    String shortName;

    public Node(String nodeID, int xcoord, int ycoord, int floor, String building, NodeType nodeType, String longName, String shortName) {
        this.nodeID = nodeID;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
    }
}
