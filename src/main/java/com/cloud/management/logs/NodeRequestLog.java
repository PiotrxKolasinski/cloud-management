package com.cloud.management.logs;

import lombok.Data;

import java.time.Instant;

@Data
public class NodeRequestLog {
    private long remoteNodeId;
    private long shardId;
    private double unsuccessfulRequestsTime;
    private double operationTime;
    private boolean isSuccessRequest;
    private Instant createDate;

    public NodeRequestLog(boolean isSuccessRequest, double operationTime) {
        this.isSuccessRequest = isSuccessRequest;
        this.operationTime = operationTime;
        this.createDate = Instant.now();
    }
}
