package com.isthisalis.chataissistant.twitch.chat;

import com.isthisalis.chataissistant.configuration.Config.Configuration;
import com.isthisalis.chataissistant.db.DBWorker;
import com.isthisalis.chataissistant.twitch.Client;
import com.isthisalis.chataissistant.twitch.moderation.Moderation;

import lombok.NonNull;

import com.isthisalis.chataissistant.ai.AiService;

import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.time.LocalDateTime;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.enums.CommandPermission;

/**
 * EventPoller
 */
public class EventPoller {

  private String response;

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private Logger logger = Logger.getGlobal();
  private DBWorker worker;
  private Client client;
  private Configuration config;
  private  AiService ai;
  private Moderation moderation;
  private Ignore ignore = new Ignore();

  public EventPoller(@NonNull AiService ai, @NonNull Configuration config, @NonNull Client client, @NonNull Moderation moderation) {
    this.config = config;
    this.ai = ai;
    this.client = client;
    this.moderation = moderation;
    this.worker = new DBWorker(config);
  }


  public void startMessagesPolling() {
    client.getTwitchClient().getEventManager().onEvent(ChannelMessageEvent.class, event -> {
      
      Message message = new Message(event.getChannel().getName(), event.getUser().getName(), event.getMessage(), LocalDateTime.now().format(FORMATTER));
      worker.saveMessage(message);
      logger.info("[ " + message.Username() + " ]: "+message.Message());

      if (message.Message().startsWith(config.askWord)) { pollAiCall(message); }
      if (message.Message().contains(config.commandPrefix)) { if (event.getPermissions().contains(CommandPermission.MODERATOR) || event.getPermissions().contains(CommandPermission.BROADCASTER)) pollCommand(message, event); System.out.println("NOT SOSAL"); return; }
    });
  }


  private void pollAiCall(Message message) {
    try {
      response = ai.ask(message);
      if (response != null && !response.equals("none")) {
        worker.saveMessage(new Message(config.channel.toLowerCase(), "AI Response", response, LocalDateTime.now().format(FORMATTER)));
        client.getTwitchClient().getChat().sendMessage(message.channel(), response);
        return;
      }
    } catch (Exception e) {
      logger.warning(e.toString());
      e.printStackTrace();
    }

    ignore.logToIgnored("[ " + message.Username() + " ]: " + message);
    response = null;
  }


  private void pollCommand(Message message, ChannelMessageEvent event) { 

    if (message.Message().contains("warn")) { 
      String[] parts = message.Message().split("\\s+", 2);
      moderation.warn(event.getReplyInfo().getUserId(), parts[1]);
      logger.info("Warned user: " + event.getReplyInfo().getDisplayName());
      return;
    }

    if (message.Message().contains(config.commands.get("mute"))) { 
      String[] parts = message.Message().split("\\s+", 4);
      for (int x=0; x<parts.length; x++) System.out.println(parts[x] + " x: "+x);
      moderation.mute(event.getReplyInfo().getUserId(), Integer.valueOf(parts[2]), parts[3]);
      logger.info("Muted user: " + event.getReplyInfo().getDisplayName());
      return; 
    }

    if (message.Message().contains(config.commands.get("ban"))) {
      String[] parts = message.Message().split("\\s+", 3);

      moderation.ban(event.getReplyInfo().getUserId(), parts[2]);
      return;
    }

  }

}
