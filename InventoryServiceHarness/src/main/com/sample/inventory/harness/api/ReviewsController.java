package com.sample.inventory.harness.api;

import com.sample.inventory.harness.entity.Reviews;
import com.sample.inventory.harness.entity.ReviewsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class ReviewsController implements ServiceEndpoint<Reviews> {
    /**
     * Reviews Repository
     */
    private final ReviewsRepository reviewsRepository;

    public ReviewsController(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    /**
     * @return the maximum number of concurrent invocations allowed, or {@code -1}
     * if unlimited.
     */
    @GetMapping("/reviews/config/max-concurrent")
    public int getMaxConcurrentInvocations() {
        return 10;
    }

    /**
     * @return the set of parameter names supported by this service
     */
    @GetMapping("/reviews/config/supported-params")
    public Set<String> getSupportedParameters() {
        return new HashSet<String>() {
            {
                add("category");
                add("brand");
                add("model");
                add("rating");
            }
        };
    }

    /**
     * Invoke the service with a map of named parameters (any subset of the supported
     * list of parameters) and return the results.
     *
     * @param params a map from parameter names to values
     * @return list of reviews
     */
    @GetMapping("/invokeReviews")
    public List<Reviews> invoke(@RequestParam Map<String, Object> params) {
        SearchSpecification<Reviews> query = new SearchSpecification<>(params);
        return reviewsRepository.findAll(query);
    }
}
