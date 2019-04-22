package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

/**
 * makes coorindate.
 */
public class Coordinate {
  private final int xcoord;
  private final int ycoord;
  private final boolean elevator;
  private final boolean stair;
  private final String floor;

  /**
   * creates a coordinate object.
   * @param xcoord x pixel value.
   * @param ycoord y pixel value.
   * @param isElevator is and elevator boolean.
   * @param isStair is a stair.
   * @param floor the floor this coordinate is on.
   */
  public Coordinate(int xcoord, int ycoord, boolean isElevator, boolean isStair, String floor) {
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.elevator = isElevator;
    this.stair = isStair;
    this.floor = floor;

  }


  public int getXcoord() {
    return xcoord;
  }

  public int getYcoord() {
    return ycoord;
  }

  public boolean isElevator() {
    return elevator;
  }

  public boolean isStair() {
    return stair;
  }

  public String getFloor() {
    return floor;
  }

  /**
   * gets the distance in ft between the two coordinates.
   * @param coordinate end.
   * @return distance ft.
   */
  public double getDist(Coordinate coordinate) {
    double xsquare = Math.pow(this.xcoord - coordinate.getXcoord(), 2);
    double ysquare = Math.pow(this.ycoord - coordinate.getYcoord(), 2);
    double dist = Math.sqrt(xsquare + ysquare);

    return dist;
  }
}
