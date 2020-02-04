package com.cloud.management.management;

import com.cloud.management.config.Properties;
import com.cloud.management.models.AlgorithmType;
import com.cloud.management.models.CloudData;
import com.cloud.management.models.DataInput;
import com.cloud.management.service.NodeGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.cloud.management.utils.Constants.NUMBER_OF_ALGORITHM_PRECISION;

public class CloudManagement {
    private static Logger logger = LoggerFactory.getLogger(CloudManagement.class);

    private final AlgorithmManagement raft = new AlgorithmManagement(AlgorithmType.RAFT, Boolean.parseBoolean(Properties.getInstance().getProperty("algorithm.raft.run")));
    private final AlgorithmManagement multiMaster = new AlgorithmManagement(AlgorithmType.MULTI_MASTER, Boolean.parseBoolean(Properties.getInstance().getProperty("algorithm.master.run")));

    public void run() {
        List<DataInput> dataInputs = Properties.getInstance().getDataInputs();
        runAlgorithms(dataInputs);
        generateGraphs();
    }

    private void runAlgorithms(List<DataInput> dataInputs) {
        for (DataInput dataInput : dataInputs) {
            logger.info((dataInputs.indexOf(dataInput) + 1) + ". START ITERATION FOR DATA INPUT: " + dataInput);

            boolean isLastIteration = false;
            for (int i = 0; i < NUMBER_OF_ALGORITHM_PRECISION; i++) {
                CloudData data = generateNodes(dataInput);
                logger.info("TESTED DATA: " + data);

                raft.run(dataInput, data, isLastIteration);
                multiMaster.run(dataInput, data, isLastIteration);

                if (i == NUMBER_OF_ALGORITHM_PRECISION - 2) isLastIteration = true;
            }
        }
    }

    private CloudData generateNodes(DataInput dataInput) {
        CloudData data = new CloudData();
        NodeGeneratorService service = new NodeGeneratorService(dataInput);
        data.getRemoteNodes().addAll(service.generateRemoteStrongNode());
        data.getRemoteNodes().addAll(service.generateRemoteWeakNode());
        data.getShardNodeMap().putAll(service.generateShards(data.getRemoteNodes()));
        return data;
    }



    private void generateGraphs() {
        raft.generateGraphs();
        multiMaster.generateGraphs();
    }
}
