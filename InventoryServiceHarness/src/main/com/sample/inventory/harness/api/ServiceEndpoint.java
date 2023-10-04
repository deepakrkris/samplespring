package com.sample.inventory.harness.api;

import com.sun.jdi.InvocationException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple service interface that combines an {@link #invoke(Map)}
 * method to accept a map of named parameters and return zero or more results with
 * additional policy methods that specify {@link #getSupportedParameters() which
 * parameters are accepted} and {@link #getMaxConcurrentInvocations() the maximum
 * number of concurrent invocations allowed}.
 */
public interface ServiceEndpoint<T> {
    /**
     * @return the maximum number of concurrent invocations allowed, or {@code -1}
     * if unlimited.
     */
    int getMaxConcurrentInvocations();

    /**
     * @return the set of parameter names supported by this service
     */
    Set<String> getSupportedParameters();

    /**
     * Invoke the service with a map of named parameters (any subset of the supported
     * list of parameters) and return the results.
     *
     * @param parameters a map from parameter names to values
     * @return the results of
     */
    List<T> invoke(Map<String, Object> parameters) throws InvocationException, InterruptedException;
}
