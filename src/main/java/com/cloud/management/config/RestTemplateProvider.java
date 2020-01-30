package com.cloud.management.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestTemplateProvider {
    private final static RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
    private final static MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = JacksonConfig.mappingJackson2HttpMessageConverter();

    public static RestTemplate provideRestTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setOutputStreaming(false);
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);
        RestTemplate restTemplateProvider = restTemplateBuilder
                .additionalMessageConverters(mappingJackson2HttpMessageConverter)
                .build();
        restTemplateProvider.setRequestFactory(bufferingClientHttpRequestFactory);
        return restTemplateProvider;
    }
}
