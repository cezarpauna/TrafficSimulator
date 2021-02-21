package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Railroad implements Intersection {

    public boolean trainPassed; // aux variable for printing some msg once
    public final List<Car> carsWaitingLane0; // arrayList for lane 0
    public final List<Car> carsWaitingLane1; // arrayList for lane 1

    public CyclicBarrier bar; // barrier to wait for the train

    public Railroad() {
        trainPassed = false;
        carsWaitingLane0 = Collections.synchronizedList(new ArrayList<>());
        carsWaitingLane1 = Collections.synchronizedList(new ArrayList<>());
        bar = new CyclicBarrier(Main.carsNo);
    }
}
