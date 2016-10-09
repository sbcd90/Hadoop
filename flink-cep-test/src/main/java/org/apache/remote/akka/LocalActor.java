package org.apache.remote.akka;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

public class LocalActor extends UntypedActor {

  @Override
  public void preStart() throws Exception {
    ActorSelection remoteActor = context().actorSelection(
      "akka.tcp://RemoteSystem@127.0.0.1:5150/user/remote");
    remoteActor.tell("Hi", getSelf());
  }

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof String) {
      System.out.println(message);
    } else {
      System.out.println("message format cannot be detected");
    }
  }
}