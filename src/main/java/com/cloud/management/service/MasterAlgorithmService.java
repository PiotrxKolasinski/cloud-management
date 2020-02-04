package com.cloud.management.service;

import com.cloud.management.logs.NodeRequestLog;
import com.cloud.management.models.AlgorithmInput;
import com.cloud.management.models.CloudData;

public class MasterAlgorithmService extends AlgorithmService {

    @Override
    protected void manageSpecificRequest(AlgorithmInput input, Integer requestDataID, int nodeId) {
        if (nodeId != -1) multiMasterProcessing(input.getData(), requestDataID, nodeId);
        else nodeRequestLogs.add(new NodeRequestLog(false, 0));
    }


    private void multiMasterProcessing(CloudData data, Integer requestDataID, int masterNode) {
        double accessTime = data.getShardNodeMap().get(requestDataID).get(masterNode).getComputingPower();

//        // average shard processing
//        double sum = 0;
//        double max = 0;
//        for (int i = 0; i < data.getShardNodeMap().get(requestDataID).size(); i++) {
//            sum += data.getShardNodeMap().get(requestDataID).get(i).getComputingPower();
//            if (data.getShardNodeMap().get(requestDataID).get(i).getComputingPower() > max)
//                max = data.getShardNodeMap().get(requestDataID).get(i).getComputingPower();
//        }
//        sum /= numberOfReplicas;
//
//        // ver ! - average synchronization
//        accessTime += sum;
//
//        //ver II - longest synchronization
//        //accessTime += max;

        // report success + access time
        nodeRequestLogs.add(new NodeRequestLog(true, accessTime));
    }
}
