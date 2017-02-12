package com.sap.junits;

public class HelloWorld {

  private String hello;
  private String world;
  public static String WORLD = "World";

  public HelloWorld(String hello, String world) {
    this.hello = hello;
    this.world = world;
  }

  public void setHello(String hello) {
    this.hello = hello;
  }

  public String getHello() {
    return hello;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public String getWorld() {
    return world;
  }

  public String printWORLD() {
    return WORLD;
  }
}