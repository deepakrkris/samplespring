package com.sample.inventory.harness.api;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @class SearchSpecification
 * <p>
 * This class implements a search specification using spring's Specification interface
 * <p>
 * It creates a predicate with all the passed in query params
 * Additionally, it also implements a custon range query per field in the format of __from: and __fromDate:
 * <p>
 * for example,
 * http://host:port/method?rating=__from:2.5;to:3.5
 * http://host:port/method?date=__fromDate:12-10-2023
 */
public class SearchSpecification<T> implements Specification<T> {
    private Logger log = Logger.getLogger(String.valueOf(getClass()));
    private Map<String, Object> params;

    public SearchSpecification(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * Creates a JPA predicate using the query params passed
     *
     * @param root
     * @param query
     * @param criteriaBuilder
     * @return
     */
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        for (String key : params.keySet()) {
            Object value = params.get(key);

            // handle range queries for all data types except date
            if (value instanceof String && ((String) value).startsWith("__from:")) {
                String[] range = ((String) value).split(";");
                if (range.length == 0) {
                    range[0] = (String) value;
                }

                log.log(Level.FINEST, "range query" + Arrays.toString(range));

                if (range.length > 0)
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(key), range[0].substring(7)));
                if (range.length > 1 && range[1].startsWith("__to:"))
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(key), range[1].substring(5)));

                //handle range queries for Date
            } else if (value instanceof String && ((String) value).startsWith("__fromDate:")) {
                String[] range = ((String) value).split(";");
                if (range.length == 0) {
                    range[0] = (String) value;
                }

                log.log(Level.FINEST, "range query" + Arrays.toString(range));
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                if (range.length > 0) {
                    try {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(key), formatter.parse(range[0].substring(11))));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (range.length > 1 && range[1].startsWith("__toDate:")) {
                    try {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(key), formatter.parse(range[1].substring(9))));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                // no range query, simple equals relation
                predicates.add(criteriaBuilder.equal(root.get(key), params.get(key)));
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
