package com.cloud.management.models;

import lombok.Data;

import java.util.Random;

@Data
public class Node {
    private final String id;
    private double capacity;
    private double computingPower;
    private double availability;

    public void setAvailability(NodeAvailability availability, DataInput dataInput) {
        Random random = new Random();
        switch (availability) {
            case REMOTE_STRONG_NODE:
                this.availability = dataInput.getNodeRemoteStrongAvailability() + random.nextGaussian() / 10;
                break;
            case REMOTE_WEAK_NODE:
                this.availability = dataInput.getNodeRemoteWeakAvailability() + random.nextGaussian() / 10;
                break;
            default:
                this.availability = 1;
        }
    }
}
