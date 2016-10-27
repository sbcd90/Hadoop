package com.sap.java;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureAnyOf {

  public CompletableFuture<Object> ask() {
    ExecutorService executor = Executors.newCachedThreadPool();

    CompletableFuture<Double> futureFirst = new CompletableFuture<>();
    CompletableFuture.runAsync(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(10000);
          System.out.println("first Thread is hit");
          futureFirst.complete(100.0);
        } catch (Exception e) {
          e.printStackTrace();
          futureFirst.complete(-100.0);
        }
      }
    }, executor);

    CompletableFuture<Double> futureSecond = new CompletableFuture<>();
    CompletableFuture.runAsync(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(10000);
          System.out.println("Second thread is hit");
          futureSecond.complete(200.0);
        } catch (Exception e) {
          e.printStackTrace();
          futureSecond.complete(-200.0);
        }
      }
    });

    List<CompletableFuture<Double> > futures = new ArrayList<>();
    futures.add(futureFirst);
    futures.add(futureSecond);

    CompletableFuture<Object> finalFuture =
      CompletableFuture.anyOf(futures.toArray(new CompletableFuture[futures.size()]));

    return finalFuture;
  }

  public static void main(String[] args) {
    CompletableFutureAnyOf app = new CompletableFutureAnyOf();

    app.ask().join();
  }
}