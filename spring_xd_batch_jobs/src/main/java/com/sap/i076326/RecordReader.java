package com.sap.i076326;

import org.springframework.batch.item.ItemReader;

public class RecordReader implements ItemReader<Record>{
    public Record read() {
        Record record = new Record();
        record.setStr1("Hello");
        record.setStr2("World");

        return record;
    }
}