package com.cloud.management.utils;

import com.cloud.management.config.Properties;
import com.cloud.management.logs.TestedParameter;

public interface Constants {
    String DYNAMIC = "DYNAMIC";

    TestedParameter TESTED_PARAMETER = TestedParameter.valueOf(Properties.getInstance().getProperty("parameter.tested").toUpperCase());
    int NUMBER_OF_ALGORITHM_PRECISION = Integer.parseInt(Properties.getInstance().getProperty("number.of.algorithm.precision"));
}
