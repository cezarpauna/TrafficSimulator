package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleMaintenance implements Intersection {

    public Semaphore s0, s1; // semaphores for lanes
    public int x; // max
    public int lane0, lane1; // aux variables for crt no car on lanes

    public CyclicBarrier bar;

    public SimpleMaintenance(int x) {
        this.x = x;
        this.s0 = new Semaphore(x);
        this.s1 = new Semaphore(0);
        this.lane0 = 0;
        this.lane1 = 0;
        this.bar = new CyclicBarrier(Main.carsNo);
    }

}
