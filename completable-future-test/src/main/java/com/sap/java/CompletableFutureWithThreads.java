package com.sap.java;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureWithThreads {

  public CompletableFuture ask() {
    final CompletableFuture<Double> future = new CompletableFuture<Double>();
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(20000);
          future.complete(100.0);
        } catch (Exception e) {
          e.printStackTrace();
          future.complete(-100.0);
        }
      }
    });
    thread.start();
    return future;
  }

  public static void main(String[] args)
    throws ExecutionException, InterruptedException {
    CompletableFutureWithThreads app = new CompletableFutureWithThreads();
    System.out.println(app.ask().join());
  }
}