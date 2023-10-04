package com.project.servicemanager.api;

import com.project.servicemanager.registry.ServiceRegistry;
import com.project.servicemanager.util.AggregateByModel;
import com.project.servicemanager.util.InvocationException;
import com.project.servicemanager.util.ServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service Manager class
 * <p>
 * invokeAggregateByModel - invokes end services and does an aggregation
 * invokeSequential - invokes the services one by one
 * invoke - invokes the services concurrently
 * </p>
 */
@RestController
public class ServiceManager {
    private Integer ThreadPoolSize = 10;

    private Logger log = Logger.getLogger(String.valueOf(getClass()));
    private ThreadPoolExecutor executor;

    public ServiceManager(@Value("${threadPoolSize}") Integer threadPoolSize) {
        this.ThreadPoolSize = threadPoolSize;
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(ThreadPoolSize);
    }

    /**
     * Service Manager invoke method :
     * <p>
     * 1. Identifies the end services to invoke using the service registry
     * 2. invokes the end services concurrently
     * </p>
     *
     * @param queryParams a map from parameter names to values
     * @return the results from database query
     */
    @RequestMapping(value = "/invoke", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> invoke(@RequestParam Map<String, Object> queryParams) throws InvocationException {
        Map<String, Object> response = new HashMap<>();

        List<ServiceClient> services = ServiceRegistry.getInstance().getServicesForParams(queryParams.keySet());

        Map<String, Future<List<Object>>> invokeFutures = new HashMap<>();

        try {
            log.log(Level.FINE, "Service Manager : called services , waiting for response");
            for (ServiceClient svc : services) {
                invokeFutures.put(svc.getServiceId(), executor.submit(() -> svc.invoke(queryParams)));
            }

            for (Map.Entry<String, Future<List<Object>>> result : invokeFutures.entrySet()) {
                response.put(result.getKey(), result.getValue().get());
            }
        } catch (InterruptedException e1) {
            throw new InvocationException(e1.getLocalizedMessage(), "ERR_SVC_INT");
        } catch (ExecutionException e2) {
            throw new InvocationException(e2.getLocalizedMessage(), "ERR_SVC_EXEC");
        }
        log.log(Level.FINE, "Service Manager : received response from all service endpoints " + System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * TODO :
     *    This method implements a non-standard aggregation implementation for demo purposes, it works only for the given harness
     *    an ideal aggregation would be usecase independent and have multiple query configurations
     *
     * @param queryParams a map from parameter names to values
     * @return the results from database query
     */
    @RequestMapping(value = "/invoke/aggregateByModel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, AggregateByModel>> invokeAggregateByModel(@RequestParam Map<String, Object> queryParams) throws InvocationException {
        Map<String, AggregateByModel> response = new HashMap<>();

        List<ServiceClient> services = ServiceRegistry.getInstance().getServicesForParams(queryParams.keySet());

        try {
            for (ServiceClient svc : services) {
                List<Object> data = svc.invoke(queryParams);

                for (Object obj : data) {
                    if (obj instanceof LinkedHashMap || obj instanceof HashMap) {
                        Map<String, Object> objHash = (Map<String, Object>) obj;
                        if (objHash.get("model") != null) {
                            String model = (String) objHash.get("model");
                            if (response.get(model) == null) {
                                response.put(model, new AggregateByModel());
                            }
                            if (objHash.get("availableStock") != null) {
                                response.get(model).setAvailableStock(response.get(model).getAvailableStock() + (Integer) objHash.get("availableStock"));
                            }

                            if (objHash.get("sales") != null) {
                                response.get(model).setSales(response.get(model).getSales() + (Integer) objHash.get("sales"));
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException e1) {
            throw new InvocationException(e1.getLocalizedMessage(), "ERR_SVC_INT");
        } catch (Exception e) {
            throw new InvocationException(e.getLocalizedMessage(), "ERR_SVC_UNKNOWN");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * This method is only for load testing purposes to test effectiveness of the thread pool configuration
     *
     * @param queryParams a map from parameter names to values
     * @return the results from database query
     */
    @RequestMapping(value = "/invokeSequential", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> invokeSequential(@RequestParam Map<String, Object> queryParams) throws InterruptedException, InvocationException {
        Map<String, Object> response = new HashMap<>();

        List<ServiceClient> services = ServiceRegistry.getInstance().getServicesForParams(queryParams.keySet());

        for (ServiceClient svc : services) {
            List<Object> data = svc.invoke(queryParams);
            response.put(svc.getServiceId(), data);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
