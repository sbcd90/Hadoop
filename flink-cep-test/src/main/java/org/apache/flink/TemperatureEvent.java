package org.apache.flink;

public class TemperatureEvent extends MonitoringEvent {
  private double temp;

  public TemperatureEvent(int id, double temp) {
    super(id);
    this.temp = temp;
  }

  public double getTemp() {
    return temp;
  }

  public void setTemp(double temp) {
    this.temp = temp;
  }
}