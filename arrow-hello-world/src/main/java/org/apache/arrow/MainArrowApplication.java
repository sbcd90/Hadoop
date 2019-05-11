package org.apache.arrow;

import org.apache.arrow.utils.CompareUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainArrowApplication {

    public static void main(String[] args) throws Exception {
        ArrowWriter writer = new ArrowWriter();
        writer.setupWriter("sample.arrow");
        writer.writeData();

        ArrowReader arrowReader = new ArrowReader();
        arrowReader.read("sample.arrow");

        List<Integer> written = CompareUtils.get("writeint")
                .stream()
                .map(new Function<Object, Integer>() {
                    @Override
                    public Integer apply(Object o) {
                        return Integer.valueOf(o.toString());
                    }
                }).collect(Collectors.toList());

        List<Integer> sent = CompareUtils.get("readint")
                .stream()
                .map(new Function<Object, Integer>() {
                    @Override
                    public Integer apply(Object o) {
                        return Integer.valueOf(o.toString());
                    }
                }).collect(Collectors.toList());
        System.out.println(CompareUtils.compareInts(written, sent));
    }
}