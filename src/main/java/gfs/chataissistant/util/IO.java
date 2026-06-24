package gfs.chataissistant.util;

  // io
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

  // nio 
import java.nio.charset.StandardCharsets;

  /**
   * IO util class for loading String data
   */
public class IO {


    static {
      Path logs = Path.of("logs");
      Path ignored = Path.of("logs", "ignored.log");
      
      try {
        if (!Files.isDirectory(logs)) {
          Files.createDirectory(logs);
          Files.createFile(ignored);
        }
      } catch (IOException e) {
        System.err.println("Unable to create 'logs/' folder: " + e);
      }
    }
  
      /**
      * Used to load files to String.
      * @param path path to file need to be loaded.
      * @return File source in String.
      */ 
    public static String loadTextFile(String path) {
    
    try {
      Path fpath = Paths.get(path);

      if (!Files.exists(fpath)) {
          throw new IOException("Cannot find file " + "[ " +path+ " ]");
      }

      byte[] bytes = Files.readAllBytes(fpath);
      return new String(bytes, StandardCharsets.UTF_8).trim();
      
    } catch(IOException e) {
        throw new RuntimeException("Encountered unknown error while loading file " + "[ " + path + " ]" + "[ " + e + " ]");
    }
  }
}
