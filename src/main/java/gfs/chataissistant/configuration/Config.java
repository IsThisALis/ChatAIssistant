package gfs.chataissistant.configuration;

import org.yaml.snakeyaml.Yaml;

import gfs.chataissistant.util.IO;

/**
 * Config class. Loads and stores data from YAML configuration.
 */
public class Config {

  private static String yml = IO.loadTextFile("config.yml");
  private static Yaml yaml = new Yaml();
  private static Configuration configuration = yaml.loadAs(yml, Configuration.class);


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
