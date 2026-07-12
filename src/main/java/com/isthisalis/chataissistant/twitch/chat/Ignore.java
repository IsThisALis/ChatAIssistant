package com.isthisalis.chataissistant.twitch.chat;

import java.util.logging.*;

/**
 * Logs ignored messages.
 */
public class Ignore {

  private final Logger logger;
  private final String ignoredPath = "logs/ignored.log";


  public Ignore() {
    logger = Logger.getLogger("chataissistant.ChatLogger"); 
    logger.setUseParentHandlers(false);
    logger.setLevel(Level.ALL);

    try {
      FileHandler fileHandler = new FileHandler(ignoredPath, true);

      fileHandler.setFormatter(new SimpleFormatter() {
        private static final String format = "[%1$tF %1$tT] %2$s%n";

          @Override
        public synchronized String format(LogRecord lr) {
          return String.format(format, lr.getMillis(), lr.getMessage());
        }
      });

      logger.addHandler(fileHandler);
    } catch (Exception e) {
      System.err.println("Unable to create logger: "+e.getMessage());
    }
  }

  
  public void logToIgnored(String content) {
    logger.info(content);
  }
}
