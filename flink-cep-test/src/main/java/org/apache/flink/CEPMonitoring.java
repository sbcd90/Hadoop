package org.apache.flink;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.IngestionTimeExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Map;

public class CEPMonitoring {
  private static final double TEMPERATURE_THRESHOLD = 100;
  private static final int MAX_RACK_ID = 10;
  private static final long PAUSE = 100;
  private static final double TEMPERATURE_RATIO = 0.5;
  private static final double POWER_STD = 10;
  private static final double POWER_MEAN = 100;
  private static final double TEMP_STD = 20;
  private static final double TEMP_MEAN = 80;

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    DataStream<MonitoringEvent> inputEventStream = env
      .addSource(new MonitoringEventSource(MAX_RACK_ID,
                                           PAUSE,
                                           TEMPERATURE_RATIO,
                                           POWER_STD,
                                           POWER_MEAN,
                                           TEMP_STD,
                                           TEMP_MEAN))
      .assignTimestampsAndWatermarks(new IngestionTimeExtractor<MonitoringEvent>());

    Pattern<MonitoringEvent, ?> warningPattern = Pattern.<MonitoringEvent>begin("first")
//      .subtype(TemperatureEvent.class)
/*      .where(new FilterFunction<TemperatureEvent>() {
        public boolean filter(TemperatureEvent temperatureEvent) throws Exception {
          if (temperatureEvent.getTemp() >= TEMPERATURE_THRESHOLD) {
            return true;
          }
          return false;
        }
      }) */
      .where(new FilterFunction<MonitoringEvent>() {
        public boolean filter(MonitoringEvent monitoringEvent) throws Exception {
          return false;
        }
      })
/*      .next("second")
      .subtype(TemperatureEvent.class)
      .where(new FilterFunction<TemperatureEvent>() {
        public boolean filter(TemperatureEvent temperatureEvent) throws Exception {
          if (temperatureEvent.getTemp() >= TEMPERATURE_THRESHOLD) {
            return true;
          }
          return false;
        }
      }) */
      .within(Time.seconds(10));

    PatternStream<MonitoringEvent> tempPatternStream = CEP.pattern(
      inputEventStream.keyBy("id"),
      warningPattern
    );

    DataStream<TemperatureWarning> warnings = tempPatternStream.select(new PatternSelectFunction<MonitoringEvent, TemperatureWarning>() {
      public TemperatureWarning select(Map<String, MonitoringEvent> map) throws Exception {
        TemperatureEvent first = (TemperatureEvent) map.get("first");
        TemperatureEvent second = (TemperatureEvent) map.get("first");
//        TemperatureEvent second = (TemperatureEvent) map.get("second");

        System.out.println("Key: " + first.getId() + " Value: " + ((first.getTemp() + second.getTemp()) / 2));
        return new TemperatureWarning(first.getId(), (first.getTemp() + second.getTemp()) / 2);
      }
    });

//    warnings.print();

    env.execute("CEP Monitoring job");
  }
}