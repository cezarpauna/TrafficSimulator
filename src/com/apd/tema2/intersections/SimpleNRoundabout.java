package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class SimpleNRoundabout implements Intersection {
    public int noCarsAllowed; // no cars allowed in intersection
    public int passingTime; // passing time of a car
    public Semaphore s; // semaphore for no cars allowed in intersection

    public SimpleNRoundabout(int noCarsAllowed, int passingTime, Semaphore s) {
        this.noCarsAllowed = noCarsAllowed;
        this.passingTime = passingTime;
        this.s = s;
    }
}
