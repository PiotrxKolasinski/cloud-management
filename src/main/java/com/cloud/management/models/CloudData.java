package com.cloud.management.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CloudData {
    private final List<RemoteNode> remoteNodes = new ArrayList<>();
    private final Map<Integer, List<RemoteNode>> shardNodeMap = new HashMap<>();
}
