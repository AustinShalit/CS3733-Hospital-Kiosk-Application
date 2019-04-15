package edu.wpi.cs3733.d19.teamO.entity;

import java.util.Objects;
import java.util.Optional;

import edu.wpi.cs3733.d19.teamO.util.Registry;

public class Floors extends Registry<Floor> {

  /**
   * Creates a new Floors registry.
   */
  public Floors() {
  }

  /**
   * Creates a new Floors registry.
   *
   * @param initial the initial Floors
   */
  public Floors(Floor... initial) {
    registerAll(initial);
  }

  /**
   * Gets the floor with the given name.
   *
   * @param name the name of the floor to get
   */
  public Optional<Floor> forName(String name) {
    return getItems().stream()
        .filter(t -> t.getName().equals(name))
        .findFirst();
  }

  @Override
  public void register(Floor floor) {
    Objects.requireNonNull(floor);
    if (isRegistered(floor)) {
      throw new IllegalArgumentException("Floor " + floor + " is already registered");
    }
    addItem(floor);
  }

  @Override
  public void unregister(Floor floor) {
    removeItem(floor);
  }
}
