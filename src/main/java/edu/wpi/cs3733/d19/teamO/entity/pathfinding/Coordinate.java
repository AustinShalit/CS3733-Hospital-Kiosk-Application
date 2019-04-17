package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

public class Coordinate {
  private final int xcoord;
  private final int ycoord;
  private final boolean isElevator;
  private final boolean isStair;

  public Coordinate(int xcoord, int ycoord, boolean isElevator, boolean isStair) {
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.isElevator = isElevator;
    this.isStair = isStair;
  }

  public int getXcoord() {
    return xcoord;
  }

  public int getYcoord() {
    return ycoord;
  }

  public boolean isElevator() { return isElevator; }

  public boolean isStair() { return isStair; }

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
