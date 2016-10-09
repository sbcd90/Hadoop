package org.apache.remote.akka;

import akka.actor.UntypedActor;

public class RemoteActor extends UntypedActor {

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof String) {
      getSender().tell("echo " + message, getSelf());
    } else {
      System.out.println("the message format is unknown");
    }
  }
}