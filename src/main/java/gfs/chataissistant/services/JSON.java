package gfs.chataissistant.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.List;

/**
 * JSON message wrapper 
 */
public class JSON {

  private static ObjectMapper mapper = new ObjectMapper();
  private static String bio;
  private static String rules;

  public static void init() {
    bio = Config.getConfig().bio;
    rules = Config.getConfig().rules;
  }

  public static String receiveMessage(String message, String model) throws Exception {

    Map<String, String> settings = Map.of("role", "system", "content", bio + "\n\n" + rules); 

    Map<String, String> userMsg = Map.of("role", "user", "content", message);

    Map<String, Object> root = Map.of("model", model, "messages", List.of(settings, userMsg));

    return mapper.writeValueAsString(root);
  }


  public static String parseJson(String json) {
    try {
      Map<String, Object> response = mapper.readValue(json, Map.class);

      List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");

      Map<String, Object> first = choices.get(0);
      Map<String, Object> msg = (Map<String, Object>) first.get("message");

      System.out.println(msg.get("content"));
      return (String) msg.get("content");
    } catch (Exception e) {
      System.err.println("Error! "+e);
      return "none";
    }
  }
}
