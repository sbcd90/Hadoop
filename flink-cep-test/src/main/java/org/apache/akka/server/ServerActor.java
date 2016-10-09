package org.apache.akka.server;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class ServerActor extends UntypedActor {
  private Logger LOG = LoggerFactory.getLogger(getClass());

  private ActorRef tcpActor;
  private String host;
  private int port;

  public ServerActor(ActorRef tcpActor,
                     String host,
                     int port) {
    this.tcpActor = tcpActor;
    this.host = host;
    this.port = port;
  }

  public static Props props(ActorRef tcpActor,
                            String host, int port) {
    return Props.create(ServerActor.class, tcpActor, host, port);
  }

  @Override
  public void preStart() throws Exception {
    if (tcpActor == null) {
      tcpActor = Tcp.get(getContext().system()).manager();
    }

    tcpActor.tell(TcpMessage.bind(getSelf(),
      new InetSocketAddress(host, port), 100), getSelf());
  }

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Tcp.Bound) {
      LOG.info("In Server Actor - received message Bound");
    } else if (message instanceof Tcp.CommandFailed) {
      getContext().stop(getSelf());
    } else if (message instanceof Tcp.Connected) {
      Tcp.Connected connected = (Tcp.Connected) message;
      LOG.info("In Server Actor - received message Connected");
      ActorRef handler = getContext().actorOf(
        Props.create(HandlerActor.class));

//      getSender().tell(TcpMessage.register(handler), getSelf());
    } else if (message instanceof Tcp.Received) {
      String data = ((Tcp.Received) message).data().utf8String();
      System.out.println(data);
      LOG.info("in Handler Actor - received data");
      getSender().tell(TcpMessage.write(
        ByteString.fromArray(("echo " + data).getBytes())), getSelf());
    }
  }
}