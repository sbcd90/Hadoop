package org.apache.flink;

public class PowerEvent extends MonitoringEvent {
  private double voltage;

  public PowerEvent(int id, double voltage) {
    super(id);
    this.voltage = voltage;
  }

  public double getVoltage() {
    return voltage;
  }

  public void setVoltage(double voltage) {
    this.voltage = voltage;
  }
}