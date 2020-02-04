package com.cloud.management.logs;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import static com.cloud.management.utils.Constants.NUMBER_OF_ALGORITHM_PRECISION;

public class CloudLogs {
    private final FullSummary fullSummary;
    private double temporarySuccessFullAccess = 0;
    private int temporaryAverageAccessTime = 0;

    public CloudLogs(FullSummary fullSummary) {
        this.fullSummary = fullSummary;
    }

    public void addSummary(List<NodeRequestLog> nodeRequestLogs, String[] testedParameterValues, boolean isLastIteration) {
        int sumSuccess = 0;
        int sumOperationTime = 0;

        for (NodeRequestLog nodeRequestLog : nodeRequestLogs) {
            if (nodeRequestLog.isSuccessRequest()) {
                sumSuccess++;
                sumOperationTime += nodeRequestLog.getOperationTime();
            }
        }
        temporarySuccessFullAccess += ((double) sumSuccess / nodeRequestLogs.size()) * 100;
        temporaryAverageAccessTime += sumOperationTime / sumSuccess;

        createSummaryIfLastIteration(testedParameterValues, isLastIteration);
    }

    private void createSummaryIfLastIteration(String[] testedParameterValues, boolean isLastIteration) {
        if (isLastIteration || NUMBER_OF_ALGORITHM_PRECISION == 1) {
            fullSummary.getSummaries().add(
                    new Summary(
                            temporarySuccessFullAccess / NUMBER_OF_ALGORITHM_PRECISION,
                            temporaryAverageAccessTime / NUMBER_OF_ALGORITHM_PRECISION,
                            testedParameterValues
                    ));
            temporarySuccessFullAccess = 0;
            temporaryAverageAccessTime = 0;
        }
    }

    public FullSummary getFullSummary() {
        return fullSummary;
    }
}
