package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class ComplexMaintenance implements Intersection {

    // old = N, new = M;
    public int M, N;
    public List<Queue<Car>> newLanes;
    public List<Queue<Car>> oldLanes;
    public CyclicBarrier bar;
    public int maxCars;
    public int[] crtCars;
    public Semaphore[] sem;

    public ComplexMaintenance(int M, int N, int maxCars) {
        this.N = N;
        this.M = M;
        this.maxCars = maxCars;
        this.crtCars = new int[M];
        this.sem = new Semaphore[M];
        for (int i = 0; i < M; i++) {
            this.crtCars[i] = 0;
            this.sem[i] = new Semaphore(maxCars);
        }

        this.newLanes = new ArrayList<>(M);
        this.oldLanes = new ArrayList<>(N);

        for (int i = 0; i < M; i++) {
            this.newLanes.add(new LinkedList<>());
        }

        for (int i = 0; i < N; i++) {
            this.oldLanes.add(new LinkedList<>());
        }
        this.bar = new CyclicBarrier(Main.carsNo);
    }

}
