package com.cloud.management.logs;

import lombok.Data;

@Data
public class Summary {
    private final double successFullAccess;
    private final int averageAccessTime;
    private final String[] testedParameterValues;
}
