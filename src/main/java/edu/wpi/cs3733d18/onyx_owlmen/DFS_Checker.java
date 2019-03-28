package edu.wpi.cs3733d18.onyx_owlmen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class DFS_Checker {
    private LinkedList<Node> allNodes;
    private List<NodeCSV> allNodeCSV;
    private List<EdgeCSV> allEdgeCSV;
    private HashMap<String,Node> NodeIDHash;

    DFS_Checker(List<NodeCSV> allNodeCSV, List<EdgeCSV> allEdgeCSV) {
        this.allNodeCSV = allNodeCSV;
        this.allEdgeCSV = allEdgeCSV;
        this.NodeIDHash = new HashMap<String, Node>();
        this.allNodes = new LinkedList<Node>();
    }

    void createAllNodes() {

        LinkedList<Node> newNodeList = new LinkedList<Node>();
        for (NodeCSV aNodeCSV : allNodeCSV) {
            Node n = new Node(aNodeCSV.getNodeID());
            newNodeList.add(n);
            NodeIDHash.put(aNodeCSV.getNodeID(), n);
        }


        for (Node aNode: newNodeList){
            this.createAdjList(aNode);
        }
        this.allNodes = newNodeList;

    }

    private void createAdjList(Node aNode){
        LinkedList<Node> adj = new LinkedList<Node>();
        for (EdgeCSV aEdgeCSV: allEdgeCSV){
            if (aNode.getNodeID().equals(aEdgeCSV.getStartNode())){
                adj.add(NodeIDHash.get(aEdgeCSV.getEndNode()));
            }
            else if (aNode.getNodeID().equals(aEdgeCSV.getEndNode())){
                adj.add(NodeIDHash.get(aEdgeCSV.getStartNode()));
            }
        }
        aNode.setAdj(adj);
    }

    void runDFS(String rootID){
        Node root = NodeIDHash.get(rootID);
        root.setColor('g');

        for (Node aNode: root.getAdj()){
            if (aNode.getColor() == 'w'){
                this.runDFS(aNode.getNodeID());
            }
        }
        root.setColor('b');
    }

    boolean checkDFS(){
        int wrongCount = 0;
        for (Node aNode: this.allNodes){
            if(aNode.getColor() != 'b'){
                System.out.println(aNode);
                wrongCount++;
            }
        }
        if (wrongCount != 0){
            return false;
        }
        else {
            return true;
        }
    }

}
