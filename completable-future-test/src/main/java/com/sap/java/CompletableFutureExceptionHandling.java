package com.sap.java;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompletableFutureExceptionHandling {

  public CompletableFuture<Double> ask() {
    ExecutorService executor = Executors.newCachedThreadPool();

    CompletableFuture<Double> future =
      CompletableFuture.supplyAsync(new Supplier<Double>() {
        @Override
        public Double get() {
          try {
            Thread.sleep(10000);
            int exception = 1 / 0;
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
    CompletableFutureExceptionHandling app = new CompletableFutureExceptionHandling();

    CompletableFuture<Double> future = app.ask();

    future.exceptionally(new Function<Throwable, Double>() {
      @Override
      public Double apply(Throwable throwable) {
        if (throwable != null) {
          System.out.println(throwable.getMessage());
          return null;
        } else {
          return future.join();
        }
      }
    });
  }
}