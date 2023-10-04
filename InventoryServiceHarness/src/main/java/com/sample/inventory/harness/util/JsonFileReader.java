package com.sample.inventory.harness.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
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
            System.out.println("loading file ... " + fileName + " : ");

            if (classLoader.getResource(fileName) == null) {
                ApplicationContext appContext = new ClassPathXmlApplicationContext();
                System.out.println("using app context : " + appContext.getResource(fileName).getURI());
                InputStream input = appContext.getClassLoader().getResourceAsStream(fileName);
                return objectMapper.readValue(input, typeFactory.constructCollectionType(List.class, clazz));
            } else {
                System.out.println("using class loader : " + classLoader.getResource(fileName).toURI());
                InputStream input = classLoader.getResourceAsStream(fileName);
                return objectMapper.readValue(input, typeFactory.constructCollectionType(List.class, clazz));
            }
        } catch (Exception e) {
            log.info("error occurred " + e.getMessage());
            throw e;
        }
    }
}
