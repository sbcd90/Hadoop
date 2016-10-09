package org.apache.remote.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class MainApplication {

  public Config getLocalActorConfig() {
    String configFile = getClass().getClassLoader()
      .getResource("local_application.conf").getFile();
    Config config = ConfigFactory.parseFile(new File(configFile));
    return config;
  }

  public Config getRemoteActorConfig() {
    String configFile = getClass().getClassLoader()
      .getResource("remote_application.conf").getFile();
    Config config = ConfigFactory.parseFile(new File(configFile));
    return config;
  }

  public static void main(String[] args) {
    MainApplication app = new MainApplication();

    Config localConfig = app.getLocalActorConfig();

    ActorSystem localSystem = ActorSystem.create("ClientSystem", localConfig);
    ActorRef localActor = localSystem.actorOf(Props.create(LocalActor.class), "local");
    System.out.println("Local Actor is ready");

    Config remoteConfig = app.getRemoteActorConfig();

    ActorSystem remoteSystem = ActorSystem.create("RemoteSystem", remoteConfig);
    ActorRef remoteActor = remoteSystem.actorOf(Props.create(RemoteActor.class), "remote");
    System.out.println("Remote Actor is ready");
  }
}