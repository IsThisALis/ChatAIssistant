package gfs.chataissistant.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * AI functions implementation 
 */
public class AI {

  private static HttpClient http = HttpClient.newHttpClient();
  private static HttpRequest request;
  private static HttpResponse<String> response;

//  public static String apiUrl;
  private static String json;

  private static String model;
  private static String apiKey; 


  public static void init() {
    model = Config.getConfig().model;
    apiKey = Config.getConfig().apiKey;
  }


  public static String ask(String message) {
    try {
      json = JSON.receiveMessage(message, model);
    } catch (Exception e) {
      System.err.println("Error! "+e);
      return "none";
    }


    if (Config.getConfig().apiProvider.toLowerCase().equals("lms")) {
      request = HttpRequest.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .uri(URI.create("http://127.0.0.1:1234/v1/chat/completions"))
        .header("Authorization", "Bearer "+apiKey)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();
    }


    if (Config.getConfig().apiProvider.toLowerCase().equals("openrouter")) {
      request = HttpRequest.newBuilder()
        .uri(URI.create("https://openrouter.ai/api/v1/chat/completions"))
        .header("Authorization", "Bearer " + apiKey)
        .header("Content-Type", "application/json")
        .header("HTTP-Referer", "http://localhost")
        .header("X-Title", "ChatAIssistant")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();
    }

    try {
      System.out.println("Request sent to: "+model);
      response = http.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) throw new RuntimeException("API error: " + response.statusCode() + ": " + response.body());
      if (response != null) { System.out.println("Got response from "+model+" response: "+response); }
    } catch (Exception e) {
      System.err.println("Error! "+e);
      e.printStackTrace();
      return "none";
     }
    return JSON.parseJson(response.body());
  }
}
