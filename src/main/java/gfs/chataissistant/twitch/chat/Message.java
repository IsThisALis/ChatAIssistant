package gfs.chataissistant.twitch.chat;

/**
 * Message DTO.
 */
public record Message(String channel, String Username, String Message, String timestamp) {

    @Override
  public String toString() {
    return channel + ": " + "[" + Username + "]: " + Message;
  }
}
