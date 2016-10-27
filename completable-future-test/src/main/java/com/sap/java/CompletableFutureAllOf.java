package com.sap.java;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureAllOf {

  public CompletableFuture<Void> ask() {
    ExecutorService executor = Executors.newCachedThreadPool();

    CompletableFuture<Double> futureFirst = new CompletableFuture<>();
    CompletableFuture.runAsync(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(10000);
          System.out.println("hit first thread");
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
          System.out.println("hit second thread");
          futureSecond.complete(200.0);
        } catch (Exception e) {
          e.printStackTrace();
          futureSecond.complete(-200.0);
        }
      }
    }, executor);

    List<CompletableFuture<Double> > futures = new ArrayList<>();
    futures.add(futureFirst);
    futures.add(futureSecond);

    CompletableFuture<Void> finalFuture =
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));

    return finalFuture;
  }

  public static void main(String[] args) {
    CompletableFutureAllOf app = new CompletableFutureAllOf();

    app.ask().join();
  }
}