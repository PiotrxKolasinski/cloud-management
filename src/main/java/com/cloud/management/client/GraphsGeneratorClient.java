package com.cloud.management.client;

import com.cloud.management.client.exception.GraphHttpErrorException;
import com.cloud.management.config.Properties;
import com.cloud.management.config.RestTemplateProvider;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class GraphsGeneratorClient {
    private final String GENERATE_GRAPHS_URL = "/graphs/generate";
    private final String localGraphLUrl = Properties.getInstance().getProperty("custom.graphs.url");
    private final RestTemplate restTemplate = RestTemplateProvider.provideRestTemplate();

    public void generateGraphSummary(FullSummariesRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(localGraphLUrl + GENERATE_GRAPHS_URL);
        try {
            restTemplate.postForEntity(builder.toUriString(), request, Void.class);
        } catch (Exception ex) {
            throw new GraphHttpErrorException("Http exception", ex);
        }
    }
}
