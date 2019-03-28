package edu.wpi.cs3733d18.onyx_owlmen;

import com.opencsv.bean.CsvBindByName;

public class EdgeCSV {
    @CsvBindByName
    private String edgeID;
    @CsvBindByName
    private String startNode;
    @CsvBindByName
    private String endNode;

    public String getEdgeID() {
        return edgeID;
    }

    public String getStartNode() {
        return startNode;
    }

    public String getEndNode() {
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
