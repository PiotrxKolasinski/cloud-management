package com.cloud.management.service;

import com.cloud.management.logs.NodeRequestLog;
import com.cloud.management.models.AlgorithmInput;
import com.cloud.management.models.CloudData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class CloudManagementService {
    private static Logger logger = LoggerFactory.getLogger(CloudManagementService.class);
    protected final List<NodeRequestLog> nodeRequestLogs = new ArrayList<>();

    public void manage(AlgorithmInput input) {
        Random random = new Random();
        HeartBeatDynamicService heartBeatService = new HeartBeatDynamicService();
        manageForEachRequestStreamSize(input, random, heartBeatService);
        input.getCloudLogs().addSummary(nodeRequestLogs, input.getTestedParameterValues(), input.isLastIteration());
        logger.info(" Algorithm has been completed. TYPE: " + input.getCloudLogs().getFullSummary().getType());
    }

    private void manageForEachRequestStreamSize(AlgorithmInput input, Random random, HeartBeatDynamicService heartBeatService) {
        for (int i = 0; i < input.getDataInput().getRequestStreamSize(); i++) {
            runHeartBeatServiceIfDynamicActive(input, heartBeatService);
            Integer requestDataID = random.nextInt(input.getData().getShardNodeMap().size()) + 1;
            manageRequest(input, requestDataID);
        }
    }

    private void runHeartBeatServiceIfDynamicActive(AlgorithmInput input, HeartBeatDynamicService heartBeatService) {
        if (input.isDynamicAlgorithm()) {
            heartBeatService.heartBeat(input.getData(), input.getDataInput().getNumberOfReplicas());
        }
    }

    protected final void manageRequest(AlgorithmInput input, Integer requestDataID) {
        CloudData data = input.getData();
        int nodeId = -1;
        double nodeProcessingPower = 1000;
        for (int i = 0; i < data.getShardNodeMap().get(requestDataID).size(); i++) {
            if (data.getShardNodeMap().get(requestDataID).get(i).isAvailable() && data.getShardNodeMap().get(requestDataID).get(i).getComputingPower() < nodeProcessingPower) {
                nodeId = i;
                nodeProcessingPower = data.getShardNodeMap().get(requestDataID).get(i).getComputingPower();
            }
        }
        manageSpecificRequest(input, requestDataID, nodeId);
    }

    protected abstract void manageSpecificRequest(AlgorithmInput input, Integer requestDataID, int nodeId);
}
