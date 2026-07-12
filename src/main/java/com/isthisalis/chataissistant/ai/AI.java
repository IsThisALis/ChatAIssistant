package com.isthisalis.chataissistant.ai;

import com.isthisalis.chataissistant.util.JSON;

import com.isthisalis.chataissistant.configuration.Config.Configuration;

import com.isthisalis.chataissistant.DTO.request.Message;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;
import java.util.logging.Logger;

/**
 * AI functions implementation 
 */
public class AI implements AiService { 

  private static HttpClient http = HttpClient.newHttpClient();
  private HttpRequest request;
  private HttpResponse<String> response;

  private Configuration config;
  private JSON json;

  private Logger logger = Logger.getGlobal();

  private String rawJson;
  private String model;
  private String apiKey;
  private String apiUrl;


  public AI(Configuration config) {
    this.config = config;
    json = new JSON(config, this);
    model = config.model;
    apiKey = config.apiKey;
    apiUrl = config.apiUrl;
  }

    @Override
  public void update(Configuration config) {
    this.config = config;
    json = new JSON(config, this);
    model = config.model;
    apiKey = config.apiKey;
    apiUrl = config.apiUrl;
  }

    @Override
  public void update() {
    model = config.model;
    apiKey = config.apiKey;
  }

    @Override
  public String ask(com.isthisalis.chataissistant.twitch.chat.Message message) {
    List<Message> history = json.createInitialStory("[" + message.Username() + "]: " + message.Message());
    try {
      rawJson = json.makeAiRequest(history);
    } catch (Exception e) {
      logger.warning("Error! "+e);
      return "none";
    }
   
    switch (config.apiProvider) {
      case "deepseek":
        logger.warning("No deepseek support available");
        break;

      case "lms":  
        request = HttpRequest.newBuilder()
          .version(HttpClient.Version.HTTP_1_1)
          .uri(URI.create("http://127.0.0.1:1234/v1/chat/completions"))
          .header("Authorization", "Bearer "+apiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(rawJson))
          .build();
        break;

      case "openrouter":
        request = HttpRequest.newBuilder()
          .uri(URI.create("https://openrouter.ai/api/v1/chat/completions"))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .header("HTTP-Referer", "http://localhost")
          .header("X-Title", "ChatAIssistant")
          .POST(HttpRequest.BodyPublishers.ofString(rawJson))
          .build();
        break;

      default:
        request = HttpRequest.newBuilder()
          .uri(URI.create(apiUrl))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(rawJson))
          .build();
        break;
    }

    try {
      logger.info("Request sent to: "+model);
      response = http.send(request, HttpResponse.BodyHandlers.ofString());
      logger.info(response.body().trim());

      if (response.statusCode() != 200) throw new RuntimeException("API error: " + response.statusCode() + ": " + response.body());
      if (response != null) { logger.info("Got response from " + model + " response: " + response); }
    } catch (Exception e) {
      logger.warning("Error! "+e);
      e.printStackTrace();
      return "none";
     }
    return json.parseAiResponse(history, response.body());
  }


    @Override
  public String ask(String json) {
    switch (config.apiProvider) {
      case "deepseek":
        logger.warning("No deepseek support available");
        break;

      case "lms":  
        request = HttpRequest.newBuilder()
          .version(HttpClient.Version.HTTP_1_1)
          .uri(URI.create("http://127.0.0.1:1234/v1/chat/completions"))
          .header("Authorization", "Bearer "+apiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(rawJson))
          .build();
        break;

      case "openrouter":
        request = HttpRequest.newBuilder()
          .uri(URI.create("https://openrouter.ai/api/v1/chat/completions"))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .header("HTTP-Referer", "http://localhost")
          .header("X-Title", "ChatAIssistant")
          .POST(HttpRequest.BodyPublishers.ofString(rawJson))
          .build();
        break;

      default:
        request = HttpRequest.newBuilder()
          .uri(URI.create(apiUrl))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(rawJson))
          .build();
        break;
    }


    try {
      logger.info("Request sent to: "+model);
      response = http.send(request, HttpResponse.BodyHandlers.ofString());

      //if (response.statusCode() != 200) { throw new RuntimeException("API error: " + response.statusCode() + ": " + response.body()); }

      if (response.statusCode() == 404) { logger.warning("No model: " + model + " found, check your model name (404 ERROR)"); return "none"; }

      if (response.statusCode() == 429) { logger.warning("Model quota exceeded, model: " + model + ", try again later"); return "none"; }

      if (response.statusCode() != 200) { logger.warning("Unexpected API error: " + response.statusCode() + "" + response.body()); return "none"; }

      if (response != null) { logger.info("Got response from " + model + " response: " + response); }

    } catch (Exception e) {
      logger.warning("" + e);
      e.printStackTrace();
      return "none";
     }
    return response.body();
  }
}
