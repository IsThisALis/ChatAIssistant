package com.isthisalis.chataissistant.twitch.moderation;

/**
 * Moderation
 */
public interface Moderation {

  public void ban(String userId, String reason);

  public void mute(String userId, int seconds, String reason);

  public void warn(String userId, String reason);
}
