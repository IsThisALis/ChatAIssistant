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

  private static String json;
  private static String model;
  private static String apiKey; 
  public static String apiUrl;


  public static void init() {
    model = IO.loadTextFile("configs/model.txt");
    apiKey = IO.loadTextFile("configs/apiKey.txt");
    apiUrl = "https://openrouter.ai/api/v1/chat/completions";
  }


  public static String ask(String message) {
 
    try {
      json = JSON.receiveMessage(message, model);
    } catch (Exception e) {
      System.err.println("Error! "+e);
      return "none";
    }

    request = HttpRequest.newBuilder()
      .uri(URI.create(apiUrl))
      .header("Authorization", "Bearer " + apiKey)
      .header("Content-Type", "application/json")
      .header("HTTP-Referer", "http://localhost")
      .header("X-Title", "VTuberBot")
      .POST(HttpRequest.BodyPublishers.ofString(json))
      .build();

    try {
      System.out.println("Request sent to "+model);
      response = http.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) throw new RuntimeException("API error: " + response.statusCode() + ": " + response.body());
      if (response != null) System.out.println("Got response from "+model+" response: "+response);
    } catch (Exception e) {
      System.err.println("Error! "+e);
      e.printStackTrace();
      return "none";
     }
    return JSON.parseJson(response.body());
  }
}
