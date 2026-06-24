package gfs.chataissistant.twitch;

import gfs.chataissistant.configuration.Config;
import gfs.chataissistant.util.Log;
import gfs.chataissistant.ai.AI;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;


/**
 * Chat functionality implementation.
 */
public class Chat {

  private static String clientId;
  private static String accessToken;
  private static String askWord;

  private static Message message; 
  private static String response;

  private static String channel;

  private static TwitchClientBuilder builder = TwitchClientBuilder.builder();
  private static OAuth2Credential credential;
  private static TwitchClient client;
  
  public static void configure() {
    askWord = Config.getConfig().askWord;
    accessToken = Config.getConfig().accessToken;
    clientId = Config.getConfig().clientId;

    credential = new OAuth2Credential("twitch", accessToken);

    client =  builder.withClientId(clientId).withChatAccount(credential).withEnableChat(true).build();
    client.getEventManager().onEvent(ChannelMessageEvent.class, event -> {
      
      message = new Message(event.getUser().getName(), event.getMessage());
      System.out.println("[ " + message.Username() + " ]: "+message.Message());
      if(!message.Message().toLowerCase().startsWith(askWord)) return;

      response = AI.ask(message);

      if (response != null && !response.equals("none")) client.getChat().sendMessage(event.getChannel().getName(), response);
      else {
        Log.logToIgnored("[ " + message.Username() + " ]: " + message);
        return;
      }

      response = null;
    });
  }


  public static void join(String channel) {
    Chat.channel = channel;
    client.getChat().joinChannel(channel);
    System.out.println("Joined: "+channel);
  }


  public static String getChannel() {
    return channel;
  }
}
