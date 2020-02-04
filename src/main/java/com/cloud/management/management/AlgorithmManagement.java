package com.cloud.management.management;

import com.cloud.management.client.FullSummariesRequest;
import com.cloud.management.client.GraphsGeneratorClient;
import com.cloud.management.logs.CloudLogs;
import com.cloud.management.logs.FullSummary;
import com.cloud.management.models.AlgorithmInput;
import com.cloud.management.models.AlgorithmType;
import com.cloud.management.models.CloudData;
import com.cloud.management.models.DataInput;
import com.cloud.management.service.AlgorithmService;
import com.cloud.management.service.MasterAlgorithmService;
import com.cloud.management.service.RaftAlgorithmService;
import com.cloud.management.utils.Constants;

import static com.cloud.management.utils.Constants.TESTED_PARAMETER;

public class AlgorithmManagement {
    private final AlgorithmService service;
    private final CloudLogs staticCloudLogs;
    private final CloudLogs dynamicCloudLogs;
    private final boolean isRunningAlgorithm;

    public AlgorithmManagement(AlgorithmType type, boolean isRunningAlgorithm) {
        this.staticCloudLogs = new CloudLogs(new FullSummary(type.toString()));
        this.dynamicCloudLogs = new CloudLogs(new FullSummary(Constants.DYNAMIC + "_" + type.toString()));
        this.isRunningAlgorithm = isRunningAlgorithm;
        this.service = getAlgorithmService(type);
    }

    private AlgorithmService getAlgorithmService(AlgorithmType type) {
        switch (type) {
            case RAFT:
                return new RaftAlgorithmService();
            case MULTI_MASTER:
                return new MasterAlgorithmService();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public void run(DataInput dataInput, CloudData cloudData, boolean isLastIteration) {
        if (isRunningAlgorithm) {
            runAlgorithm(dataInput, cloudData, staticCloudLogs, false, isLastIteration);
            runAlgorithm(dataInput, cloudData, dynamicCloudLogs, true, isLastIteration);
        }
    }

    private void runAlgorithm(DataInput dataInput, CloudData data, CloudLogs cloudLogs, boolean isDynamicAlgorithm, boolean isLastIteration) {
        AlgorithmInput input = new AlgorithmInput(dataInput, data, getTestedParameterValues(dataInput), cloudLogs, isDynamicAlgorithm, isLastIteration);
        service.manage(input);
    }

    public void generateGraphs() {
        if (isRunningAlgorithm) {
            GraphsGeneratorClient graphsGeneratorClient = new GraphsGeneratorClient();
            FullSummariesRequest request = new FullSummariesRequest(staticCloudLogs.getFullSummary(), dynamicCloudLogs.getFullSummary());
            graphsGeneratorClient.generateGraphSummary(request);
        }
    }

    private String[] getTestedParameterValues(DataInput dataInput) {
        switch (TESTED_PARAMETER) {
            case REPLICA:
                return new String[]{String.valueOf(dataInput.getNumberOfReplicas())};
            case NODE:
                return new String[]{String.valueOf(dataInput.getNodeRemoteStrongQuantity())};
            case AVAILABILITY_DECREASING:
                return new String[]{String.valueOf(dataInput.getNodeRemoteWeakAvailability()), String.valueOf(dataInput.getNodeRemoteStrongAvailability())};
            case AVAILABILITY_INCREASING:
                return new String[]{String.valueOf(dataInput.getNodeRemoteStrongAvailability()), String.valueOf(dataInput.getNodeRemoteWeakAvailability())};
            default:
                return new String[]{""};
        }
    }
}
