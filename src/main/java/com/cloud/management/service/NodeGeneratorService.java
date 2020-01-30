package com.cloud.management.service;

import com.cloud.management.models.DataInput;
import com.cloud.management.models.NodeAvailability;
import com.cloud.management.models.RemoteNode;
import com.cloud.management.models.Shard;

import java.util.*;

public class NodeGeneratorService {
    private final DataInput dataInput;
    private static int remoteStrongNodeCounter = 0;
    private static int remoteWeakNodeCounter = 0;

    public NodeGeneratorService(DataInput dataInput) {
        this.dataInput = dataInput;
    }

    public List<RemoteNode> generateRemoteStrongNode() {
        List<RemoteNode> nodes = new ArrayList<>();
        for (int i = 0; i < dataInput.getNodeRemoteStrongQuantity(); i++) {
            RemoteNode node = new RemoteNode("REMOTE_STRONG_NODE_" + remoteStrongNodeCounter++);
            node.setAvailability(NodeAvailability.REMOTE_STRONG_NODE, dataInput);
            node.setCapacity(dataInput.getNodeRemoteStrongCapacity());
            node.setComputingPower(dataInput.getNodeRemoteStrongComputingPower());
            nodes.add(node);
        }
        return nodes;
    }

    public List<RemoteNode> generateRemoteWeakNode() {
        List<RemoteNode> nodes = new ArrayList<>();
        for (int i = 0; i < dataInput.getNodeRemoteWeakQuantity(); i++) {
            RemoteNode node = new RemoteNode("REMOTE_WEAK_NODE_" + remoteWeakNodeCounter++);
            node.setAvailability(NodeAvailability.REMOTE_WEAK_NODE, dataInput);
            node.setCapacity(dataInput.getNodeRemoteWeakCapacity());
            node.setComputingPower(dataInput.getNodeRemoteWeakComputingPower());
            nodes.add(node);
        }
        return nodes;
    }

    public Map<Integer, List<RemoteNode>> generateShards(List<RemoteNode> remoteNodes) {
        List<RemoteNode> notFilledNodes = new ArrayList<>(remoteNodes);
        Map<Integer, List<RemoteNode>> shardNodeMap = new HashMap<>();

        int dataId = 1;
        while (notFilledNodes.size() > dataInput.getNumberOfReplicas()) {
            List<RemoteNode> tempList = new ArrayList<>();
            while (tempList.size() < dataInput.getNumberOfReplicas()) {
                Random rand = new Random();
                RemoteNode nodeForReplic = notFilledNodes.get(rand.nextInt(notFilledNodes.size()));
                while (tempList.contains(nodeForReplic)) {
                    nodeForReplic = notFilledNodes.get(rand.nextInt(notFilledNodes.size()));
                }
                tempList.add(nodeForReplic);
                nodeForReplic.getShards().add(new Shard(dataId));

                if (nodeForReplic.getShards().size() >= nodeForReplic.getCapacity() / 2) {
                    notFilledNodes.remove(nodeForReplic);
                }
            }
            shardNodeMap.put(dataId, tempList);
            dataId++;
        }
        return shardNodeMap;
    }
}
