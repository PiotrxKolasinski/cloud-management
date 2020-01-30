package com.cloud.management.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@EqualsAndHashCode(callSuper = true)
public class RemoteNode extends Node {
    private final List<Shard> shards = new ArrayList<>();

    public RemoteNode(String id) {
        super(id);
    }

    public boolean isAvailable() {
        Random random = new Random();
        return Double.compare(random.nextDouble(), this.getAvailability()) <= 0;
    }
}
