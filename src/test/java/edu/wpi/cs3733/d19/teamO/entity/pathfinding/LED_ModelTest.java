package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LED_ModelTest {

  @Test
  void sendPathToPi() {

    LED_Model amazingModel = new LED_Model();

    amazingModel.sendPathToPi(amazingModel.endHall, amazingModel.bottomExit);

    assertTrue(true);
  }
}