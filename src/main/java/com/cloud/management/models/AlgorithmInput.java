package com.cloud.management.models;

import com.cloud.management.logs.CloudLogs;
import lombok.Data;

@Data
public class AlgorithmInput {
    private final DataInput dataInput;
    private final CloudData data;
    private final String[] testedParameterValues;
    private final CloudLogs cloudLogs;
    private final boolean isDynamicAlgorithm;
    private final boolean isLastIteration;
}
