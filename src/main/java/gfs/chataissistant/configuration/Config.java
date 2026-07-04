package gfs.chataissistant.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.util.logging.Logger;

import lombok.Data;

/**
 * Config class. Loads and stores data from YAML configuration.
 */
public class Config {

  private ObjectMapper yaml = new ObjectMapper(new YAMLFactory());
  private Configuration configuration;
  private Logger logger = Logger.getGlobal();


  public Config(String path) {
    yaml.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
    try {
      configuration = yaml.readValue(new File("config.yml"), Configuration.class);
    } catch (Exception e) {
      logger.warning("Encountered error: " + e);
      return;
    }  }


  public Config() {
    yaml.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
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
    return configuration;
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


    public void sysout() {
      if (apiProvider.toLowerCase().equals("lms")) { System.out.println("Model: " + model
        + "\n API key: secret"
        + "\n Client ID: " + clientId
        + "\n Ask command: " + askWord
        + "\n Channel: " + channel
        + "\n Access Token: secret"
        + "\n Biography: " + bio
        + "\n Rules: " + rules
        + "\n API Provider: LM Studio Server"
        + "\n API URL: " + apiUrl
        + "\n Database URL: " + dbUrl); return; }

      if (apiProvider.toLowerCase().equals("openrouter")) { System.out.println("Model: " + model
        + "\n API key: secret"
        + "\n Client ID: " + clientId
        + "\n Ask command: " + askWord
        + "\n Channel: " + channel
        + "\n Access Token: secret"
        + "\n Biography: " + bio
        + "\n Rules: " + rules
        + "\n API Provider: OpenRouter"
        + "\n API URL: " + apiUrl
        + "\n Database URL: " + dbUrl); return; }
    }
  }
}
