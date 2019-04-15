package edu.wpi.cs3733.d19.teamO.entity;

import javafx.scene.image.Image;

public class Floor {
  private final int number;
  private final String id;
  private final Image mapImage;

  public Floor(int number, String id, Image mapImage) {
    this.number = number;
    this.id = id;
    this.mapImage = mapImage;
  }

  public int getNumber() {
    return number;
  }

  public String getId() {
    return id;
  }

  public Image getMapImage() {
    return mapImage;
  }
}
