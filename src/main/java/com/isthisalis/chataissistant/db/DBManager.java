package com.isthisalis.chataissistant.db;

import java.io.File;

import java.sql.*;

import java.util.logging.Logger;

import com.isthisalis.chataissistant.configuration.Config.Configuration;

/**
 * DBManager
 */
public class DBManager {

  private String url;
  private boolean hasShutdownHook;
  private static boolean createdTable = false;

  private Logger logger = Logger.getGlobal();
  private Connection connection;
  private PreparedStatement pstmt;

  public DBManager(Configuration config) {
    if (config.dbUrl != null && !config.dbUrl.isBlank() && !config.dbUrl.equalsIgnoreCase("default")) url = config.dbUrl;
    else { url = "jdbc:sqlite:db/chat_history.db"; }
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
    if (!createdTable) {
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
      createdTable = true;
    }
  }

  public void execute(String sql) {
 try (Statement stmt = connection.createStatement()) {
      stmt.execute(sql);
      logger.info("Executed sql: " + sql);
    } catch (SQLException e) {
      logger.warning("Error: " + e);
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
