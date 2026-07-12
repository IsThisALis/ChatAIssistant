package com.isthisalis.chataissistant.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.isthisalis.chataissistant.DTO.request.AiRequest;
import com.isthisalis.chataissistant.DTO.request.Message;
import com.isthisalis.chataissistant.DTO.request.Tool;
import com.isthisalis.chataissistant.DTO.ToolCall;
//import com.isthisalis.chataissistant.DTO.request.Function;
import com.isthisalis.chataissistant.DTO.response.AiResponse;

import com.isthisalis.chataissistant.ai.AiService;
import com.isthisalis.chataissistant.ai.tools.ToolArgs;
import com.isthisalis.chataissistant.ai.tools.getChatHistory;

import com.isthisalis.chataissistant.configuration.Config.Configuration;

import java.util.List;
import java.util.logging.Logger;

/**
 * JSON parser. Wraps and unwraps content.
 */
public class JSON {

  private static Logger logger = Logger.getGlobal();
  private ObjectMapper mapper = new ObjectMapper();
  private AiService ai;

  private String bio;
  private String rules;
  private String model;
  private List<Tool> tools;

  public JSON(Configuration config, AiService ai) {
    model = config.model;
    bio = config.bio;
    rules = config.rules;
    makeTools();
    this.ai = ai;
  }

  public String makeAiRequest(String message) throws Exception {
    var settings = new Message("system", "Remember: your creator - IsThisALis" + bio + "\n\n" + rules);
    var userMsg = new Message("user", message);
    var request = new AiRequest(model, List.of(settings, userMsg), tools);

    return mapper.writeValueAsString(request);
  }


  public String makeAiRequest(List<Message> history) throws Exception {
    var req = new AiRequest(model, history, tools);

    return mapper.writeValueAsString(req);
  }

  public String parseAiResponse(List<Message> currHistory, String json) {
    String newResp = null;
    try {
      var response = mapper.readValue(json, AiResponse.class);
      var choice = response.choices().get(0);
      var msg = choice.message();

      if (msg.content() != null && !msg.content().isBlank()) { logger.info(msg.content()); return msg.content(); }

      if (msg.toolCalls() != null && !msg.toolCalls().isEmpty()) {
        logger.info("Tool Called by ChatAIssistant:" + model);
        currHistory.add(new Message(msg.role(), msg.content(), msg.toolCalls()));

        for (ToolCall toolCall : msg.toolCalls()) {
          String toolName = toolCall.function().name();
          String toolId = toolCall.id();

          ToolArgs args = mapper.readValue(toolCall.function().arguments(), ToolArgs.class);

          String toolResult = "Unknown tool";
          logger.info("Tool called by ChatAIssistant (" + model + "). Executing: " + toolName + " args: " + args);

          if (toolName.equals("get_chat_history")) {
            var history = getChatHistory.getChatHistory(args.limit());
            toolResult = mapper.writeValueAsString(history);
          }

          if (toolName.equals("get_chat_history_by_user")) {
            var history = getChatHistory.getChatHistory(args.user(), args.limit());
            toolResult = mapper.writeValueAsString(history);
          }

          if (toolName.equals("get_chat_history_by_user_no_limit")) {
            var history = getChatHistory.getChatHistory(args.user());
            toolResult = mapper.writeValueAsString(history);
          }

          currHistory.add(new Message(toolId, toolName, toolResult));
        }

        var newReq = new AiRequest(model, currHistory, tools);
        String newJson = mapper.writeValueAsString(newReq);

        newResp = ai.ask(newJson); 
      }

      return parseAiResponse(currHistory, newResp);

    } catch (Exception e) {
      logger.warning("" + e);
      return "none";
    }
  }


  public List<Message> createInitialStory(String msg) {
      var settings = new Message("system", "Remember: your creator - IsThisALis" + bio + "\n\n" + rules);
      var usrMsg = new Message("user", msg);
      return new java.util.ArrayList<>(List.of(settings, usrMsg));
  }


  public void makeTools() {
    if (tools != null) return;

    String json = IO.loadTextFile("tools.json");
    try {
      tools = mapper.readValue(json, new TypeReference<List<Tool>>() {});
    } catch (Exception e) {
      logger.warning("" + e);
    }
  }
}
