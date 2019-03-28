package edu.wpi.cs3733d18.onyx_owlmen;

import java.util.ArrayList;
import java.util.LinkedList;

public class Node {
    private String nodeID;
    private char color = 'w';
    private LinkedList<Node> adj;

    public Node(String nodeID) {
        this.nodeID = nodeID;
        this.adj = adj;
    }

    public String getNodeID() {
        return nodeID;
    }

    public char getColor() {
        return color;
    }

    public LinkedList<Node> getAdj() {
        return adj;
    }

    public void setColor(char color) {
        this.color = color;
    }

    public void setAdj(LinkedList<Node> adj) {
        this.adj = adj;
    }
}
