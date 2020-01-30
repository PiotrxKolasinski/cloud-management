package com.cloud.management.config;

import com.cloud.management.config.exception.DataInputsWrongFormatException;
import com.cloud.management.models.DataInput;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Properties {
    private static final Properties instance = new Properties();
    private java.util.Properties properties;
    private List<DataInput> dataInputs;

    private Properties() {
        preparePropertiesFile();
        prepareDataInputs();
    }

    private void preparePropertiesFile() {
        try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {
            properties = new java.util.Properties();
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void prepareDataInputs() {
        try (InputStream input = new FileInputStream("src/main/resources/dataInputs.json")) {
            dataInputs = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            dataInputs = Arrays.asList(objectMapper.readValue(input, DataInput[].class));
        } catch (IOException ex) {
            throw new DataInputsWrongFormatException(ex.getMessage(), ex);
        }
    }

    public static Properties getInstance() {
        return instance;
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public List<DataInput> getDataInputs() {
        return dataInputs;
    }
}
