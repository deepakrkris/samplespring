package com.project.servicemanager.registry;

import com.project.servicemanager.util.InvocationException;
import com.project.servicemanager.util.ServiceClient;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Logger;

/**
 * @class : ServiceRegistry
 * <p>
 * This class uses the Singleton pattern to provide a single object per jvm
 * <p>
 * This class is used to register all the end services and helps to find the relevant service for a given query
 */
public class ServiceRegistry {
    static ServiceRegistry instance = null;
    private Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    @Value("${IntersectionStrategy}")
    private String intersection_strategy = "Bitmap";

    Set<String> registered_services = null;
    HashMap<String, List<String>> params_to_services_map = null;
    private Map<Integer, String> service_bitmap;
    private Map<String, ServiceBitMap> params_to_service_bitmap;

    /**
     * Bit map pointer to add every new service
     * <p>
     * Used by BitMap set intersection algorithm
     * </p>
     */
    int currentServiceBitmapCount = 0;

    private Map<String, ServiceClient> service_url_registry = null;

    private ServiceRegistry() {
        params_to_services_map = new HashMap<>();
        registered_services = new HashSet<>();
        service_bitmap = new HashMap<>();
        params_to_service_bitmap = new HashMap<>();
        service_url_registry = new HashMap<>();
    }

    public static ServiceRegistry getInstance() {
        if (instance == null) {
            instance = new ServiceRegistry();
        }
        return instance;
    }

    /**
     * This method is used to register the service endpoints
     *
     * @param service
     * @throws Exception
     */
    public void register_service(ServiceClient service) throws Exception {
        try {
            // serviceId not available, go ahead and register
            if (!registered_services.contains(service.getServiceId())) {
                // register serviceId
                registered_services.add(service.getServiceId());

                for (String p : service.getSupportedParameters()) {
                    // check if param already available in registry
                    // every parameter is ensured to have a set of services it is relevant for
                    if (!params_to_services_map.containsKey(p)) {
                        // create a <param, list<services>> entry for each supported param of the service
                        params_to_services_map.put(p, new ArrayList<>());

                        // every parameter is also initialized with a bitmap which represents a set of services
                        // having 1 in a bit position says that service is available
                        // having 0 says service for that bit position does not support param
                        params_to_service_bitmap.put(p, new ServiceBitMap());
                    }
                    // map each parameter to the service
                    params_to_services_map.get(p).add(service.getServiceId());
                    // add the current service to the parameter set, where the bitmap counter, points currently
                    params_to_service_bitmap.get(p).set(currentServiceBitmapCount);
                }
                // finally also have a map that tells which bit position is reserved for the current service
                service_bitmap.put(currentServiceBitmapCount, service.getServiceId());

                // current service is registered, move bit map pointer by one for next service
                currentServiceBitmapCount++;

                service_url_registry.put(service.getServiceId(), service);
            } else {
                throw new Exception("service already registered");
            }
        } catch (InvocationTargetException ie) {
            throw new Exception("given service factory is unable to create instance");
        }
    }

    /**
     * This method is called to find the services that are relevant for a given query,
     * using one of the available Set intersection algorithms
     *
     * @param parameters
     * @return
     */
    public List<ServiceClient> getServicesForParams(Set<String> parameters) throws InvocationException {
        List<String> intersection = null;

        for (String p : parameters) {
            if (params_to_services_map.get(p) == null) {
                throw new InvocationException("unsupported parameter received : " + p, "ERR_ENDPARAMS");
            }
        }

        /**
         * Todo
         *     Only Bitmap and List intersection strategies are currently implemented
         */
        if (intersection_strategy == "Bitmap") {
            intersection = BitMapIntersection.intersect(parameters,
                    params_to_services_map,
                    params_to_service_bitmap,
                    service_bitmap);
        } else {
            intersection = ListIntersection.intersect(parameters,
                    params_to_services_map);
        }

        List<ServiceClient> serviceEndpoints = new ArrayList<>();

        if (intersection == null) {
            return serviceEndpoints;
        }

        for (String serviceId : intersection) {
            serviceEndpoints.add(service_url_registry.get(serviceId));
        }

        return serviceEndpoints;
    }
}
