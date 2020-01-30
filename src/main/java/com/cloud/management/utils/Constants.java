package com.cloud.management.utils;

import com.cloud.management.config.Properties;
import com.cloud.management.logs.TestedParameter;

public interface Constants {
    String RAFT = "RAFT";
    String MULTI_MASTER = "MULTI-MASTER";
    String DYNAMIC_RAFT = "DYNAMIC_RAFT";
    String DYNAMIC_MULTI_MASTER = "DYNAMIC_MULTI-MASTER";

    TestedParameter TESTED_PARAMETER = TestedParameter.valueOf(Properties.getInstance().getProperty("parameter.tested").toUpperCase());
    int NUMBER_OF_ALGORITHM_PRECISION = Integer.parseInt(Properties.getInstance().getProperty("number.of.algorithm.precision"));
}
