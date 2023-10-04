package com.project.servicemanager.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class MockObject {
    private String field1;
    private String field2;

    public MockObject(String field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public String getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }
}

public class TestServiceClient extends ServiceClient {

    public TestServiceClient(String serviceId, String url, int maxConcurrentInvocations, Set<String> supportedParams) {
        super(url, serviceId, maxConcurrentInvocations, supportedParams);
    }

    public List<Object> invoke(Map<String, Object> queryParams) throws InterruptedException {
        Thread.sleep(1000);

        List<Object> results = new ArrayList<>() {
            {
                add(new MockObject("value1", "value2"));
            }
        };

        return results;
    }
}
