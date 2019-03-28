package edu.wpi.cs3733d18.onyx_owlmen;

import java.util.LinkedList;

class Node {
  private String nodeID;
  private char color = 'w';
  private LinkedList<Node> adj;

  Node(String nodeID) {
    this.nodeID = nodeID;
    this.adj = new LinkedList<>();
  }

  String getNodeID() {
    return nodeID;
  }

  char getColor() {
    return color;
  }

  LinkedList<Node> getAdj() {
    return adj;
  }

  void setColor(char color) {
    this.color = color;
  }

  void setAdj(LinkedList<Node> adj) {
    this.adj = adj;
  }
}
