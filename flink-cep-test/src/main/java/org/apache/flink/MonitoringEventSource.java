package org.apache.flink;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.util.Random;

public class MonitoringEventSource
  extends RichParallelSourceFunction<MonitoringEvent> {
  private boolean running = true;

  private final int id;

  private final long pause;

  private final double temperatureRatio;

  private final double powerStd;

  private final double powerMean;

  private final double temperatureStd;

  private final double temperatureMean;

  private Random random;

  private int shard;

  private int offset;

  public MonitoringEventSource(int id,
                               long pause,
                               double temperatureRatio,
                               double powerStd,
                               double powerMean,
                               double temperatureStd,
                               double temperatureMean) {
    this.id = id;
    this.pause = pause;
    this.temperatureRatio = temperatureRatio;
    this.powerStd = powerStd;
    this.powerMean = powerMean;
    this.temperatureStd = temperatureStd;
    this.temperatureMean = temperatureMean;
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    int numberTasks = getRuntimeContext().getNumberOfParallelSubtasks();
    int index = getRuntimeContext().getIndexOfThisSubtask();

    offset = (int)((double) id / numberTasks * index);
    shard = (int)((double) id / numberTasks * (index + 1)) - offset;

    random = new Random();
  }

  public void run(SourceContext<MonitoringEvent> sourceContext) throws Exception {
    while (running) {
      MonitoringEvent monitoringEvent;

      int id = random.nextInt(shard) + offset;

      if (random.nextDouble() >= temperatureRatio) {
        double power = random.nextGaussian() * powerStd + powerMean;
        monitoringEvent = new PowerEvent(id, power);
      } else {
        double temperature = random.nextGaussian() * temperatureStd + temperatureMean;
        monitoringEvent = new TemperatureEvent(id, temperature);
      }

      sourceContext.collect(monitoringEvent);
      Thread.sleep(pause);
    }
  }

  public void cancel() {
    running = false;
  }
}