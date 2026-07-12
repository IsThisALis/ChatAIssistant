package com.isthisalis.chataissistant.db;

import com.isthisalis.chataissistant.configuration.Config.Configuration;
import com.isthisalis.chataissistant.twitch.chat.Message;

/**
 * Worker
 */
public class DBWorker {

  private final DBManager dbManager;

  public DBWorker(Configuration config) {
    dbManager = new DBManager(config);
    dbManager.connect();
  }


  public DBManager getDbManager() {
    return dbManager;
  }


  public void saveMessage(Message message) {
   String sql = "INSERT INTO messages (channel, user, message) VALUES (?, ?, ?)";
   
   dbManager.prepToExec(sql)
     .setString(1, message.channel())
     .setString(2, message.Username())
     .setString(3, message.Message())
     .execUpdt();
  }
}
