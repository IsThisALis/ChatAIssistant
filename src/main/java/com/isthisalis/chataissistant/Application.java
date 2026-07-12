package com.isthisalis.chataissistant;

import java.util.Scanner;
import java.util.logging.*;

import com.isthisalis.chataissistant.ai.AI;
import com.isthisalis.chataissistant.ai.tools.getChatHistory;

import com.isthisalis.chataissistant.configuration.Config;
import com.isthisalis.chataissistant.configuration.Config.Configuration;

import com.isthisalis.chataissistant.twitch.Client;
import com.isthisalis.chataissistant.twitch.chat.Chat;
import com.isthisalis.chataissistant.twitch.chat.EventPoller;
import com.isthisalis.chataissistant.twitch.moderation.Moderator;

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

    client = new Client(config);
    getChatHistory.setConfig(config);
    ai = new AI(config);

    chat = new Chat(config, client);
    chatEventPoller = new EventPoller(ai, config, client, new Moderator(config, client));
  }


  public void start() {
    chat.join();
    chatEventPoller.startMessagesPolling();
  }

  public void update() {
    while (true) {
      if (scanner.hasNext()) command = scanner.nextLine();

      if (command != null) {
        if (command.contains("get-history")) {
          if (scanner.hasNextInt()) logger.info(getChatHistory.getChatHistory(scanner.nextInt()).toString());
          if (scanner.hasNextLine()) logger.info(getChatHistory.getChatHistory(scanner.nextLine(), scanner.nextInt()).toString());
          command = null;
        }

        if (command.contains("restart")) {
          reload();
          start();
        }

        if (command.contains("exit") || command.contains("stop")) { logger.info("Stopping ChatAIssistant"); System.exit(0); }
        command = null;
      }
    }
  }


  private void reload() {
    config = new Config("config.yml").getConfig();

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
