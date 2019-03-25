package edu.wpi.cs3733d18.onyx_owlmen;

import java.util.LinkedList;

public class Database {
    public Database() {

    }


    LinkedList<Node> getNodes() {
        LinkedList<Node> ll = new LinkedList<>();
        ll.add(new Node(
            "BCONF00102",
                2150,
                1025,
                2,
                "45 Francis",
                NodeType.CONF,
                "Duncan Reid Conference Room",
                "Conf B0102"
        ));
        ll.add(new Node(
            "BHALL03802",
                2279,
                786,
                2,
                "45 Francis",
                NodeType.HALL,
                "Hallway Intersection 38 Level 2",
                "Hallway B3802"
        ));
        return ll;
    }

    void updateNode(String nodeId, Node newNode) {
        //
    }
}
