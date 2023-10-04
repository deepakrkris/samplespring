package com.project.servicemanager.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @class ListIntersection
 * <p>
 * This class provides a Set intersection algorithm
 * It takes a mutiple lists of strings , and finds the intersection between all of them using Java's collection methods
 */
public class ListIntersection {
    private static Logger log = Logger.getLogger(String.valueOf(ListIntersection.class));

    public static List<String> intersect(
            Set<String> parameters,
            HashMap<String, List<String>> params_to_services_map) {
        List<String> intersection = null;

        log.log(Level.FINEST, "Starting set intersection : " + System.currentTimeMillis());
        for (String p : parameters) {
            List<String> list_of_serviceIds = params_to_services_map.get(p);

            if (list_of_serviceIds == null) {
                continue;
            }

            if (intersection == null) {
                intersection = new ArrayList<>(list_of_serviceIds);
            } else {
                intersection.retainAll(list_of_serviceIds);
            }
        }

        log.log(Level.FINEST, "End set intersection : " + System.currentTimeMillis());
        return intersection;
    }
}
