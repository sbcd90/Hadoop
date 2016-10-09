package org.apache.spark;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.examples.streaming.SubscribeReceiver;
import org.apache.spark.examples.streaming.UnsubscribeReceiver;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.akka.AkkaUtils;
import org.apache.spark.streaming.akka.JavaActorReceiver;
import org.apache.spark.streaming.akka.SingleItemData;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.io.File;
import java.util.Arrays;

class JavaSampleActorReceiver<T> extends JavaActorReceiver
  implements java.io.Serializable {
  private final String urlOfPublisher;
  private ActorSelection remotePublisher;

  public JavaSampleActorReceiver(String urlOfPublisher) {
    this.urlOfPublisher = urlOfPublisher;
  }

  @Override
  public void onReceive(Object message) throws Exception {
    @SuppressWarnings("unchecked")
    T messageT = (T) message;
    context().parent().tell(new SingleItemData<T>(messageT), getSelf());
//    store(messageT);
  }

  @Override
  public void preStart() {
    remotePublisher = getContext().actorSelection(urlOfPublisher);
    remotePublisher.tell(new SubscribeReceiver(getSelf()), getSelf());
  }

  @Override
  public void postStop() {
    remotePublisher.tell(new UnsubscribeReceiver(getSelf()), getSelf());
  }
}

public class AkkaSparkWordCount {
  private final JavaStreamingContext jsc;

  private final String feederActorUri;

  private static FlatMapFunction<String, String> flatMapFunction = new FlatMapFunction<String, String>() {
    public Iterable<String> call(String s) throws Exception {
      return Arrays.asList(s.split("\\s+"));
    }
  };

  private static PairFunction<String, String, Integer> pairFunction = new PairFunction<String, String, Integer>() {
    public Tuple2<String, Integer> call(String s) throws Exception {
      return new Tuple2<String, Integer>(s, 1);
    }
  };

  private static Function2<Integer, Integer, Integer> function2 = new Function2<Integer, Integer, Integer>() {
    public Integer call(Integer integer, Integer integer2) throws Exception {
      return integer + integer2;
    }
  };

  public AkkaSparkWordCount(String host, String port) {
    SparkConf conf = new SparkConf().setAppName("AkkaSparkWordCount").setMaster("local[*]");
    jsc = new JavaStreamingContext(conf, new Duration(2000));
    jsc.sc().setLogLevel("WARN");

    feederActorUri = "akka.tcp://test@" + host + ":" + port + "/user/FeederActor";
  }

  public void doStreamOps() {
    JavaDStream<String> lines = AkkaUtils.createStream(jsc,
      Props.create(JavaSampleActorReceiver.class, feederActorUri), "SampleReceiver");

    lines.flatMap(flatMapFunction)
      .mapToPair(pairFunction)
      .reduceByKey(function2).print();
  }

  public void start() {
    jsc.start();
    jsc.awaitTermination();
  }

  private static final String host = "127.0.0.1";
  private static final String port = "8080";

  public Config getFeederActorConfig() {
    String configFile = getClass().getClassLoader()
      .getResource("feeder_application.conf").getFile();
    Config config = ConfigFactory.parseFile(new File(configFile));
    return config;
  }

  public static void main(String[] args) {
    AkkaSparkWordCount wordCount = new AkkaSparkWordCount(host, port);
    ActorSystem system = ActorSystem.create("test", wordCount.getFeederActorConfig());

    ActorRef feederActor = system.actorOf(Props.create(FeederActor.class), "FeederActor");
    System.out.println("Feeder actor is ready");

    wordCount.doStreamOps();
    wordCount.start();
  }
}