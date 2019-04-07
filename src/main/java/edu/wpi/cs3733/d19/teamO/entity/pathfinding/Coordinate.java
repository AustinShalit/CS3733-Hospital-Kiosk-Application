package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

public class Coordinate {
  private final int xcoord;
  private final int ycoord;

  public Coordinate(int xCoord, int yCoord) {
    this.xcoord = xCoord;
    this.ycoord = yCoord;
  }

  public int getXcoord() {
    return xcoord;
  }

  public int getYcoord() {
    return ycoord;
  }
}
