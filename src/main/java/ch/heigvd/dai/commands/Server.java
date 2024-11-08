package ch.heigvd.dai.commands;

import ch.heigvd.dai.server.ListenServer;
import java.util.concurrent.Callable;
import picocli.CommandLine;

import ch.heigvd.dai.server.ListenServer;

@CommandLine.Command(name = "server", description = "Start the server part of the network game.")
public class Server implements Callable<Integer> {

  @CommandLine.Option(
      names = {"-p", "--port"},
      description = "Port to use (default: ${DEFAULT-VALUE}).",
      defaultValue = "6433")
  protected int port;

  @Override
  public Integer call() {
    ListenServer.run(port,10);
    return 0;
  }
}
