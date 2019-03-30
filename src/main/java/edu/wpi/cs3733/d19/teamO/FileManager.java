package edu.wpi.cs3733.d19.teamO;

import java.io.File;
import java.util.logging.Logger;

/**
 * Manage directories and log files.
 */
public final class FileManager {

  private static final Logger logger
      = Logger.getLogger(FileManager.class.getName());

  private static final String DIRECTORY_NAME = "Onyx Owlmen Kiosk";

  public static final File APP_DIRECTORY;

  static {
    if (isWindows()) {
      APP_DIRECTORY
          = new File(System.getenv("APPDATA")
          + File.separator
          + DIRECTORY_NAME);
    } else if (isMac()) {
      APP_DIRECTORY
          = new File(System.getProperty("user.home")
          + File.separator
          + "/Library/"
          + DIRECTORY_NAME);
    } else {
      APP_DIRECTORY
          = new File(System.getProperty("user.home")
          + File.separator
          + DIRECTORY_NAME);
    }
    if (!APP_DIRECTORY.mkdirs()) {
      logger.warning("Unable to create application directory: " + APP_DIRECTORY.getAbsolutePath());
    }
  }

  private static boolean isWindows() {
    return System.getProperty("os.name").startsWith("Windows");
  }

  private static boolean isMac() {
    return System.getProperty("os.name").startsWith("Mac");
  }

  private FileManager() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
