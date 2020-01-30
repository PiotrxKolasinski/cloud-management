package com.cloud.management.service;

import com.cloud.management.models.CloudData;
import com.cloud.management.models.RemoteNode;
import com.cloud.management.models.Shard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HeartBeatDynamicService {

    public void heartBeat(CloudData data, int numberOfReplicas) {
        for (int i = 1; i <= data.getShardNodeMap().size(); i++) {
            manageShard(data, numberOfReplicas, i);
        }
    }

    private void manageShard(CloudData data, int numberOfReplicas, Integer shardID) {
        List<RemoteNode> unavailableNodesToDelete = new ArrayList<>();
        int numberOfWorkingNodes = verifyAvailableNodes(data, numberOfReplicas, shardID, unavailableNodesToDelete);
        deleteNodes(data, shardID, unavailableNodesToDelete);
        generateNewShards(data, numberOfReplicas, shardID, numberOfWorkingNodes);
    }

    private void generateNewShards(CloudData data, int numberOfReplicas, Integer shardId, int numberOfWorkingNodes) {
        Random random = new Random();
        for (int i = numberOfWorkingNodes; i <= numberOfReplicas; i++) {
            RemoteNode newShard = data.getRemoteNodes().get(random.nextInt(data.getRemoteNodes().size()));
            int errCount = 0;
            while (!newShard.isAvailable() || (newShard.getShards().size() == newShard.getCapacity()) || data.getShardNodeMap().get(shardId).contains(newShard)) {
                newShard = data.getRemoteNodes().get(random.nextInt(data.getRemoteNodes().size()));
                errCount++;
                if (errCount == 100) break;
            }
            if (errCount < 100) {
                newShard.getShards().add(new Shard(shardId));
                data.getShardNodeMap().get(shardId).add(newShard);
            }
        }
    }

    private int verifyAvailableNodes(CloudData data, int numberOfReplicas, Integer shardId, List<RemoteNode> unavailableNodesToDelete) {
        int numberOfWorkingNodes = 0;
        for (int i = 0; i < data.getShardNodeMap().get(shardId).size(); i++) {
            if (data.getShardNodeMap().get(shardId).get(i).isAvailable()) {
                numberOfWorkingNodes++;
                if (numberOfWorkingNodes == numberOfReplicas && numberOfWorkingNodes < data.getShardNodeMap().get(shardId).size() - 1) {
                    unavailableNodesToDelete.addAll(data.getShardNodeMap().get(shardId).subList(i + 1, data.getShardNodeMap().get(shardId).size()));
                    break;
                }
            } else {
                if (i > numberOfReplicas) {
                    unavailableNodesToDelete.add(data.getShardNodeMap().get(shardId).get(i));
                }
            }
        }
        return numberOfWorkingNodes;
    }

    private void deleteNodes(CloudData data, Integer shardID, List<RemoteNode> deleteNodes) {
        if (deleteNodes.size() > 0) {
            for (RemoteNode deleteNode : deleteNodes) deleteNode.getShards().remove(0);
            data.getShardNodeMap().get(shardID).removeAll(deleteNodes);
        }
    }
}
