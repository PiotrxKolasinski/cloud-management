package com.cloud.management.management;

import com.cloud.management.client.FullSummariesRequest;
import com.cloud.management.client.GraphsGeneratorClient;
import com.cloud.management.config.Properties;
import com.cloud.management.logs.CloudLogs;
import com.cloud.management.logs.FullSummary;
import com.cloud.management.models.AlgorithmInput;
import com.cloud.management.models.CloudData;
import com.cloud.management.models.DataInput;
import com.cloud.management.service.CloudManagementService;
import com.cloud.management.service.MasterCloudManagementService;
import com.cloud.management.service.NodeGeneratorService;
import com.cloud.management.service.RaftCloudManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.cloud.management.utils.Constants.*;

public class CloudManagement {
    private static Logger logger = LoggerFactory.getLogger(CloudManagement.class);

    private final CloudLogs staticRaftCloudLogs = new CloudLogs(new FullSummary(RAFT));
    private final CloudLogs dynamicRaftCloudLogs = new CloudLogs(new FullSummary(DYNAMIC_RAFT));
    private final CloudLogs staticMultiMasterCloudLogs = new CloudLogs(new FullSummary(MULTI_MASTER));
    private final CloudLogs dynamicMultiMasterCloudLogs = new CloudLogs(new FullSummary(DYNAMIC_MULTI_MASTER));

    public void run() {
        List<DataInput> dataInputs = Properties.getInstance().getDataInputs();
        runAlgorithms(dataInputs);
        printFullSummaries();
        generateGraphs();
    }

    private void runAlgorithms(List<DataInput> dataInputs) {
        for (DataInput dataInput : dataInputs) {
            logger.info((dataInputs.indexOf(dataInput) + 1) + ". START ITERATION FOR DATA INPUT: " + dataInput);
            String[] testedParameterValues = getTestedParameterValues(dataInput);

            boolean isLastIteration = false;
            for (int i = 0; i < NUMBER_OF_ALGORITHM_PRECISION; i++) {
                CloudData data = generateNodes(dataInput);
                logger.info("TESTED DATA: " + data);
                runRaftAlgorithm(dataInput, data, testedParameterValues, staticRaftCloudLogs, false, isLastIteration);
                runRaftAlgorithm(dataInput, data, testedParameterValues, dynamicRaftCloudLogs, true, isLastIteration);

                runMultiMasterAlgorithm(dataInput, data, testedParameterValues, staticMultiMasterCloudLogs, false, isLastIteration);
                runMultiMasterAlgorithm(dataInput, data, testedParameterValues, dynamicMultiMasterCloudLogs, true, isLastIteration);

                if (i == NUMBER_OF_ALGORITHM_PRECISION - 2) isLastIteration = true;
            }
        }
    }

    private void runRaftAlgorithm(DataInput dataInput, CloudData data, String[] testedParameterValues, CloudLogs cloudLogs, boolean isDynamicAlgorithm, boolean isLastIteration) {
        if (Boolean.parseBoolean(Properties.getInstance().getProperty("algorithm.raft.run"))) {
            AlgorithmInput input = new AlgorithmInput(dataInput, data, testedParameterValues, cloudLogs, isDynamicAlgorithm, isLastIteration);
            CloudManagementService service = new RaftCloudManagementService();
            service.manage(input);
        }
    }

    private void runMultiMasterAlgorithm(DataInput dataInput, CloudData data, String[] testedParameterValues, CloudLogs cloudLogs, boolean isDynamicAlgorithm, boolean isLastIteration) {
        if (Boolean.parseBoolean(Properties.getInstance().getProperty("algorithm.master.run"))) {
            AlgorithmInput input = new AlgorithmInput(dataInput, data, testedParameterValues, cloudLogs, isDynamicAlgorithm, isLastIteration);
            CloudManagementService service = new MasterCloudManagementService();
            service.manage(input);
        }
    }

    private CloudData generateNodes(DataInput dataInput) {
        CloudData data = new CloudData();
        NodeGeneratorService service = new NodeGeneratorService(dataInput);
        data.getRemoteNodes().addAll(service.generateRemoteStrongNode());
        data.getRemoteNodes().addAll(service.generateRemoteWeakNode());
        data.getShardNodeMap().putAll(service.generateShards(data.getRemoteNodes()));
        return data;
    }

    private void printFullSummaries() {
        staticRaftCloudLogs.printFullSummary();
        dynamicRaftCloudLogs.printFullSummary();
        staticMultiMasterCloudLogs.printFullSummary();
        dynamicMultiMasterCloudLogs.printFullSummary();
    }

    private void generateGraphs() {
        GraphsGeneratorClient graphsGeneratorClient = new GraphsGeneratorClient();
        FullSummariesRequest raftRequest = new FullSummariesRequest(staticRaftCloudLogs.getFullSummary(), dynamicRaftCloudLogs.getFullSummary());
        FullSummariesRequest multiMasterRequest = new FullSummariesRequest(staticMultiMasterCloudLogs.getFullSummary(), dynamicMultiMasterCloudLogs.getFullSummary());

        graphsGeneratorClient.generateGraphSummary(raftRequest);
        graphsGeneratorClient.generateGraphSummary(multiMasterRequest);
    }

    private String[] getTestedParameterValues(DataInput dataInput) {
        switch (TESTED_PARAMETER) {
            case NONE:
                return new String[]{""};
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
