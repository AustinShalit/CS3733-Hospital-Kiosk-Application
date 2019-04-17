package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs3733.d19.teamO.entity.Node;


@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.LooseCoupling", "PMD.CyclomaticComplexity",
    "PMD.AvoidInstantiatingObjectsInLoops"})

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
    ArrayList<Double> distance = new ArrayList<>();

    ArrayList<String> instructions = new ArrayList<>();
    for (int i = 0; i < coordinates.size() - 1; i++) {
      String direction = getDirection(coordinates.get(i), coordinates.get(i + 1));
      cardinalDirections.add(direction);
      double diff = coordinates.get(i).getDist(coordinates.get(i + 1));
      distance.add(diff);
    }
    instructions.add("Walk " + cardinalDirections.get(0) + " from " + nodes.get(0).getShortName());
    double totalDist = 0;
    String last = "other";
    String forward = "forward";

    for (int i = 0; i < cardinalDirections.size() - 1; i++) {
      String current = cardinalDirections.get(i);
      String next = cardinalDirections.get(i + 1);

      int start = compass.indexOf(current);
      int end = compass.indexOf(next);
      int diff = end - start;


      if (!last.equals(forward)) {
        instructions.add("Walk " + Double.toString(Math.round(distance.get(i))) + " ft then");
      }


      if (diff == 1 || diff == -7 ) {
        instructions.add("take a slight right");
        last = "other";
      } else if (diff == 2 || diff == -6) {
        instructions.add("take a right");
        last = "other";
      } else if (diff == 3 || diff == -5) {
        instructions.add("take a hard right");
        last = "other";
      } else if (diff == -1 || diff == 7) {
        instructions.add("take a slight left");
        last = "other";
      } else if (diff == -2 || diff == 6) {
        instructions.add("take a left");
        last = "other";
      } else if (diff == -3 || diff == 5) {
        instructions.add("take a hard left");
        last = "other";
      } else if (diff == 0) {
        if (!last.equals(forward)) {
          instructions.add("go forward");
          totalDist += Math.round(distance.get(i));
          last = "forward";
        }
        last = "forward";
      }
    }

    instructions.add("Arrive at " + nodes.get(nodes.size() - 1).getShortName());

    return instructions;
  }



}
