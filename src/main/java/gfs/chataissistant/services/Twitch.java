package gfs.chataissistant.services;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

/**
 * Twitch implementation 
 */
public class Twitch {

  private static String clientId;
  private static String accessToken;
  private static String askWord;
  private static String userName;
  private static String message;
  private static String response;

  private static TwitchClientBuilder builder = TwitchClientBuilder.builder();
  private static OAuth2Credential credential;
  private static TwitchClient client;

  public static void init() {
    askWord = IO.loadTextFile("configs/askWord.txt");
    accessToken = IO.loadTextFile("configs/accessToken.txt");
    clientId = IO.loadTextFile("configs/clientId.txt");

    credential = new OAuth2Credential("twitch", accessToken);

    client =  builder.withClientId(clientId).withChatAccount(credential).withEnableChat(true).build();

    client.getEventManager().onEvent(ChannelMessageEvent.class, event -> {

      userName = event.getUser().getName();
      message = event.getMessage();

      System.out.println("[ " + userName + " ]: "+message);

      if(!message.toLowerCase().startsWith(askWord.toLowerCase())) return;

      response = AI.ask(message);

      if (response == null || !response.equals("none")) client.getChat().sendMessage(event.getChannel().getName(), response);
      response = null;
    });
  }


  public static void start(String channel) {
    client.getChat().joinChannel(channel);
    System.out.println("Joined: "+channel);
  }
}
