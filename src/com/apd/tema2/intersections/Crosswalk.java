package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

public class Crosswalk implements Intersection {
    // true = green; false = red;
    public boolean light;

    public Crosswalk() {
        light = true;
    }
}
