package com.cloud.management.client;

import com.cloud.management.logs.FullSummary;
import lombok.Data;

@Data
public class FullSummariesRequest {
    private final FullSummary staticFullSummary;
    private final FullSummary dynamicFullSummary;
}
