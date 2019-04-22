package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

import edu.wpi.cs3733.d19.teamO.entity.Node;


@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.LooseCoupling", "PMD.CyclomaticComplexity",
    "PMD.AvoidInstantiatingObjectsInLoops", "PMD.GodClass", "PMD.ExcessiveMethodLength",
    "PMD.NPathComplexity", "PMD.PositionLiteralsFirstInComparisons"})

public class StepByStep {

  public StepByStep() {
  }

  /**
   * This returns a String direction between a start and end node.
   *
   * @param start the starting node.
   * @param end   the end node.
   * @return a String diretion.
   */
  public String getDirection(Coordinate start, Coordinate end) {
    int xdif = end.getXcoord() - start.getXcoord();
    int ydif = start.getYcoord() - end.getYcoord();

    double theta = Math.atan2(ydif, xdif) * 180 / Math.PI;

    if (start.isElevator() && end.isElevator()) {
      return "Elevator";
    } else if (start.isStair() && end.isStair()) {
      return "Stair";
    } else if (theta > 22.5 && theta <= 67.5) {
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
   *
   * @param nodes list of nodes.
   * @return list of instructions.
   */
  public ArrayList<Pair<String, Node>> getStepByStep(List<Node> nodes) {
    ArrayList<Pair<Coordinate, Node>> coordinates = new ArrayList<>();

    for (Node node : nodes) {
      boolean isElevator = false;
      boolean isStair = false;
      if (node.getNodeType() == Node.NodeType.STAI) {
        isStair = true;

      }
      if (node.getNodeType() == Node.NodeType.ELEV) {
        isElevator = true;
      }
      Coordinate newCoord = new Coordinate(node.getXcoord(), node.getYcoord(),
          isElevator, isStair, node.getFloor());
      coordinates.add(new Pair<>(newCoord, node));
    }

    ArrayList<Pair<String, Node>> cardinalDirections = new ArrayList<>();

    List<String> compass = new ArrayList<>();
    compass.add("North");
    compass.add("North East");
    compass.add("East");
    compass.add("South East");
    compass.add("South");
    compass.add("South West");
    compass.add("West");
    compass.add("North West");
    ArrayList<Pair<Double, Node>> distance = new ArrayList<>();

    ArrayList<Pair<String, Node>> instructions = new ArrayList<>();
    ArrayList<String> floors = new ArrayList<>();

    for (int i = 0; i < coordinates.size() - 1; i++) {
      String direction = getDirection(coordinates.get(i).getFirst(), coordinates.get(i + 1).getFirst());
      cardinalDirections.add(new Pair<>(direction, coordinates.get(i).getSecond()));
      double diff = coordinates.get(i).getFirst().getDist(coordinates.get(i + 1).getFirst());
      distance.add(new Pair<>(diff, coordinates.get(i).getSecond()));
      floors.add(coordinates.get(i).getFirst().getFloor());
    }
    String currFloor = nodes.get(0).getFloor();
    instructions.add(new Pair<>("Floor " + currFloor, nodes.get(0)));
    instructions.add(new Pair<>(
        "\tWalk " + cardinalDirections.get(0).getFirst() + " from " + nodes.get(0).getLongName(),
        nodes.get(0))
    );
    double totalDist = 0;
    String last = "other";
    String forward = "forward";

    for (int i = 0; i < cardinalDirections.size() - 1; i++) {
      String current = cardinalDirections.get(i).getFirst();
      String next = cardinalDirections.get(i + 1).getFirst();

      int start = compass.indexOf(current);
      int end = compass.indexOf(next);
      int diff = end - start;


      if (!last.equals(forward)) {
        instructions.add(new Pair<>(
            "\tWalk " + Double.toString(Math.round(distance.get(i).getFirst())) + " ft then",
            distance.get(i).getSecond()));
      }

      if (last.equals("Elevator") || last.equals("Stair")) {
        instructions.remove(instructions.size() - 1);
      }

      if (current.equals("Elevator")) {
        instructions.remove(instructions.size() - 1);
        instructions.remove(instructions.size() - 1);
        instructions.add(new Pair<>("\tUse elevator", cardinalDirections.get(i).getSecond()));

        String nextFloor = "";
        currFloor = floors.get(i);
        if (i > 0) {
          nextFloor = floors.get(i + 1);
        }
        if (!currFloor.equals(nextFloor)) {
          instructions.add(new Pair<>("Floor " + nextFloor, cardinalDirections.get(i).getSecond()));
        }

        last = "Elevator";
      } else if (current.equals("Stair")) {
        instructions.remove(instructions.size() - 1);
        instructions.remove(instructions.size() - 1);
        instructions.add(new Pair<>("\tUse stairs", cardinalDirections.get(i).getSecond()));

        String nextFloor = "";
        currFloor = floors.get(i);
        if (i > 0) {
          nextFloor = floors.get(i + 1);
        }
        if (!currFloor.equals(nextFloor)) {
          instructions.add(new Pair<>("Floor " + nextFloor, cardinalDirections.get(i).getSecond()));
        }

        last = "Stair";
      } else if (diff == 1 || diff == -7) {
        instructions.add(new Pair<>("\tTake a slight right", cardinalDirections.get(i).getSecond()));
        last = "other";
      } else if (diff == 2 || diff == -6) {
        instructions.add(new Pair<>("\tTake a right", cardinalDirections.get(i).getSecond()));
        last = "other";
      } else if (diff == 3 || diff == -5) {
        instructions.add(new Pair<>("\tTake a hard right", cardinalDirections.get(i).getSecond()));
        last = "other";
      } else if (diff == -1 || diff == 7) {
        instructions.add(new Pair<>("\tTake a slight left", cardinalDirections.get(i).getSecond()));
        last = "other";
      } else if (diff == -2 || diff == 6) {
        instructions.add(new Pair<>("\tTake a left", cardinalDirections.get(i).getSecond()));
        last = "other";
      } else if (diff == -3 || diff == 5) {
        instructions.add(new Pair<>("\tTake a hard left", cardinalDirections.get(i).getSecond()));
        last = "other";
      } else if (diff == 0) {
        if (!last.equals(forward)) {
          instructions.add(new Pair<>("\tGo forward", cardinalDirections.get(i).getSecond()));
          totalDist += Math.round(distance.get(i).getFirst());
          last = "forward";
        }
        last = "forward";
      }
    }

    instructions.add(new Pair<>(
        "\tArrive at " + nodes.get(nodes.size() - 1).getLongName(),
        nodes.get(nodes.size() - 1)
    ));

    return instructions;
  }


}
