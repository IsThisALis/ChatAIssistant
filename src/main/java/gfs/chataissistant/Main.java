package gfs.chataissistant;

import gfs.chataissistant.services.*;

/**
 * Main
 */
public class Main {

  
  public static void main(String[] args) {
    Config.update();
    JSON.init();
    AI.init();
    Twitch.init();
    Twitch.start(Config.getConfig().channel);
  }
}
