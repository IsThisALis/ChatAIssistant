package gfs.chataissistant.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

import gfs.chataissistant.configuration.Config.Configuration;

/**
 * Client
 */
public class Client {

  private OAuth2Credential credential;
  private TwitchClient client;

  private Configuration config;

  public Client(Configuration config) {
    this.config = config;
 
    credential = new OAuth2Credential("twitch", config.accessToken);
    TwitchClientBuilder builder = TwitchClientBuilder.builder();
    client = builder.withClientId(config.clientId).withChatAccount(credential).withEnableChat(true).build();
  }

  public void update(Configuration config) {
    this.config = config;

    credential = new OAuth2Credential("twitch", config.accessToken);
    TwitchClientBuilder builder = TwitchClientBuilder.builder();
    client = builder.withClientId(config.clientId).withChatAccount(credential).withEnableChat(true).build();
  }

  public TwitchClient getTwitchClient() {
    return client;
  }
}
