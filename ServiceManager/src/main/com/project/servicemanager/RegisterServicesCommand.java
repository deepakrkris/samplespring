package com.project.servicemanager;

import com.project.servicemanager.ratelimit.RateLimitedServiceEndpointProxy;
import com.project.servicemanager.registry.ServiceRegistry;
import com.project.servicemanager.util.TestServiceClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;

class ServiceConfig {
    public String serviceUrl;
    public String serviceConfigUrl;

    public ServiceConfig(String serviceUrl, String serviceConfigUrl) {
        this.serviceUrl = serviceUrl;
        this.serviceConfigUrl = serviceConfigUrl;
    }
}

/**
 * @class RegisterServicesCommand
 * <p>
 * This is a startup command implementation that registers all the given end services
 * @Note :
 * This command expects end services to be available during startup, so it call and fetch the
 * configuration details like supported params for each service
 */
@Component
public class RegisterServicesCommand implements CommandLineRunner {
    private Logger log = Logger.getLogger(String.valueOf(getClass()));

    private Map<String, ServiceConfig> serviceConfigs = new HashMap<>() {
        {
            put("stocks", new ServiceConfig("http://localhost:8080/invokeStock", "http://localhost:8080/stock/config"));
            put("sales", new ServiceConfig("http://localhost:8080/invokeSales", "http://localhost:8080/sales/config"));
            put("reviews", new ServiceConfig("http://localhost:8080/invokeReviews", "http://localhost:8080/reviews/config"));
        }
    };

    @Override
    public void run(String... args) throws Exception {
        for (Map.Entry<String, ServiceConfig> keyVal : serviceConfigs.entrySet()) {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<String>> response = restTemplate.exchange(keyVal.getValue().serviceConfigUrl + "/supported-params", HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
            });
            List<String> supportedParamsList = response.getBody();
            Set<String> supportedParams = new HashSet<String>(supportedParamsList);
            RateLimitedServiceEndpointProxy service = new RateLimitedServiceEndpointProxy(keyVal.getKey(), keyVal.getValue().serviceUrl, 5, supportedParams);
            ServiceRegistry.getInstance().register_service(service);
        }

        /**
         * We also have a Test Client that doesnt call any end service
         * but registers itself with a huge number of test params to help in load testing this implementation
         */
        Set<String> supportedParamsTestClient = new HashSet<>();
        for (int j = 1; j <= 100000; j++) {
            supportedParamsTestClient.add("mockParam" + j);
        }

        TestServiceClient service4 = new TestServiceClient("test_client", "http://localhost/test", 5, supportedParamsTestClient);
        ServiceRegistry.getInstance().register_service(service4);
    }
}
