package com.apd.tema2.factory;

import com.apd.tema2.entities.Intersection;

import java.util.HashMap;
import java.util.Map;

/**
 * Prototype Factory: va puteti crea cate o instanta din fiecare tip de implementare de Intersection.
 */
public class IntersectionFactory {
    private static Map<String, Intersection> cache = new HashMap<>();

    static {
        // cache.put("name_intersection", new Intersection() {
        // });
    }
}
