package org.apache.akka.clients;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.japi.Procedure;
import akka.util.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class ClientActor extends UntypedActor {
  private Logger LOG = LoggerFactory.getLogger(getClass());

  private ActorRef tcpActor;
  private InetSocketAddress remote;

  public ClientActor(InetSocketAddress remote,
                     ActorRef tcpActor) {
    this.remote = remote;
    this.tcpActor = tcpActor;

    if (tcpActor == null) {
      this.tcpActor = Tcp.get(getContext().system()).manager();
    }

    this.tcpActor.tell(TcpMessage.connect(remote), getSelf());
  }

  public static Props props(InetSocketAddress remote,
                            ActorRef tcpActor) {
    return Props.create(ClientActor.class, remote, tcpActor);
  }

  public void onReceive(Object message) throws Exception {
    if (message instanceof Tcp.CommandFailed) {
      LOG.info("In ClientActor - received message: failed");
      getContext().stop(getSelf());
    } else if (message instanceof Tcp.Connected) {
      LOG.info("In ClientActor - received message: connected");

      getSender().tell(TcpMessage.register(getSelf()), getSelf());
      getContext().become(connected(getSender()));
      getSender().tell(
        TcpMessage.write(ByteString.fromArray("hello".getBytes())), getSelf());
    }
  }

  private Procedure<Object> connected(final ActorRef connection) {
    return new Procedure<Object>() {
      public void apply(Object param) throws Exception {
        if (param instanceof ByteString) {
          connection.tell(TcpMessage.write((ByteString) param), getSelf());
        } else if (param instanceof Tcp.CommandFailed) {

        } else if (param instanceof Tcp.Received) {
          LOG.info("In ClientActor - Received message: " + ((Tcp.Received) param).data().utf8String());
        } else if (param.equals("close")) {
          connection.tell(TcpMessage.close(), getSelf());
        } else if (param instanceof Tcp.ConnectionClosed) {
          getContext().stop(getSelf());
        }
      }
    };
  }
}