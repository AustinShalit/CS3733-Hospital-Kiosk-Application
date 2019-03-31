package edu.wpi.cs3733.d19.teamO;

public class Edge {
  private final String edgeID;
  private final String startNode;
  private final String endNode;

  /**
   * this creates an edge object and is not final.
   * @param edgeID the string that identifies the edge.
   * @param startNode the nodeID of the start node.
   * @param endNode the nodeID of the end node.
   */
  public Edge(String edgeID, String startNode, String endNode) {
    this.edgeID = edgeID;
    this.startNode = startNode;
    this.endNode = endNode;
  }

  public String getEdgeID() {
    return edgeID;
  }

  public String getStartNode() {
    return startNode;
  }

  public String getEndNode() {
    return endNode;
  }
}
