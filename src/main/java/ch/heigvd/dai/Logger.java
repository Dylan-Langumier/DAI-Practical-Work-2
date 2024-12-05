package ch.heigvd.dai;

import java.sql.Timestamp;

public class Logger {
  private static final String ANSI_BASE = "\u001B[";
  private static final String ANSI_RESET = ANSI_BASE + "0m";
  private static final String ANSI_BLACK = ANSI_BASE + "30m";
  private static final String ANSI_RED = ANSI_BASE + "31m";
  private static final String ANSI_GREEN = ANSI_BASE + "32m";
  private static final String ANSI_YELLOW = ANSI_BASE + "33m";
  private static final String ANSI_BLUE = ANSI_BASE + "34m";
  private static final String ANSI_PURPLE = ANSI_BASE + "35m";
  private static final String ANSI_CYAN = ANSI_BASE + "36m";
  private static final String ANSI_WHITE = ANSI_BASE + "37m";

  private final String prefix;

  public Logger(String prefix) {
    this.prefix = prefix;
  }

  private Timestamp now() {
    return new Timestamp(System.currentTimeMillis());
  }

  private String prefix() {
    return String.format("[%s%s @ %s%s]", ANSI_CYAN, prefix, now(), ANSI_WHITE);
  }

  private void print(String color, String message) {
    System.out.printf("%s: %s%s%s%n", prefix(), color, message, ANSI_RESET);
  }

  public void error(String message, Object... args) {
    print(ANSI_RED, String.format(message, args));
  }

  public void info(String message, Object... args) {
    print(ANSI_WHITE, String.format(message, args));
  }

  public void warn(String message, Object... args) {
    print(ANSI_YELLOW, String.format(message, args));
  }

  public void request(String message, Object... args) {
    print(ANSI_PURPLE, String.format(message, args));
  }
}
