package com.isthisalis.chataissistant.configuration;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

import lombok.Data;

/**
 * Config class. Loads and stores data from YAML configuration.
 */
public class Config {

  private YAMLMapper yaml = YAMLMapper.builder().propertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE).build();
  private Configuration configuration;
  private Logger logger = Logger.getGlobal();


  public Config(String path) {
    try {
      configuration = yaml.readValue(new File("config.yml"), Configuration.class);
    } catch (Exception e) {
      logger.warning("Encountered error: " + e);
      return;
    }  }


  public Config() {
    try {
      configuration = yaml.readValue(new File("config.yml"), Configuration.class);
    } catch (Exception e) {
      logger.warning("Encountered error: " + e);
      return;
    }  }


  public void update() {
    try {
      configuration = yaml.readValue(new File("config.yml"), Configuration.class);
    } catch (Exception e) {
      logger.warning("Encountered error: " + e);
      return;
    }  }


  public void update(String path) {
    try {
      configuration = yaml.readValue(new File(path), Configuration.class);
    } catch (Exception e) {
      logger.warning("Encountered error: " + e);
      return;
    }  }


  public Configuration getConfig() {
    System.out.println(configuration.commands);
    return configuration.trim();
  }


    @Data
  public static class Configuration {

    private Configuration() {}

    public String model;
    public String apiKey;
    public String clientId;
    public String askWord;
    public String channel;
    public String accessToken;
    public String bio;
    public String rules;
    public String apiProvider;
    public String apiUrl;
    public String dbUrl;
    public Map<String, String> commands;
    public String commandPrefix;
    public int warnsToMute;

    public Configuration trim() {
      if (apiUrl.isBlank()) apiUrl = "default";
      if (dbUrl.isBlank()) dbUrl = "default";
      return this;
    }

      @Override
    public String toString() {
      if (apiProvider.equals("lms")) return "Model: " + model
        + "\n API key: " + apiKey
        + "\n Client ID: " + clientId
        + "\n Ask command: " + askWord
        + "\n Channel: " + channel
        + "\n Access Token: " + accessToken
        + "\n Biography: " + bio
        + "\n Rules: " + rules
        + "\n API Provider: LM Studio Server"
        + "\n API URL: " + apiUrl
        + "\n Database URL: " + dbUrl;

      return "Model: " + model
        + "\n API key: " + apiKey
        + "\n Client ID: " + clientId
        + "\n Ask command: " + askWord
        + "\n Channel: " + channel
        + "\n Access Token: " + accessToken
        + "\n Biography: " + bio
        + "\n Rules: " + rules
        + "\n API Provider: OpenRouter"
        + "\n API URL: " + apiUrl
        + "\n Database URL: " + dbUrl;
    } 
  }
}
