package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import org.junit.jupiter.api.Test;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;

import static org.junit.jupiter.api.Assertions.*;

class StepByStepTest {

  final Node NODE_A = new Node("A", 0, 0, "0", "B", edu.wpi.cs3733.d19.teamO.entity.Node.NodeType.HALL,
        "AL", "AS");
  final Node NODE_B = new Node("B", 1, 0, "0", "B", Node.NodeType.STAI,
      "BL", "BS");
  final Node NODE_C = new Node("C", 2, 1, "0", "B", Node.NodeType.STAI,
      "CL", "CS");
  final Node NODE_D = new Node("D", 2, 2, "0", "B", edu.wpi.cs3733.d19.teamO.entity.Node.NodeType.HALL,
      "CL", "CS");
  final Node NODE_E = new Node("E", 1, 1, "0", "B", edu.wpi.cs3733.d19.teamO.entity.Node.NodeType.HALL,
      "CL", "CS");
  final Node NODE_F = new Node("F", 1, 3, "0", "B", edu.wpi.cs3733.d19.teamO.entity.Node.NodeType.HALL,
      "CL", "CS");
  final Node NODE_G = new Node("G", 2, 3, "0", "B", edu.wpi.cs3733.d19.teamO.entity.Node.NodeType.HALL,
      "CL", "CS");
  final Node NODE_H = new Node("H", 2, 4, "0", "B", edu.wpi.cs3733.d19.teamO.entity.Node.NodeType.HALL,
      "CL", "CS");
  final Node NODE_I = new Node("I", 3, 5, "0", "B", edu.wpi.cs3733.d19.teamO.entity.Node.NodeType.HALL,
      "CL", "CS");

  final Edge ab = new Edge("ab", NODE_A, NODE_B);
  final Edge bc = new Edge("bc", NODE_B, NODE_C);
  final Edge cd = new Edge("cd", NODE_C, NODE_D);



  @Test
  void getStepByStep() {




  }
}