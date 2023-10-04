package com.sample.inventory.harness.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;


public class JsonFileReader {
    private Logger log = Logger.getLogger(String.valueOf(getClass()));
    private final ObjectMapper objectMapper;

    public JsonFileReader() {
        this.objectMapper = new ObjectMapper();
    }

    public List<Object> parseJsonFile(String fileName, Class clazz) throws IOException, URISyntaxException {
        try {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            ClassLoader classLoader = getClass().getClassLoader();
            System.out.println(classLoader.getResource(fileName) != null);
            File file = new File(classLoader.getResource(fileName).toURI());
            return objectMapper.readValue(file, typeFactory.constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            log.info("error occurred " + e.getMessage());
            throw e;
        }
    }
}
