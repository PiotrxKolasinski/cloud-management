package com.cloud.management.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataInput {
    private int nodeRemoteStrongQuantity;
    private double nodeRemoteStrongAvailability;
    private int nodeRemoteStrongCapacity;
    private int nodeRemoteStrongComputingPower;

    private int nodeRemoteWeakQuantity;
    private double nodeRemoteWeakAvailability;
    private int nodeRemoteWeakCapacity;
    private int nodeRemoteWeakComputingPower;

    private int numberOfReplicas;
    private int requestStreamSize;
}
