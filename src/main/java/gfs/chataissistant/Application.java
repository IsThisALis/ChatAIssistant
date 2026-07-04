package gfs.chataissistant;

import java.util.Scanner;
import java.util.logging.*;

import gfs.chataissistant.ai.AI;
import gfs.chataissistant.ai.tools.getChatHistory;

import gfs.chataissistant.configuration.Config;
import gfs.chataissistant.configuration.Config.Configuration;

import gfs.chataissistant.twitch.Client;
import gfs.chataissistant.twitch.chat.Chat;
import gfs.chataissistant.twitch.chat.EventPoller;

/**
 * Application
 */
public class Application {

  private String command;

  private Configuration config;
  private Client client;
  private AI ai;
  private Chat chat;
  private EventPoller chatEventPoller;

  private Scanner scanner;
  private Logger logger;

  public Application() {

    try {
      logger = Logger.getGlobal();

      FileHandler handler = new FileHandler("ChatAIssistant.log", 0, 1, true);
      handler.setLevel(Level.ALL);
      handler.setFormatter(new BaseLogFormat());
      
      logger.setUseParentHandlers(false);
      logger.setLevel(Level.ALL);
      logger.addHandler(handler);
    } catch (Exception e) {
      if (logger != null) logger.warning("Error! " + e);
      e.printStackTrace();
    }

    scanner = new Scanner(System.in);
    config = new Config("config.yml").getConfig();
    config.sysout();

    client = new Client(config);
    getChatHistory.setConfig(config);
    ai = new AI(config);

    chat = new Chat(config, client);
    chatEventPoller = new EventPoller(ai, config, client);
  }


  public void start() {
    chat.join();
    chatEventPoller.startMessagesPolling();
  }

  public void update() {
    while (true) {
      if (scanner.hasNext()) command = scanner.nextLine();

        if (command != null && command.contains("get-history")) {
          if (scanner.hasNextInt()) System.out.println(getChatHistory.getChatHistory(scanner.nextInt()));
          if (scanner.hasNextLine()) System.out.println(getChatHistory.getChatHistory(scanner.nextLine(), scanner.nextInt()));
          command = null;
        }

        if (command != null && command.contains("restart")) {
          reload();
          start();
        }

        command = null;
    }
  }


  private void reload() {
    config = new Config("config.yml").getConfig();
    config.sysout();

    client.update(config);
    chat.update(config, client);
    ai.update(config);
    getChatHistory.setConfig(config);
    
  }


  private final class BaseLogFormat extends Formatter {
      
        @Override
      public String format(LogRecord rec) {
        return String.format("%s  %s: %s%n", rec.getLevel(), "ChatAIssistant", rec.getMessage());
      }
    }
} 
