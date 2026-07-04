package gfs.chataissistant.ai.tools;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import gfs.chataissistant.configuration.Config.Configuration;
import gfs.chataissistant.db.DBWorker;
import gfs.chataissistant.twitch.chat.Message;

/**
 * getChatHistory AI tool. 
 */
public final class getChatHistory {

  private static Logger logger = Logger.getGlobal();

  private static DBWorker worker;
  private static Configuration config;

  public static void setConfig(Configuration config) {
    getChatHistory.config = config;
    worker = new DBWorker(config);
  }

  public static List<Message> getChatHistory(int limit) {
    List<Message> messages = new ArrayList<>();

    worker = new DBWorker(config);
    String sql = """
      SELECT user, message, timestamp
      FROM messages
      WHERE channel = ?
      ORDER BY timestamp DESC
      LIMIT ?
      """;

    try {
      logger.info("Searching chat history for channel: " + config.channel.toLowerCase() + " with limit: " + limit);
      ResultSet rs = worker.getDbManager()
        .prepToExec(sql)
        .setString(1, config.channel.toLowerCase())
        .setInt(2, limit)
        .exec();

    
      while (rs.next()) {
        messages.add(new Message(config.channel, rs.getString("user"), rs.getString("message"), rs.getString("timestamp")));
      }
    } catch (Exception e) {
      logger.warning("" + e);
      e.printStackTrace();
      return null;
    }
    return messages;
  }

  public static List<Message> getChatHistory(String username, int limit) {
    List<Message> messages = new ArrayList<>();

    String sql = """
      SELECT user, message, timestamp
      FROM messages
      WHERE channel = ?
        AND user = ?
      ORDER BY timestamp DESC
      LIMIT ?
      """;

    try {
      ResultSet rs = worker.getDbManager()
          .prepToExec(sql)
          .setString(1, config.channel.toLowerCase())
          .setString(2, username.toLowerCase())
          .setInt(3, limit)
          .exec();

    
      while (rs.next()) {
        messages.add(new Message(config.channel, rs.getString("user"), rs.getString("message"), rs.getString("timestamp")));
      }
      rs.close();
    } catch (Exception e) {
      logger.warning("" + e);
      e.printStackTrace();
      return null;
    }
    return messages;
  }


  public static List<Message> getChatHistory(String username) {
    List<Message> messages = new ArrayList<>();

    String sql = """
      SELECT user, message, timestamp
      FROM messages
      WHERE channel = ?
        AND user = ?
      ORDER BY timestamp DESC
      """;

    try {
      ResultSet rs = worker.getDbManager()
          .prepToExec(sql)
          .setString(1, config.channel.toLowerCase())
          .setString(2, username.toLowerCase())
          .exec();

    
      while (rs.next()) {
        messages.add(new Message(config.channel, rs.getString("user"), rs.getString("message"), rs.getString("timestamp")));
      }
      rs.close();
    } catch (Exception e) {
      logger.warning("" + e);
      e.printStackTrace();
      return null;
    }
    return messages;
  }

}
