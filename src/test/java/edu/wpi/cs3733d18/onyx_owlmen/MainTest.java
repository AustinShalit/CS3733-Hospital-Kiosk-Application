package edu.wpi.cs3733d18.onyx_owlmen;

import org.junit.jupiter.api.Test;

class MainTest extends UtilityClassTest {

  MainTest() {
    super(Main.class);
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void mainTest() {
    Main.main();
  }
}
