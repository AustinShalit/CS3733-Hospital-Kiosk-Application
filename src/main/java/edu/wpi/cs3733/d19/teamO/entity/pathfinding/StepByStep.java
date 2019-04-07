package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.ArrayList;
import java.util.Stack;

import edu.wpi.cs3733.d19.teamO.entity.Node;

public class StepByStep {

  public StepByStep() {
  }

  public String getDirection(Coordinate start, Coordinate end) {
    int xDif = end.getxCoord() - start.getxCoord();
    int ydif = start.getyCoord() - end.getyCoord();

    double theta = Math.atan2(xDif, ydif) + 1.5708;


    if (theta < .78 || theta > 5.49779) {
      return "North";
    } else if (theta >= .78 && theta <= 2.35) {
      return "West";
    } else if (theta > 2.35 && theta <= 3.92699) {
      return "South";
    } else if (theta > 3.92699 && theta <= 5.49779) {
      return "East";
    }

    return "";
  }

  public ArrayList<String> getStepByStep(Stack<Node> nodes) {
    ArrayList<String> instructions = new ArrayList<>();

    ArrayList<Coordinate> coordinates= new ArrayList<>();

    for (Node node: nodes) {
      Coordinate newCoord = new Coordinate(nodes.peek().getXcoord(),nodes.peek().getYcoord());
      coordinates.add(newCoord);
    }

    ArrayList<String> cardinalDirections = new ArrayList<>();

    for (int i = 0; i < coordinates.size() -1; i++) {
      String direction = getDirection(coordinates.get(i), coordinates.get(i+1));
      cardinalDirections.add(direction);
    }

    for (int i = 0; i < cardinalDirections.size() -1; i++) {
      String current = cardinalDirections.get(i);
      String next = cardinalDirections.get(i+1);

      if (current.equals("North")) {
        if (next.equals("East")) {
          instructions.add("Right");
        } else if (next.equals("West")) {
          instructions.add("Left");
        } else if (next.equals("North")) {
          instructions.add("Forward");
        }
      } else if (current.equals("East")) {
        if (next.equals("South")) {
          instructions.add("Right");
        } else if (next.equals("North")) {
          instructions.add("Left");
        } else if (next.equals("East")) {
          instructions.add("Forward");
        }
      } else if (current.equals("South")) {
        if (next.equals("West")) {
          instructions.add("Right");
        } else if (next.equals("East")) {
          instructions.add("Left");
        } else if (next.equals("South")) {
          instructions.add("Forward");
        }
      } else if (current.equals("West")) {
        if (next.equals("North")) {
          instructions.add("Right");
        } else if (next.equals("South")) {
          instructions.add("Left");
        } else if (next.equals("West")) {
          instructions.add("Forward");
        }
      }
    }

    return instructions;
  }
}
