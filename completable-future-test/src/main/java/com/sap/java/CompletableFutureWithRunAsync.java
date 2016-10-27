package com.sap.java;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureWithRunAsync {

  public CompletableFuture<Double> runAsyncWithExecutor() {
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    CompletableFuture<Double> future = new CompletableFuture<>();

    CompletableFuture.runAsync(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(10000);
          future.complete(100.0);
        } catch (Exception e) {
          e.printStackTrace();
          future.complete(-100.0);
        }
      }
    }, executorService);
    return future;
  }

  public CompletableFuture<Double> runAsyncWithoutExecutor() {
    CompletableFuture<Double> future = new CompletableFuture<>();

    CompletableFuture.runAsync(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(10000);
          future.complete(200.0);
        } catch (Exception e) {
          e.printStackTrace();
          future.complete(-200.0);
        }
      }
    });
    return future;
  }

  public static void main(String[] args) {
    CompletableFutureWithRunAsync app = new CompletableFutureWithRunAsync();

    CompletableFuture<Double> futureWithoutExecutor = app.runAsyncWithoutExecutor();
    CompletableFuture<Double> futureWithExecutor = app.runAsyncWithExecutor();

    if (futureWithExecutor != null) {
      System.out.println(futureWithExecutor.join());
    }

    if (futureWithoutExecutor != null) {
      System.out.println(futureWithoutExecutor.join());
    }
  }
}