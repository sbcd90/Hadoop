package org.apache.spark;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import org.apache.spark.examples.streaming.SubscribeReceiver;

public class FeederActor extends UntypedActor {

  @Override
  public void onReceive(Object message)
    throws Exception {
    if (message instanceof SubscribeReceiver) {
      ActorRef sender = getSender();

      while (true) {
        sender.tell("Hello World", getSelf());
        Thread.sleep(200);
      }
    }
  }

  @Override
  public void postStop() throws Exception {
    System.out.println("Hello World");
  }
}