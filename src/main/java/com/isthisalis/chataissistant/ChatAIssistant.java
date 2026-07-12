package com.isthisalis.chataissistant;

/**
 * Main class.
 */
public class ChatAIssistant {

  private static Application app = new Application();

  public static void main(String[] args) {
    app.start();
    app.update();
  }


  public static void setService(Application app) {
    ChatAIssistant.app = app;
  }
}
