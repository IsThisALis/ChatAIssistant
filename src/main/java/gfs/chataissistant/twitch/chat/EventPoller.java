package gfs.chataissistant.twitch.chat;

import gfs.chataissistant.configuration.Config.Configuration;
import gfs.chataissistant.db.DBWorker;
import gfs.chataissistant.twitch.Client;
import gfs.chataissistant.ai.AiService;

import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.time.LocalDateTime;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

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
  private AiService ai;
  private Ignore ignore = new Ignore();

  public EventPoller(AiService ai, Configuration config, Client client) {
    this.config = config;
    this.ai = ai;
    this.client = client;
    this.worker = new DBWorker(config);
  }


  public void startMessagesPolling() {
    client.getTwitchClient().getEventManager().onEvent(ChannelMessageEvent.class, event -> {
      
      Message message = new Message(event.getChannel().getName(), event.getUser().getName(), event.getMessage(), LocalDateTime.now().format(FORMATTER));
      worker.saveMessage(message);
      logger.info("[ " + message.Username() + " ]: "+message.Message());
      if(!message.Message().toLowerCase().startsWith(config.askWord)) { logger.info("No ask word detected! Abort!"); return; }

      pollAiCall(message);
    });
  }

  private void pollAiCall(Message message) {
    response = ai.ask(message);

    if (response != null && !response.equals("none")) client.getTwitchClient().getChat().sendMessage(message.channel(), response);
      else {
        ignore.logToIgnored("[ " + message.Username() + " ]: " + message);
        return;
      }
      response = null;
  }

}
