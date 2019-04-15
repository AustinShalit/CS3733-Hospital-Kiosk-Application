package edu.wpi.cs3733.d19.teamO.entity;

import org.jetbrains.annotations.NotNull;

import javafx.scene.image.Image;

public class Floor implements Comparable<Floor> {
  private final int number;
  private final String name;
  private final Image mapImage;

  /**
   * Create a new floor.
   */
  public Floor(int number, String name, Image mapImage) {
    this.number = number;
    this.name = name;
    this.mapImage = mapImage;
  }

  public int getNumber() {
    return number;
  }

  public String getName() {
    return name;
  }

  public Image getMapImage() {
    return mapImage;
  }

  @Override
  public int compareTo(@NotNull Floor o) {
    return Integer.compare(o.number, number);
  }
}
