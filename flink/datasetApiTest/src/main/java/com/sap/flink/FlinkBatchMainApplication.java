package com.sap.flink;

import com.sap.flink.transformations.TransformationTest;
import org.apache.flink.api.java.ExecutionEnvironment;

public class FlinkBatchMainApplication {
    private static TransformationTest transformationTest;

    public FlinkBatchMainApplication() {
        transformationTest = new TransformationTest(ExecutionEnvironment.getExecutionEnvironment());
    }

    public static void main(String[] args) throws Exception {
        FlinkBatchMainApplication application = new FlinkBatchMainApplication();

        /**
         * map transformation test
         */
        transformationTest.testMap();

        /**
         * flatMap transformation test
         */
        transformationTest.testFlatMap();

        /**
         * mapPartition transformation test
         */
        transformationTest.testMapPartition();
    }
}