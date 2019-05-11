package org.apache.arrow;

import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.utils.CompareUtils;
import org.apache.arrow.utils.FileUtils;
import org.apache.arrow.vector.*;
import org.apache.arrow.vector.dictionary.DictionaryProvider;
import org.apache.arrow.vector.ipc.ArrowFileWriter;
import org.apache.arrow.vector.types.FloatingPointPrecision;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArrowWriter {

    private final RootAllocator rootAllocator;
    private FileOutputStream fileOutputStream;
    private VectorSchemaRoot root;
    private ArrowFileWriter arrowFileWriter;
    private final int batchSize;
    private final Random random;
    private final int entries;
    private final static int MAX_SIZE_INT = 100;
    private final static long MAX_SIZE_LONG = 100;
    private final static double MAX_SIZE_DOUBLE = 100.0;

    public ArrowWriter() {
        this.rootAllocator = new RootAllocator(Integer.MAX_VALUE);
        this.batchSize = 100;
        this.random = new Random(System.nanoTime());
        this.entries = this.random.nextInt(1024);
    }

    private Schema makeSchema() {
        List<Field> children = new ArrayList<>();
        children.add(new Field("int",
                FieldType.nullable(new ArrowType.Int(32, true)), null));
        children.add(new Field("long",
                FieldType.nullable(new ArrowType.Int(64, true)), null));
        children.add(new Field("binary",
                FieldType.nullable(new ArrowType.Binary()), null));
        children.add(new Field("double",
                FieldType.nullable(new ArrowType.FloatingPoint(FloatingPointPrecision.SINGLE)), null));
        return new Schema(children, null);
    }

    public void writeData() throws Exception {
        arrowFileWriter.start();
        for (int count=0;count < this.entries;) {
            int itemsToProcess = Math.min(this.batchSize, this.entries - count);

            root.setRowCount(itemsToProcess);
            for (Field field: root.getSchema().getFields()) {
                FieldVector vector = root.getVector(field.getName());
                switch (vector.getMinorType()) {
                    case INT:
                        writeIntField(vector, count, itemsToProcess);
                        break;
                    case BIGINT:
                        writeLongField(vector, count, itemsToProcess);
                        break;
                    case VARBINARY:
                        writeFieldVarBinary(vector, count, itemsToProcess);
                        break;
                    case FLOAT4:
                        writeFieldFloat4(vector, count, itemsToProcess);
                        break;
                    default:
                        throw new RuntimeException("Not supported yet");
                }
            }
            arrowFileWriter.writeBatch();
            count += itemsToProcess;
        }

        arrowFileWriter.end();
        arrowFileWriter.close();
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private void writeIntField(FieldVector fieldVector, int from, int items) {
        IntVector intVector = (IntVector) fieldVector;
        intVector.setInitialCapacity(items);
        intVector.allocateNew();

        List<Object> values = new ArrayList<>();
        for (int count=0;count < items;count++) {
            int value = this.random.nextInt(MAX_SIZE_INT);
            values.add(value);
            intVector.setSafe(count, 1, value);
        }
        fieldVector.setValueCount(items);
        CompareUtils.store("writeint", values);
    }

    private void writeLongField(FieldVector fieldVector, int from, int items) {
        BigIntVector bigIntVector = (BigIntVector) fieldVector;
        bigIntVector.setInitialCapacity(items);
        bigIntVector.allocateNew();

        List<Object> values = new ArrayList<>();
        for (int count = 0;count < items;count++) {
            long value = this.random.nextInt((int) MAX_SIZE_LONG);
            values.add(value);
            bigIntVector.setSafe(count, 1, value);
        }
        bigIntVector.setValueCount(items);
        CompareUtils.store("writelong", values);
    }

    private void writeFieldVarBinary(FieldVector fieldVector, int from, int items) {
        VarBinaryVector varBinaryVector = (VarBinaryVector) fieldVector;
        varBinaryVector.setInitialCapacity(items);
        varBinaryVector.allocateNew();
        for (int count=0;count < items; count++) {
            byte[] arr = new byte[5];
            this.random.nextBytes(arr);

            varBinaryVector.setIndexDefined(count);
            varBinaryVector.setValueLengthSafe(count, arr.length);
            varBinaryVector.setSafe(count, arr);
        }
        varBinaryVector.setValueCount(items);
    }

    private void writeFieldFloat4(FieldVector fieldVector, int from, int items) {
        Float4Vector float4Vector = (Float4Vector) fieldVector;
        float4Vector.setInitialCapacity(items);
        float4Vector.allocateNew();

        List<Object> values = new ArrayList<>();
        for (int count=0;count < items;count++) {
            float value = (float) Math.random();
            values.add(value);
            float4Vector.setSafe(count, 1, value);
        }


        float4Vector.setValueCount(items);
        CompareUtils.store("writefloat", values);
    }


    public void setupWriter(String fileName) throws Exception {
        File arrowFile = new FileUtils().validateFile(fileName);
        this.fileOutputStream = new FileOutputStream(arrowFile);

        Schema schema = makeSchema();
        this.root = VectorSchemaRoot.create(schema, this.rootAllocator);
        DictionaryProvider.MapDictionaryProvider provider = new DictionaryProvider.MapDictionaryProvider();

        this.arrowFileWriter = new ArrowFileWriter(this.root, provider, this.fileOutputStream.getChannel());
    }

    public static void main(String[] args) throws Exception {
        ArrowWriter writer = new ArrowWriter();
        writer.setupWriter("sample.arrow");
        writer.writeData();
    }
}