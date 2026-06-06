package gfs.chataissistant;

import gfs.chataissistant.services.AI;
import gfs.chataissistant.services.Twitch;

/**
 * Main
 */
public class Main {

  
  public static void main(String[] args) {
    AI.init();
    Twitch.init();
    Twitch.start("IsThisALis");
  }
}
