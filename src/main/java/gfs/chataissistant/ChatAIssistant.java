package gfs.chataissistant;

import gfs.chataissistant.ai.AI;
import gfs.chataissistant.configuration.Config;
import gfs.chataissistant.twitch.Chat;
import gfs.chataissistant.util.JSON;

/**
 * Main
 */
public class ChatAIssistant {

  
  public static void main(String[] args) {
    Config.update();
    JSON.init();
    AI.configure();
    Chat.configure();
    Chat.join(Config.getConfig().channel);
  }
}
