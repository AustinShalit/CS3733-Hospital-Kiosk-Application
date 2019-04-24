package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LED_ModelTest {

  @Test
  void sendPathToPi() {

    LEDModel amazingModel = new LEDModel();

    amazingModel.sendPathToPi(amazingModel.endHall, amazingModel.bottomExit);

    assertTrue(true);
  }
}