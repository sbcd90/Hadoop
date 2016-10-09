package org.apache.flink;

public class TemperatureWarning {
  private int id;
  private double averageTemperature;

  public TemperatureWarning(int id, double averageTemperature) {
    this.id = id;
    this.averageTemperature = averageTemperature;
  }

  public TemperatureWarning() {
    this(-1, -1);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getAverageTemperature() {
    return averageTemperature;
  }

  public void setAverageTemperature(double averageTemperature) {
    this.averageTemperature = averageTemperature;
  }
}