package gfs.chataissistant.ai;

import gfs.chataissistant.configuration.Config.Configuration;
import gfs.chataissistant.twitch.chat.Message;;

/**
 * AiService
 */
public interface AiService {

  public void update(Configuration config);
  public void update();

  public String ask(Message message);
  public String ask(String json);
}
