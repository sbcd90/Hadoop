package com.sap.junits;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class HelloWorldTest {
  private HelloWorld helloWorld;

  @ClassRule
  public static NameContextResource nameContextResource = new NameContextResource();

  @Before
  public void beforeTest() {
    this.helloWorld = new HelloWorld("Hello", "World");
  }

  @Test
  public void testHello() {
    Assert.assertEquals(helloWorld.getHello(), "Hello");
  }

  @Test
  public void testWorld() {
    Assert.assertEquals(helloWorld.getWorld(), "World");
  }

  @Test
  public void testPrintWorld() {
    /**
     * this test shows how to use class rules in Junits
     */
    Assert.assertEquals(HelloWorld.WORLD, "Subhobrata");
  }
}