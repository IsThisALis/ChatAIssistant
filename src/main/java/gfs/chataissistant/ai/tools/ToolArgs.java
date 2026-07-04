package gfs.chataissistant.ai.tools;

/**
 * ToolArgs
 */
public record ToolArgs(int limit, String user) {
  public ToolArgs(int limit) {
    this(limit, null);
  }
}
