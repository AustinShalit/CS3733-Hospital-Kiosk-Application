package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs3733.d19.teamO.entity.Node;



public class StepByStep {

  public StepByStep() {
  }

  /**
   * This returns a String direction between a start and end node.
   * @param start the starting node.
   * @param end the end node.
   * @return a String diretion.
   */
  public String getDirection(Coordinate start, Coordinate end) {
    int xdif = end.getXcoord() - start.getXcoord();
    int ydif = start.getYcoord() - end.getYcoord();

    double theta = Math.atan2(ydif, xdif) * 180 / Math.PI;



    if (theta > 22.5 && theta <= 67.5) {
      return "North East";
    } else if (theta > 67.5 && theta <= 112.5) {
      return "North";
    } else if (theta > 112.5 && theta <= 157.5) {
      return "North West";
    } else if (theta > 157.5 || theta <= -157.5) {
      return "West";
    } else if (theta < -112.5 && theta > -157.5) {
      return "South West";
    } else if (theta > -112.5 && theta <= -67.5) {
      return "South";
    } else if (theta > -67.5 && theta <= -22.5) {
      return "South East";
    } else if (theta > -22.5 || theta <= 22.5) {
      return "East";
    }

    return "";
  }

  /**
   * Creates a list of instruction from a list of nodes.
   * @param nodes list of nodes.
   * @return list of instructions.
   */
  public ArrayList<String> getStepByStep(List<Node> nodes) {
    ArrayList<String> instructions = new ArrayList<>();

    ArrayList<Coordinate> coordinates = new ArrayList<>();

    for (Node node: nodes) {
      Coordinate newCoord = new Coordinate(node.getXcoord(), node.getYcoord());
      coordinates.add(newCoord);
    }

    ArrayList<String> cardinalDirections = new ArrayList<>();

    List<String> compass = new ArrayList<>();
    compass.add("North");
    compass.add("North East");
    compass.add("East");
    compass.add("South East");
    compass.add("South");
    compass.add("South West");
    compass.add("West");
    compass.add("North West");

    for (int i = 0; i < coordinates.size() - 1; i++) {
      String direction = getDirection(coordinates.get(i), coordinates.get(i + 1));
      cardinalDirections.add(direction);
    }

    for (int i = 0; i < cardinalDirections.size() - 1; i++) {
      String current = cardinalDirections.get(i);
      String next = cardinalDirections.get(i + 1);

      int start = compass.indexOf(current);
      int end = compass.indexOf(next);
      int diff = end - start;

      if (diff == 1 || diff == -7 ) {
        instructions.add("Slight Right");
      } else if (diff == 2 || diff == -6) {
        instructions.add("Right");
      } else if (diff == 3 || diff == -5) {
        instructions.add("Hard Right");
      } else if (diff == -1 || diff == 7) {
        instructions.add("Slight Left");
      } else if (diff == -2 || diff == 6) {
        instructions.add("Left");
      } else if (diff == -3 || diff == 5) {
        instructions.add("Hard Left");
      } else if (diff == 0) {
        instructions.add("Forward");
      }


    }

    return instructions;
  }
}
