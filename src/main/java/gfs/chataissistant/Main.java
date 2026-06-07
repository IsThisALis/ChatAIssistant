package gfs.chataissistant;

import gfs.chataissistant.services.AI;
import gfs.chataissistant.services.IO;
import gfs.chataissistant.services.Twitch;

/**
 * Main
 */
public class Main {

  
  public static void main(String[] args) {
    AI.init();
    Twitch.init();
    Twitch.start(IO.loadTextFile("configs/userName.txt"));
  }
}
