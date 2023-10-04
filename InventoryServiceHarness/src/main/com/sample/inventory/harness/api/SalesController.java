package com.sample.inventory.harness.api;

import com.sample.inventory.harness.entity.Sales;
import com.sample.inventory.harness.entity.SalesRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class SalesController implements ServiceEndpoint<Sales> {
    /**
     * Sales repository
     */
    private final SalesRepository salesRepository;

    public SalesController(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    /**
     * @return the maximum number of concurrent invocations allowed, or {@code -1}
     * if unlimited.
     */
    @GetMapping("/sales/config/max-concurrent")
    public int getMaxConcurrentInvocations() {
        return 10;
    }

    /**
     * @return the set of parameter names supported by this service
     */
    @GetMapping("/sales/config/supported-params")
    public Set<String> getSupportedParameters() {
        return new HashSet<String>() {
            {
                add("category");
                add("brand");
                add("model");
                add("salesAmount");
                add("date");
            }
        };
    }

    /**
     * Invoke the service with a map of named parameters (any subset of the supported
     * list of parameters) and return the results.
     *
     * @param params a map from parameter names to values
     * @return list of sales
     */
    @GetMapping("/invokeSales")
    public List<Sales> invoke(@RequestParam Map<String, Object> params) {
        SearchSpecification<Sales> query = new SearchSpecification<>(params);
        return salesRepository.findAll(query, JpaSort.by(Sort.Direction.ASC, "date"));
    }
}
