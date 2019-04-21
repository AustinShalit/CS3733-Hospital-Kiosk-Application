package edu.wpi.cs3733.d19.teamO.entity;

import com.google.common.base.MoreObjects;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class Edge {

    private final String edgeId;
    private final Node startNode;
    private final Node endNode;

    /**
     * Create an Edge.
     */
    public Edge(String edgeId, Node startNode, Node endNode) {
        this.edgeId = checkNotNull(edgeId);
        this.startNode = checkNotNull(startNode);
        this.endNode = checkNotNull(endNode);

        if (startNode.equals(endNode)) {
            throw new IllegalArgumentException("The start and end nodes must be different");
        }
    }

    public String getEdgeId() {
        return edgeId;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("edgeId", edgeId)
                .add("startNode", startNode)
                .add("endNode", endNode)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return Objects.equals(edgeId, edge.edgeId)
                && Objects.equals(startNode, edge.startNode)
                && Objects.equals(endNode, edge.endNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edgeId, startNode, endNode);
    }
}
