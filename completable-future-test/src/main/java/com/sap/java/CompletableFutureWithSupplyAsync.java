package com.sap.java;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CompletableFutureWithSupplyAsync {

  public CompletableFuture<Double> supplyAsyncWithoutExecutor() {
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
      });
    return future;
  }

  public CompletableFuture<Double> supplyAsyncWithExecutor() {
    CompletableFuture<Double> future =
      CompletableFuture.supplyAsync(new Supplier<Double>() {
        @Override
        public Double get() {
          try {
            Thread.sleep(10000);
            return 200.0;
          } catch (Exception e) {
            e.printStackTrace();
            return -200.0;
          }
        }
      });

    return future;
  }

  public static void main(String[] args) {
    CompletableFutureWithSupplyAsync app = new CompletableFutureWithSupplyAsync();

    CompletableFuture<Double> futureWithoutExecutor = app.supplyAsyncWithoutExecutor();
    CompletableFuture<Double> futureWithExecutor = app.supplyAsyncWithExecutor();

    if (futureWithoutExecutor != null) {
      System.out.println(futureWithoutExecutor.join());
    }

    if (futureWithExecutor != null) {
      System.out.println(futureWithExecutor.join());
    }
  }
}