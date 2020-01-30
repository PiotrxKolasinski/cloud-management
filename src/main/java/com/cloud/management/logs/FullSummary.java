package com.cloud.management.logs;

import com.cloud.management.utils.Constants;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FullSummary {
    private final TestedParameter testedParameter = Constants.TESTED_PARAMETER;
    private final String type;
    private final List<Summary> summaries;

    public FullSummary(String type) {
        this.type = type;
        summaries = new ArrayList<>();
    }
}
