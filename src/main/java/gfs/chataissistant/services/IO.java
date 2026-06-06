package gfs.chataissistant.services;

  // io
import java.io.InputStream;
import java.io.IOException;

  // nio 
import java.nio.charset.StandardCharsets;

  /**
   * IO util class for loading String data
   */
public class IO {

  
      /**
      * Used to load files to String.
      * @param path path to file need to be loaded.
      * @return File source in String.
      */ 
    public static String loadTextFile(String path) {
    
    try (InputStream stream = IO.class.getClassLoader().getResourceAsStream(path);) {

      if (stream == null) {
          throw new IOException("Cannot find file " + "[ " +path+ " ]");
      }

      return new String(stream.readAllBytes(), StandardCharsets.UTF_8).trim();
    } catch(IOException e) {
        throw new RuntimeException("Encountered unknown error while loading file " + "[ " + path + " ]" + "[ " + e + " ]");
    }
  }
}
