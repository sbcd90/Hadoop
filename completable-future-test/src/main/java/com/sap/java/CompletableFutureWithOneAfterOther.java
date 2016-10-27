package com.sap.java;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompletableFutureWithOneAfterOther {

  public CompletableFuture<Double> ask() {
    ExecutorService executor = Executors.newCachedThreadPool();

    CompletableFuture<Double> futureFirst =
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

    CompletableFuture<Double> futureSecond =
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

    return futureSecond.thenComposeAsync(new Function<Double, CompletionStage<Double>>() {
      @Override
      public CompletionStage<Double> apply(Double aDouble) {
        System.out.println(aDouble);
        System.out.println("Calling the second future...");
        return futureFirst;
      }
    });
  }

  public static void main(String[] args) {
    CompletableFutureWithOneAfterOther app = new CompletableFutureWithOneAfterOther();

    System.out.println(app.ask().join());
  }
}