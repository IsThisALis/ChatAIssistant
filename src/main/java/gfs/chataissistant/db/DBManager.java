package gfs.chataissistant.db;

import java.io.File;

import java.sql.*;

import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;

import gfs.chataissistant.configuration.Config.Configuration;
import gfs.chataissistant.twitch.chat.Message;

/**
 * DBManager
 */
public class DBManager {

  private String channel;
  private String url;
  private boolean hasShutdownHook;

  private Logger logger = Logger.getGlobal();
  private Connection connection;
  private PreparedStatement pstmt;

  public DBManager(Configuration config) {
    if (config.dbUrl != null && !config.dbUrl.isBlank() && !config.dbUrl.equalsIgnoreCase("default")) url = config.dbUrl;
    else { url = "jdbc:sqlite:db/chat_history.db"; }

    channel = config.channel;
  }
  

  public void connect() {
    try {
      new File("db").mkdirs();

      connection = DriverManager.getConnection(url);

      createTable();
      if (!hasShutdownHook) { 
        Runtime.getRuntime().addShutdownHook(new Thread(this::close)); 
        hasShutdownHook = true; 
      }
    } catch (SQLException e) {
      logger.warning("Error: " + e);
    }  
  }

  public void createTable() {
    String sql = """
      CREATE TABLE IF NOT EXISTS messages (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          channel TEXT NOT NULL,
          user TEXT NOT NULL,
          message TEXT NOT NULL,
          timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
          )
      """;
    execute(sql);
  }

  public void execute(String sql) {
 try (Statement stmt = connection.createStatement()) {
      stmt.execute(sql);
      logger.info("Executed sql: " + sql);
    } catch (SQLException e) {
      logger.warning("Error: " + e);
    }
  } 


  public List<Message> getHistory(int limit) {
    List<Message> messages = new ArrayList<>();
    String sql = """
      SELECT user, message, timestamp
      FROM messages
      WHERE channel = ?
      ORDER BY timestamp DESC
      LIMIT ?
      """;

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, channel.toLowerCase());
      pstmt.setInt(2, limit);

      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        messages.add(new Message(channel, rs.getString("user"), rs.getString("message"), rs.getString("timestamp")));
      }
      return messages;
    } catch (SQLException e) {
      logger.warning("Error: " + e);
      return null;
    }
  }


  public DBManager prepToExec(String sql) {
    try {
      pstmt = connection.prepareStatement(sql);
    } catch (SQLException e) { logger.warning("Error! " + e); }
    return this;
  }


  public DBManager setString(int index, String val) { 
    try {
      pstmt.setString(index, val);
    } catch (SQLException e) { logger.info("Error!" + e); }
    return this;
  }


  public DBManager setInt(int index, int val) {
    try {
      pstmt.setInt(index, val);
    } catch (SQLException e) { logger.info("Error! " + e); }
    return this;
  }


  public ResultSet exec() {
    try {
      return pstmt.executeQuery();
    } catch (SQLException e) {
      logger.warning("Error! " + e);
      return null;
    }
  }

  public void execUpdt() {
    try {
      pstmt.executeUpdate();
      pstmt.close();
    } catch (SQLException e) {
      logger.info("Error! " + e);
      e.printStackTrace();
      return;
    }
  }


  public void close() {
    try {
      if (connection != null && !connection.isClosed()) connection.close();
    } catch (SQLException e) {
      logger.warning("Error: " + e);
    }
  }
}
