package com.sap.java;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CompletableFutureWithEitherCons {

  public void ask() {
    ExecutorService executor = Executors.newCachedThreadPool();

    CompletableFuture<Double> futureFirst =
      CompletableFuture.supplyAsync(new Supplier<Double>() {
        @Override
        public Double get() {
          try {
            Thread.sleep(11000);
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
      }, executor);

    futureFirst.acceptEither(futureSecond, new Consumer<Double>() {
      @Override
      public void accept(Double aDouble) {
        System.out.println(aDouble);
      }
    });
  }

  public static void main(String[] args) {
    CompletableFutureWithEitherCons app = new CompletableFutureWithEitherCons();

    app.ask();
  }
}