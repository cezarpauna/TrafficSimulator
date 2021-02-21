package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleStrictXRoundabout implements Intersection {
    public int noLanes;
    public int passingTime;
    public List<Semaphore> lanes;
    public CyclicBarrier barEntry, barExit;

    public SimpleStrictXRoundabout(int noLanes, int passingTime, int x) {
        this.passingTime = passingTime;
        this.noLanes = noLanes;
        lanes = new ArrayList<Semaphore>();
        // not with 1 as in strict 1
        for (int i = 0; i < noLanes; i++) {
            lanes.add(new Semaphore(x));
        }
        barEntry = new CyclicBarrier(Main.carsNo);
        // cars in intersection (no lanes * max cars per lane)
        barExit = new CyclicBarrier(noLanes * x);
    }
}
