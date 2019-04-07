package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.wpi.cs3733.d19.teamO.entity.Node;

public class StepByStep {

  public StepByStep() {
  }

  public String getDirection(Coordinate start, Coordinate end) {
    int xDif = end.getxCoord() - start.getxCoord();
    int yDif = start.getyCoord() - end.getyCoord();

    double theta = Math.atan2(yDif, xDif)*180/Math.PI;



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

  public ArrayList<String> getStepByStep(List<Node> nodes) {
    ArrayList<String> instructions = new ArrayList<>();

    ArrayList<Coordinate> coordinates= new ArrayList<>();

    for (Node node: nodes) {
      Coordinate newCoord = new Coordinate(node.getXcoord(),node.getYcoord());
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

      /**
       * Right
       *  North, East;
       * Left
       *  Current:
       *  Next:
       */
      if (current.equals("North")) {
        if (next.equals("East")) {
          instructions.add("Right");
        } else if (next.equals("North East")) {
          instructions.add("Slight Right");
        } else if (next.equals("North West")) {
          instructions.add("Slight Left");
        } else if (next.equals("South West")) {
          instructions.add("Hard Left");
        } else if (next.equals("South East")) {
          instructions.add("Hard Right");
        } else if (next.equals("West")) {
          instructions.add("Left");
        } else if (next.equals("North")) {
          instructions.add("Forward");
        } else if (next.equals("South")) {
          instructions.add("None");
        }
      } else if (current.equals("East")) {
        if (next.equals("South")) {
          instructions.add("Right");
        } else if (next.equals("North")) {
          instructions.add("Left");
        } else if (next.equals("East")) {
          instructions.add("Forward");
        } else if (next.equals("West")) {
          instructions.add("None");
        } else if (next.equals("North East")) {
          instructions.add("Slight Left");
        } else if (next.equals("South East")) {
          instructions.add("Slight Right");
        } else if (next.equals("North West")) {
          instructions.add("Hard Left");
        } else if (next.equals("South West")) {
          instructions.add("Hard Right");
        }
      } else if (current.equals("South")) {
        if (next.equals("West")) {
          instructions.add("Right");
        } else if (next.equals("East")) {
          instructions.add("Left");
        } else if (next.equals("South")) {
          instructions.add("Forward");
        } else if (next.equals("North")) {
          instructions.add("None");
        } else if (next.equals("South East")) {
          instructions.add("Slight Left");
        } else if (next.equals("South West")) {
          instructions.add("Slight Right");
        } else if (next.equals("North West")) {
          instructions.add("Hard Right");
        } else if (next.equals("North East")) {
          instructions.add("Hard Left");
        }
      } else if (current.equals("West")) {
        if (next.equals("North")) {
          instructions.add("Right");
        } else if (next.equals("South")) {
          instructions.add("Left");
        } else if (next.equals("West")) {
          instructions.add("Forward");
        } else if (next.equals("East")) {
          instructions.add("None");
        } else if (next.equals("North West")) {
          instructions.add("Slight Right");
        } else if (next.equals("South West")) {
          instructions.add("Slight Left");
        } else if (next.equals("North East")) {
          instructions.add("Hard Right");
        } else if (next.equals("South East")) {
          instructions.add("Hard Left");
        }
      } else if (current.equals("North West")) {
        if (next.equals("West")) {
          instructions.add("Slight Left");
        } else if (next.equals("North")) {
          instructions.add("Slight Right");
        } else if (next.equals("South West")) {
          instructions.add("Left");
        } else if (next.equals("North East")) {
          instructions.add("Right");
        } else if (next.equals("South")) {
          instructions.add("Hard Left");
        } else if (next.equals("East")) {
          instructions.add("Hard Right");
        } else if (next.equals("North West")) {
          instructions.add("Forward");
        } else if (next.equals("South East")) {
          instructions.add("None");
        }
      } else if (current.equals("North East")) {
        if (next.equals("North")) {
          instructions.add("Slight Left");
        } else if (next.equals("East")) {
          instructions.add("Slight Right");
        } else if (next.equals("North West")) {
          instructions.add("Left");
        } else if (next.equals("South East")) {
          instructions.add("Right");
        } else if (next.equals("South")) {
          instructions.add("Hard Right");
        } else if (next.equals("West")) {
          instructions.add("Hard Left");
        } else if (next.equals("South West")) {
          instructions.add("None");
        } else if (next.equals("North East")) {
          instructions.add("Forward");
        }
      } else if (current.equals("South East")) {
        if (next.equals("East")) {
          instructions.add("Slight Left");
        } else if (next.equals("South")) {
          instructions.add("Slight Right");
        } else if (next.equals("North East")) {
          instructions.add("Right");
        } else if (next.equals("South West")) {
          instructions.add("Right");
        } else if (next.equals("North")) {
          instructions.add("Hard Left");
        } else if (next.equals("West")) {
          instructions.add("Hard Right");
        } else if (next.equals("North West")) {
          instructions.add("None");
        } else if (next.equals("South East")) {
          instructions.add("Forward");
        }
      } else if (current.equals("South West")) {
        if (next.equals("South")) {
          instructions.add("Slight Left");
        } else if (next.equals("West")) {
          instructions.add("Slight Right");
        } else if (next.equals("North West")) {
          instructions.add("Right");
        } else if (next.equals("South East")) {
          instructions.add("Left");
        } else if (next.equals("North")) {
          instructions.add("Hard Right");
        } else if (next.equals("East")) {
          instructions.add("Hard Left");
        } else if (next.equals("North East")) {
          instructions.add("None");
        } else if (next.equals("South West")) {
          instructions.add("Forward");
        }
      }
    }

    return instructions;
  }
}
