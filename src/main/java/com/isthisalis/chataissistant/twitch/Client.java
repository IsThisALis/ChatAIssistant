package com.isthisalis.chataissistant.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

import com.isthisalis.chataissistant.configuration.Config.Configuration;

/**
 * Client
 */
public class Client {

  private OAuth2Credential credential;
  private TwitchClient client;


  public Client(Configuration config) {
 
    credential = new OAuth2Credential("twitch", config.accessToken);
    TwitchClientBuilder builder = TwitchClientBuilder.builder();
    client = builder.withClientId(config.clientId).withChatAccount(credential).withEnableHelix(true).withEnableChat(true).build();
  }

  public void update(Configuration config) {

    credential = new OAuth2Credential("twitch", config.accessToken);
    TwitchClientBuilder builder = TwitchClientBuilder.builder();
    client = builder.withClientId(config.clientId).withChatAccount(credential).withEnableHelix(true).withEnableChat(true).build();
  }

  public TwitchClient getTwitchClient() {
    return client;
  }
}
