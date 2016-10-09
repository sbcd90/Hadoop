package org.apache.akka.server;

import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerActor extends UntypedActor {
  private final Logger LOG = LoggerFactory.getLogger(getClass());

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Tcp.Received) {
      String data = ((Tcp.Received) message).data().utf8String();
      LOG.info("in Handler Actor - received data");
      getSender().tell(TcpMessage.write(
        ByteString.fromArray(("echo " + data).getBytes())), getSelf());
    } else if (message instanceof Tcp.ConnectionClosed) {
      getContext().stop(getSelf());
    }
  }
}