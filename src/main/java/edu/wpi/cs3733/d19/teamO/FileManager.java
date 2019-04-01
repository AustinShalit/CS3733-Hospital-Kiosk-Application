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
  public static final File LOG_DIRECTORY;

  static {
    if (isWindows()) {
      APP_DIRECTORY
          = new File(System.getenv("APPDATA")
          + File.separator
          + DIRECTORY_NAME);
      LOG_DIRECTORY = APP_DIRECTORY;
    } else if (isMac()) {
      APP_DIRECTORY
          = new File(System.getProperty("user.home")
          + "/Library/"
          + DIRECTORY_NAME);
      LOG_DIRECTORY
          = new File(System.getProperty("user.home")
          + "/Library/Logs/"
          + DIRECTORY_NAME);
    } else {
      APP_DIRECTORY
          = new File(System.getProperty("user.home")
          + File.separator
          + DIRECTORY_NAME);
      LOG_DIRECTORY = APP_DIRECTORY;
    }
    if (!APP_DIRECTORY.exists() && !APP_DIRECTORY.mkdirs()) {
      logger.warning("Unable to create application directory: " + APP_DIRECTORY.getAbsolutePath());
    }
    if (!APP_DIRECTORY.exists() && !LOG_DIRECTORY.mkdirs()) {
      logger.warning("Unable to create log directory: " + LOG_DIRECTORY.getAbsolutePath());
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
