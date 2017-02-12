package com.sap.junits;

public class NameContext {

  private HelloWorld helloWorld;

  public NameContext(String name) {
    this.helloWorld = new HelloWorld("Hello", name);
    HelloWorld.WORLD = name;
  }

  public void setName(String name) {
    this.helloWorld.setHello(name);
    HelloWorld.WORLD = name;
  }

  public String getName() {
    return this.helloWorld.getWorld();
  }
}