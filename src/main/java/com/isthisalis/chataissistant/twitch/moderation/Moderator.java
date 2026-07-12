package com.isthisalis.chataissistant.twitch.moderation;

import java.util.List;
import java.util.logging.Logger;

import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.BanUserInput;

import com.isthisalis.chataissistant.configuration.Config.Configuration;
import com.isthisalis.chataissistant.twitch.Client;

/**
 * Moderation implementation.
 */
public class Moderator implements Moderation {

  private String broadcasterId;

  private Configuration config;
  private TwitchHelix helix;
  private Logger logger = Logger.getGlobal();

  public Moderator(Configuration config, Client client) {
    this.helix = client.getTwitchClient().getHelix();
    this.config = config;
  }

  public void checkId() {
    if (broadcasterId == null || broadcasterId.isBlank()) {
    try {
      List<User> users = helix.getUsers(config.accessToken, null, List.of(config.channel)).execute().getUsers();

      if (users != null && !users.isEmpty()) broadcasterId = users.get(0).getId();
    } catch (Exception e) {
      logger.warning(e.toString());
      e.printStackTrace();
      // TODO: handle exception
      }
    }
  }


    @Override
  public void ban(String userId, String reason) {
    checkId();
    BanUserInput ban = BanUserInput.builder()
      .reason(reason)
      .userId(userId)
      .duration(null)
      .build();
    helix.banUser(config.accessToken, broadcasterId, broadcasterId, ban).execute();
  }


    @Override
  public void mute(String userId, int seconds, String reason) {
    checkId();
    try {
      BanUserInput ban = BanUserInput.builder()
        .reason(reason)
        .userId(userId)
        .duration(seconds)
        .build();
      helix.banUser(config.accessToken, broadcasterId, broadcasterId, ban).execute();
    } catch (Exception e) {
        logger.warning(e.toString());
        e.printStackTrace();
    }
  }


    @Override
  public void warn(String userId, String reason) {
    checkId();
    try {
      helix.warnChatUser(config.accessToken, broadcasterId, broadcasterId, userId, reason).execute();
    } catch (Exception e) {
      e.printStackTrace();
      logger.warning(e.toString());
    }
  }
}
