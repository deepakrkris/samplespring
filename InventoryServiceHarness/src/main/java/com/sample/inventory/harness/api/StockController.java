package com.sample.inventory.harness.api;

import com.sample.inventory.harness.entity.Stock;
import com.sample.inventory.harness.entity.StockRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class StockController implements ServiceEndpoint<Stock> {
    /**
     * Stock Repository
     */
    private final StockRepository stockRepository;

    public StockController(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * @return the maximum number of concurrent invocations allowed, or {@code -1}
     * if unlimited.
     */
    @GetMapping("/stock/config/max-concurrent")
    public int getMaxConcurrentInvocations() {
        return 10;
    }

    /**
     * @return the set of parameter names supported by this service
     */
    @GetMapping("/stock/config/supported-params")
    public Set<String> getSupportedParameters() {
        return new HashSet<>() {
            {
                add("category");
                add("brand");
                add("model");
                add("zip");
                add("company");
            }
        };
    }

    /**
     * Invoke the service with a map of named parameters (any subset of the supported
     * list of parameters) and return the results.
     *
     * @param params a map from parameter names to values
     * @return list of stock
     */
    @GetMapping("/invokeStock")
    public List<Stock> invoke(@RequestParam Map<String, Object> params) throws InterruptedException {
        SearchSpecification<Stock> query = new SearchSpecification<>(params);
        List<Stock> results = stockRepository.findAll(query);
        return results;
    }
}
