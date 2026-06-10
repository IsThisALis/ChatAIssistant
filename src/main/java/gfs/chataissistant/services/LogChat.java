package gfs.chataissistant.services;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * LogChat
 */
public class LogChat {

  private static final Logger logger;
  private static final String logs_path = "logs/";
  private static final String ignoredPath = logs_path+"ignored.log";


  static {
    logger = Logger.getLogger("chataissistant.ChatLogger"); 
    logger.setUseParentHandlers(false);
    logger.setLevel(Level.ALL);

    try {
      FileHandler fileHandler = new FileHandler(ignoredPath, true);

      fileHandler.setFormatter(new SimpleFormatter() {
        private static final String format = "[%1$tF %1$tT] %2$s%n";

          @Override
        public synchronized String format(LogRecord lr) {
          return String.format(format,lr.getMillis(), lr.getMessage());
        }
      });

      logger.addHandler(fileHandler);
    } catch (IOException e) {
      System.err.println("Unable to create logger: "+e.getMessage());
    }
  }

  
  public static void logIgnored(String username ,String message) {
    logger.info("[ " + username + " ]: " + message);
  }


  public static void logInfo(String msg) {}
  public static void logWarn(String msg) {}
  public static void logErr(String msg) {}
}
