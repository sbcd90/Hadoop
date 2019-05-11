package org.apache.arrow;

import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.utils.CompareUtils;
import org.apache.arrow.utils.FileUtils;
import org.apache.arrow.vector.*;
import org.apache.arrow.vector.dictionary.DictionaryProvider;
import org.apache.arrow.vector.ipc.ArrowFileReader;
import org.apache.arrow.vector.ipc.SeekableReadChannel;
import org.apache.arrow.vector.ipc.message.ArrowBlock;
import org.apache.arrow.vector.types.Types;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArrowReader {
    private final RootAllocator rootAllocator;
    private final FileUtils fileUtils;

    public ArrowReader() {
        this.fileUtils = new FileUtils();
        this.rootAllocator = new RootAllocator(Integer.MAX_VALUE);
    }

    public void read(String fileName) throws Exception {
        File arrowFile = this.fileUtils.validateFile(fileName);
        FileInputStream fileInputStream = new FileInputStream(arrowFile);
        DictionaryProvider.MapDictionaryProvider provider = new DictionaryProvider.MapDictionaryProvider();

        ArrowFileReader arrowFileReader = new ArrowFileReader(new SeekableReadChannel(fileInputStream.getChannel()),
                this.rootAllocator);

        VectorSchemaRoot root = arrowFileReader.getVectorSchemaRoot();

        List<ArrowBlock> arrowBlocks = arrowFileReader.getRecordBlocks();
        for (ArrowBlock arrowBlock: arrowBlocks) {
            if (!arrowFileReader.loadRecordBatch(arrowBlock)) {
                throw new IOException("Expected to read record batch");
            }
        }

        List<FieldVector> fieldVectors = root.getFieldVectors();
        for (FieldVector fieldVector: fieldVectors) {
            Types.MinorType type = fieldVector.getMinorType();
            switch (type) {
                case INT:
                    List<Object> valuesInt = showIntAccessor(fieldVector);
                    CompareUtils.store("readint", valuesInt);
                    break;
                case BIGINT:
                    List<Object> valuesLong = showLongAccessor(fieldVector);
                    CompareUtils.store("readlong", valuesLong);
                    break;
                case VARBINARY:
                    List<byte[]> data = showByteArrayAccessor(fieldVector);
                    break;
                case FLOAT4:
                    List<Object> valuesFloat = showFloatAccessor(fieldVector);
                    CompareUtils.store("readfloat", valuesFloat);
                    break;
                default:
                    throw new RuntimeException("Not supported yet");
            }
        }
        arrowFileReader.close();
    }

    private List<byte[]> showByteArrayAccessor(FieldVector fieldVector) {
        List<byte[]> resultByteArrays = new ArrayList<>();
        VarBinaryVector binaryVector = (VarBinaryVector) fieldVector;
        for (int count=0;count < binaryVector.getValueCount();count++) {
            if (!binaryVector.isNull(count)) {
                byte[] data = binaryVector.get(count);
                resultByteArrays.add(data);
            }
        }
        return resultByteArrays;
    }

    private List<Object> showFloatAccessor(FieldVector fieldVector) {
        List<Object> resultFloats = new ArrayList<>();
        Float4Vector float4Vector = (Float4Vector) fieldVector;
        for (int count=0;count < float4Vector.getValueCount();count++) {
            if (!float4Vector.isNull(count)) {
                float value = float4Vector.get(count);
                resultFloats.add(value);
            }
        }
        return resultFloats;
    }

    private List<Object> showLongAccessor(FieldVector fieldVector) {
        List<Object> resultLongs = new ArrayList<>();
        BigIntVector bigIntVector = (BigIntVector) fieldVector;
        for (int count=0;count < bigIntVector.getValueCount();count++) {
            if (!bigIntVector.isNull(count)) {
                long value = bigIntVector.get(count);
                resultLongs.add(value);
            }
        }
        return resultLongs;
    }

    private List<Object> showIntAccessor(FieldVector fieldVector) {
        List<Object> resultInts = new ArrayList<>();
        IntVector intVector = (IntVector) fieldVector;
        for (int count=0;count < intVector.getValueCount();count++) {
            if (!intVector.isNull(count)) {
                int value = intVector.get(count);
                resultInts.add(value);
            }
        }
        return resultInts;
    }

    public static void main(String[] args) throws Exception {
        ArrowReader arrowReader = new ArrowReader();
        arrowReader.read("sample.arrow");
    }
}