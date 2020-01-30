package com.cloud.management.service;

import com.cloud.management.logs.NodeRequestLog;
import com.cloud.management.models.AlgorithmInput;
import com.cloud.management.models.CloudData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RaftCloudManagementService extends CloudManagementService {

    @Override
    protected void manageSpecificRequest(AlgorithmInput input, Integer requestDataID, int nodeId) {
        if (nodeId != -1) kworum(input.getData(), input.getDataInput().getNumberOfReplicas(), requestDataID, nodeId);
        else nodeRequestLogs.add(new NodeRequestLog(false, 0));
    }

    private void kworum(CloudData data, int numberOfReplicas, Integer requestDataID, int masterNode) {
        int approves = 1;
        List<Double> worstProcessingPower = new ArrayList<>();
        for (int i = 0; i < data.getShardNodeMap().get(requestDataID).size(); i++) {
            if (data.getShardNodeMap().get(requestDataID).get(i).isAvailable() && i != masterNode) {
                approves++;
                worstProcessingPower.add(data.getShardNodeMap().get(requestDataID).get(i).getComputingPower());
            }
        }

        if (approves * 2 > numberOfReplicas) {
            Collections.sort(worstProcessingPower);
            double accessTime = data.getShardNodeMap().get(requestDataID).get(masterNode).getComputingPower()
                    + data.getShardNodeMap().get(requestDataID).get(numberOfReplicas / 2).getComputingPower();
            nodeRequestLogs.add(new NodeRequestLog(true, accessTime));
        } else {
            nodeRequestLogs.add(new NodeRequestLog(false, 0));
        }
    }
}
