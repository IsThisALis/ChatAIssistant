package com.isthisalis.chataissistant.ai;

import com.isthisalis.chataissistant.configuration.Config.Configuration;
import com.isthisalis.chataissistant.twitch.chat.Message;;

/**
 * AiService
 */
public interface AiService {

  public void update(Configuration config);
  public void update();

  public String ask(Message message);
  public String ask(String json);
}
