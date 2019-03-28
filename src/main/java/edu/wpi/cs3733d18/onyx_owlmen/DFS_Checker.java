package edu.wpi.cs3733d18.onyx_owlmen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DFS_Checker {
    private LinkedList<Node> allNodes;
    private List<NodeCSV> allNodeCSV;
    private List<EdgeCSV> allEdgeCSV;
    private HashMap<String,Node> NodeIDHash;

    public DFS_Checker(List<NodeCSV> allNodeCSV, List<EdgeCSV> allEdgeCSV) {
        this.allNodeCSV = allNodeCSV;
        this.allEdgeCSV = allEdgeCSV;
    }

    public void createAllNodes() {

        LinkedList<Node> newNodeList = new LinkedList<Node>();
        for (NodeCSV aNodeCSV : allNodeCSV) {
            Node n = new Node(aNodeCSV.getNodeID());
            newNodeList.add(n);
            NodeIDHash.put(aNodeCSV.getNodeID(), n);
        }


        for (Node aNode: newNodeList){
            this.createAdjList(aNode);
        }

    }

    public void createAdjList(Node aNode){
        LinkedList<Node> adj = new LinkedList<Node>();
        for (EdgeCSV aEdgeCSV: allEdgeCSV){
            if (aNode.getNodeID() == aEdgeCSV.getStartNode()){
                adj.add(NodeIDHash.get(aEdgeCSV.getEndNode()));
            }
            else if (aNode.getNodeID() == aEdgeCSV.getEndNode()){
                adj.add(NodeIDHash.get(aEdgeCSV.getStartNode()));
            }
        }
        aNode.setAdj(adj);
    }


}
