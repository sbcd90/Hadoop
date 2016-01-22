package com.sap.flink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FlinkStreamMainApplication {
    private static final ArrayList<String> SYMBOLS = new ArrayList<String>(Arrays.asList("SPX", "FTSE", "DJI", "DJT", "BUX", "DAX", "GOOG"));
    private static Double DEFAULT_PRICE = 0.0;

    public static class StockPrice implements Serializable {
        public String symbol;
        public Double price;

        public StockPrice() {

        }

        public StockPrice(String symbol, Double price) {
            this.symbol = symbol;
            this.price = price;
        }

        @Override
        public String toString() {
            return "StockPrice{" +
                    "symbol='" + symbol + "'" +
                    ", count=" + price + "}";
        }
    }

    public final static class StockSource implements SourceFunction<StockPrice> {
        private Double price;
        private String symbol;
        private Integer sigma;

        public StockSource(String symbol, Integer sigma) {
            this.symbol = symbol;
            this.sigma = sigma;
        }

        public void cancel() {

        }

        public void run(SourceContext<StockPrice> sourceContext) throws Exception {
            price = DEFAULT_PRICE;
            Random random = new Random();

            while (true) {
                price = price + random.nextGaussian() * sigma;
                sourceContext.collect(new StockPrice(symbol, price));
                Thread.sleep(random.nextInt(200));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<StockPrice> socketStockStream = environment.socketTextStream("localhost", 9999)
                .map(new MapFunction<String, StockPrice>() {
                    private String[] tokens;

                    public StockPrice map(String s) throws Exception {
                        tokens = s.split(",");
                        return new StockPrice(tokens[0], Double.parseDouble(tokens[1]));
                    }
                });

        DataStream<StockPrice> SPX_stream = environment.addSource(new StockSource("SPX", 10));
        DataStream<StockPrice> FTSE_stream = environment.addSource(new StockSource("FTSE", 20));
        DataStream<StockPrice> DJI_stream = environment.addSource(new StockSource("DJI", 30));
        DataStream<StockPrice> BUX_stream = environment.addSource(new StockSource("BUX", 40));

        DataStream<StockPrice> stockStream = socketStockStream.union(SPX_stream, FTSE_stream, DJI_stream, BUX_stream);

        stockStream.print();

        environment.execute("Stock Stream");
    }
}