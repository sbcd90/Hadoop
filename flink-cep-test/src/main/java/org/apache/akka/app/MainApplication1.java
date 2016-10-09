package org.apache.akka.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.testkit.TestProbe;
import akka.util.ByteString;
import org.apache.akka.clients.ClientActor;
import org.apache.akka.server.ServerActor;

import java.net.InetSocketAddress;

public class MainApplication1 {
  private static ActorSystem actorSystem =
    ActorSystem.create("actor-system");

  public void testClientActor() {
    InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
    InetSocketAddress inetSocketAddressLocal = new InetSocketAddress("localhost", 8081);

    TestProbe tcpProbe = new TestProbe(actorSystem);

    ActorRef tcpRef = tcpProbe.ref();
    ActorRef clientActor = actorSystem.actorOf(ClientActor.props(inetSocketAddress,
      tcpRef), "clientActor");

    tcpProbe.expectMsg(TcpMessage.connect(inetSocketAddress));
    tcpProbe.send(clientActor, new Tcp.Connected(inetSocketAddress, inetSocketAddressLocal));

    tcpProbe.expectMsg(TcpMessage.register(clientActor));

    String hello = "hello";
    tcpProbe.expectMsg(TcpMessage.write(ByteString.fromArray(hello.getBytes())));

    tcpProbe.send(clientActor, new Tcp.Received(
      ByteString.fromArray(("echo" + hello).getBytes())));
  }

  public void testServerActor() {
    InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
    InetSocketAddress inetSocketAddress1 = new InetSocketAddress("localhost", 8081);

    TestProbe tcpProbe = new TestProbe(actorSystem);

    ActorRef tcpRef = tcpProbe.ref();
    ActorRef serverActor = actorSystem.actorOf(ServerActor.props(tcpRef,
      inetSocketAddress.getHostName(), inetSocketAddress.getPort()), "serverActor");

    tcpProbe.expectMsg(TcpMessage.bind(serverActor, inetSocketAddress, 100));
    tcpProbe.send(serverActor, new Tcp.Bound(inetSocketAddress));

    tcpProbe.send(serverActor, new Tcp.Connected(inetSocketAddress, inetSocketAddress1));

    tcpProbe.send(serverActor, new Tcp.Received(ByteString.fromArray("hello".getBytes())));
    tcpProbe.expectMsg(TcpMessage.write(ByteString.fromArray("echo hello".getBytes())));
  }

  public static void main(String[] args) {
    MainApplication1 app = new MainApplication1();
//    app.testClientActor();
    app.testServerActor();
  }
}