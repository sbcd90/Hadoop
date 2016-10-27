package com.sap.java;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class CompletableFutureCancellation {

  public CompletableFuture<Double> ask() {
    ExecutorService executor = Executors.newCachedThreadPool();

    CompletableFuture<Double> future =
      CompletableFuture.supplyAsync(new Supplier<Double>() {
        @Override
        public Double get() {
          try {
            Thread.sleep(10000);
            return 100.0;
          } catch (Exception e) {
            e.printStackTrace();
            return -100.0;
          }
        }
      }, executor);

    return future;
  }

  public static void main(String[] args) {
    CompletableFutureCancellation app = new CompletableFutureCancellation();

    CompletableFuture<Double> future = app.ask();

    future.cancel(true);

    System.out.println(future.join());
  }
}