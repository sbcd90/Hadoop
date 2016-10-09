package org.apache.flink;

public abstract class MonitoringEvent {
  private int id;

  public MonitoringEvent(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}