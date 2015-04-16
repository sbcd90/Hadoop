package com.sap.i076326;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class RecordWriter implements ItemWriter<Record>{
    public void write(List<? extends Record> records) {
        System.out.println(records.get(0).getConcat());
    }
}