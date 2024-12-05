package ch.heigvd.dai.commands;

import ch.heigvd.dai.server.ListenServer;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "server", description = "Start the server part of the network game.")
public class Server implements Callable<Integer> {

  @CommandLine.Option(
      names = {"-p", "--port"},
      description = "Port to use (default: ${DEFAULT-VALUE}).",
      defaultValue = "6433")
  protected int port;

  @CommandLine.Option(
      names = {"-m", "--max_games"},
      description =
          "Maximum number of simultaneous games hosted by the server (default : ${DEFAULT-VALUE}).",
      defaultValue = "10")
  protected int maxGames;

  @Override
  public Integer call() {
    ListenServer.run(port, maxGames);
    return 0;
  }
}
