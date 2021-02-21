package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SimpleStrict1Roundabout implements Intersection {
    public int noLanes;
    public int passingTime;
    public List<Semaphore> lanes; // each lane has one semaphore

    public SimpleStrict1Roundabout(int noLanes, int passingTime) {
        this.passingTime = passingTime;
        this.noLanes = noLanes;
        lanes = new ArrayList<Semaphore>();
        for (int i = 0; i < noLanes; i++) {
            lanes.add(new Semaphore(1));
        }
    }
}
