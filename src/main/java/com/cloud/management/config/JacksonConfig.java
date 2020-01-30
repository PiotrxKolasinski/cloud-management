package com.cloud.management.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class JacksonConfig {

    public static MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(provideJsonObjectMapper());
    }

    private static ObjectMapper provideJsonObjectMapper() {
        return new ObjectMapper().
                configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).
                disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    }
}
