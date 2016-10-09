package org.apache.akka.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.io.Tcp;
import org.apache.akka.clients.ClientActor;
import org.apache.akka.server.ServerActor;

import java.net.InetSocketAddress;

public class MainApplication {
  private static String host = "localhost";
  private static int port = 9090;

  public static void main(String[] args) {
    ActorSystem actorSystem = ActorSystem.create("ServerActorSystem");

    ActorRef serverActor = actorSystem.actorOf(ServerActor.props(null, host, port),
                            "serverActor");
//    ActorSystem clientActorSystem = ActorSystem.create("ClientActorSystem");
    ActorRef clientActor = actorSystem.actorOf(ClientActor.props(
      new InetSocketAddress(host, port), null), "clientActor");

    actorSystem.awaitTermination();
//    clientActorSystem.awaitTermination();
  }
}