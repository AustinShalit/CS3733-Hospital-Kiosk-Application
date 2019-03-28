package edu.wpi.cs3733d18.onyx_owlmen;

import com.opencsv.bean.CsvBindByName;

public class EdgeCSV {
    @CsvBindByName
    private String edgeID;
    @CsvBindByName
    private String startNode;
    @CsvBindByName
    private String endNode;

    String getEdgeID() {
        return edgeID;
    }

    String getStartNode() {
        return startNode;
    }

    String getEndNode() {
        return endNode;
    }

    @Override
    public String toString() {
        return "EdgeCSV{" +
                "edgeID='" + edgeID + '\'' +
                ", startNode='" + startNode + '\'' +
                ", endNode='" + endNode + '\'' +
                '}';
    }
}
