package edu.wpi.cs3733.d19.teamO;

import java.time.LocalDateTime;

public final class Globals {
  private Globals() {

  }

  public static LocalDateTime defaultTime() {
    return LocalDateTime.of(1, 1, 1, 1, 1);
  }
}
