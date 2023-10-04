package com.project.servicemanager.ratelimit;

import com.project.servicemanager.util.ServiceClient;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @class RateLimitedServiceEndpointProxy
 * This class extends the ServiceClient class and provides a rate limiting wrapper function
 *
 * <p>
 * This class provides a rate limiting implementation using semaphores
 * When the number of permits are achieved,
 * calls coming in are waited for a max of 10 seconds and then timed out
 * Invoke method :
 * wrapper method that rate limits and then calls the ServiceClient's invoke method
 * </p>
 */
public class RateLimitedServiceEndpointProxy extends ServiceClient {
    private final Semaphore semaphore;

    public RateLimitedServiceEndpointProxy(String serviceId,
                                           String url,
                                           int maxConcurrentInvocations,
                                           Set<String> supportedParams) {
        super(url, serviceId, maxConcurrentInvocations, supportedParams);
        this.semaphore = new Semaphore(maxConcurrentInvocations);
    }

    /***
     * Invoke method :
     *  wrapper method that rate limits and then calls the ServiceClient's invoke method
     * @param parameters a map from parameter names to values
     * @return
     * @throws InterruptedException
     */
    public List<Object> invoke(Map<String, Object> parameters) throws InterruptedException {
        if (!semaphore.tryAcquire(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Too many concurrent invocations for service: " + serviceId);
        }
        try {
            return super.invoke(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }
}
