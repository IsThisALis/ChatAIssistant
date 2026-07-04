package gfs.chataissistant.twitch.chat;

import java.util.logging.Logger;

import gfs.chataissistant.configuration.Config.Configuration;
import gfs.chataissistant.twitch.Client;

/**
 * Chat functionality implementation.
 */
public class Chat {

  private Logger logger = Logger.getGlobal();
  private Client client;
  private Configuration config;
 

  public Chat(Configuration config, Client client) {
    this.config = config;
    this.client = client;
  }

  public void update(Configuration config, Client client) {
    this.config = config;
    this.client = client;
  }


  public void join() { 
    client.getTwitchClient().getChat().joinChannel(config.channel);
    logger.info("Joined: " + config.channel);
  } 


  public Client getClient() {
    return client;
  }
}
