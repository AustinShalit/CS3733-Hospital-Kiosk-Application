package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

public class Coordinate {
  private int xCoord;
  private int yCoord;

  public Coordinate(int xCoord, int yCoord) {
    this.xCoord = xCoord;
    this.yCoord = yCoord;
  }

  public int getxCoord() {
    return xCoord;
  }

  public int getyCoord() {
    return yCoord;
  }
}
