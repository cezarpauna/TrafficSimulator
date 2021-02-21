package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.List;

public class PriorityIntersection implements Intersection {

    public boolean fullIntersection; // to see if we can enter the intersection with low priority cars
    public List<Car> highPriorityQueue; // list of high priority cars
    public List<Car> lowPriorityQueue; // list of low priority cars
    public int noCarsHighPriority;
    public int noCarsLowPriority;

    public PriorityIntersection(int high, int low) {
        this.fullIntersection = false;
        this.noCarsHighPriority = high;
        this.noCarsLowPriority = low;
        this.highPriorityQueue = new ArrayList<>(noCarsHighPriority);
        this.lowPriorityQueue = new ArrayList<>(noCarsLowPriority);
    }


}
