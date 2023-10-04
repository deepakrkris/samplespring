package com.project.servicemanager.registry;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @class BitMapIntersection
 * <p>
 * This class provides a Set intersection algorithm
 * It takes a list of bitmaps , each bitmap's positions represent inclusion of a member in the set
 * Doing a bitmap AND on all the sets provides a list of members that are included in all sets
 * <p>
 * for instance
 * pos 1 - member1
 * pos 2 - member2
 * pos 3 - member3
 * <p>
 * bitmap1 :  1 0 1
 * bitmap2 :  0 0 1
 * <p>
 * intersection tells member3 is in both sets
 * </p>
 */
public class BitMapIntersection {
    private static Logger log = Logger.getLogger(String.valueOf(BitMapIntersection.class));

    public static List<String> intersect(
            Set<String> parameters,
            HashMap<String, List<String>> params_to_services_map,
            Map<String, ServiceBitMap> params_to_service_bitmap,
            Map<Integer, String> registered_services) {
        log.log(Level.FINEST, "Starting set intersection : " + System.currentTimeMillis());
        BitSet temp = new BitSet(320000);
        temp.set(0, 320000, true);
        ServiceBitMap intersectionSet = new ServiceBitMap(temp);

        for (String p : parameters) {
            //List<String> list_of_serviceIds = params_to_services_map.get(p);
            intersectionSet = intersectionSet.intersection(params_to_service_bitmap.get(p));
        }
        log.log(Level.FINEST, "End set intersection : " + System.currentTimeMillis());
        return intersectionSet.toList(registered_services);
    }
}
