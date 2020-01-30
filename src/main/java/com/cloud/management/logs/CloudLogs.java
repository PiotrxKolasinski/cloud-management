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


    public void printFullSummary() {
        DecimalFormat f = new DecimalFormat("##.00");
        System.out.println("\n" + fullSummary.getType());
        fullSummary.getSummaries().forEach(item -> {
            System.out.println("*********************************");
            System.out.println("Succesfull access - " + f.format(item.getSuccessFullAccess()) + "%");
            System.out.println("Average access time - " + item.getAverageAccessTime() + "");
        });

        double[] values = fullSummary.getSummaries().stream().mapToDouble(item -> item.getSuccessFullAccess()).toArray();
        double minSuccessFullAccess = Arrays.stream(values).min().getAsDouble();
        double maxSuccessFullAccess = Arrays.stream(values).max().getAsDouble();
        System.out.println("*********************************");
        System.out.println("Succesfull access MIN - " + f.format(minSuccessFullAccess) + "%");
        System.out.println("Succesfull access MAX - " + f.format(maxSuccessFullAccess) + "%");
        double res = maxSuccessFullAccess - minSuccessFullAccess;
        System.out.println("Tolerance: " + (res < 1 ? '0' + f.format(res) : f.format(res)) + "%");

        int[] averageValues = fullSummary.getSummaries().stream().mapToInt(item -> item.getAverageAccessTime()).toArray();
        int minAverageValues = Arrays.stream(averageValues).min().getAsInt();
        int maxAverageValues = Arrays.stream(averageValues).max().getAsInt();
        System.out.println("*********************************");
        System.out.println("Average access time MIN- " + minAverageValues + "");
        System.out.println("Average access time MIX- " + maxAverageValues + "");
        System.out.println("Tolerance: " + (maxAverageValues - minAverageValues) + "");
    }

    public FullSummary getFullSummary() {
        return fullSummary;
    }
}
