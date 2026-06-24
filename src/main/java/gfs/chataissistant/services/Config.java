package gfs.chataissistant.services;

import org.yaml.snakeyaml.Yaml;

/**
 * Configs
 */
public class Config {

  private static String yml = IO.loadTextFile("config.yml");

  private static Configuration configuration;
  private static Yaml yaml = new Yaml();


  public static void update() {
    yml = IO.loadTextFile("config.yml");
    configuration = yaml.loadAs(yml, Configuration.class);
  }


  public static Configuration getConfig() {
    return configuration;
  }


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
  }
}
