package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.IntersectionHandler;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.intersections.*;

import java.util.concurrent.BrokenBarrierException;

import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");
                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Car " + car.getId() + " has waited enough, now driving...");
                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // get instance of intersection class
                    SimpleNRoundabout nRoundabout = (SimpleNRoundabout) Main.intersection;

                    System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
                    // acquire intersection
                    try {
                        nRoundabout.s.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has entered the roundabout");
                    // passing the intersection
                    try {
                        sleep(nRoundabout.passingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + nRoundabout.passingTime / 1000 + " seconds");
                    // release lock
                    nRoundabout.s.release();
                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // get instance of intersection class
                    SimpleStrict1Roundabout strict1Roundabout = (SimpleStrict1Roundabout) Main.intersection;

                    System.out.println("Car " + car.getId() + " has reached the roundabout");
                    // each lane has its own semaphore, so acquire
                    try {
                        strict1Roundabout.lanes.get(car.getStartDirection()).acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());
                    // pasing the intersection
                    try {
                        sleep(strict1Roundabout.passingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + strict1Roundabout.passingTime / 1000 + " seconds");
                    // release lock on lane's semaphore
                    strict1Roundabout.lanes.get(car.getStartDirection()).release();
                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // get instance of intersection class
                    SimpleStrictXRoundabout strict1Roundabout = (SimpleStrictXRoundabout) Main.intersection;

                    System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
                    // wait for every thread to reach intersection
                    try {
                        strict1Roundabout.barEntry.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    // acquire thread's semaphore
                    try {
                        strict1Roundabout.lanes.get(car.getStartDirection()).acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " was selected to enter the roundabout from lane " + car.getStartDirection());

                    // wait for cars to be selected
                    try {
                        strict1Roundabout.barExit.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());
                    // passing the intersection
                    try {
                        sleep(strict1Roundabout.passingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + strict1Roundabout.passingTime / 1000 + " seconds");

                    // wait for every car to exit, for new set of cars to enter
                    try {
                        strict1Roundabout.barExit.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    // release specific lane lock
                    strict1Roundabout.lanes.get(car.getStartDirection()).release();
                }
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance
                    SimpleMaxXRoundabout maxRoundabout = (SimpleMaxXRoundabout) Main.intersection;

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    System.out.println("Car " + car.getId() + " has reached the roundabout from lane " + car.getStartDirection());
                    // acquire specific lane semaphore
                    try {
                        maxRoundabout.lanes.get(car.getStartDirection()).acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());
                    // passing the interseciton
                    try {
                        sleep(maxRoundabout.passingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + maxRoundabout.passingTime / 1000 + " seconds");
                    // release lock on specific lane
                    maxRoundabout.lanes.get(car.getStartDirection()).release();
                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance
                    PriorityIntersection priorityInt = (PriorityIntersection) Main.intersection;
                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici

                    // high priority cars
                    if (car.getPriority() > 1) {
                        System.out.println("Car " + car.getId() + " with high priority has entered the intersection");
                        // add to priority list
                        priorityInt.highPriorityQueue.add(car);
                        // current intersection is full (no low priority car can enter the intersection)
                        priorityInt.fullIntersection = true;
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Car " + car.getId() + " with high priority has exited the intersection");
                        // exit the intersection and remove the car
                        priorityInt.highPriorityQueue.remove(car);
                    } else {
                        // add low priority cars to queue
                        System.out.println("Car " + car.getId() + " with low priority is trying to enter the intersection...");
                        priorityInt.lowPriorityQueue.add(car);
                    }

                    // if the queue of high priority cars is empty, low priority cars can enter the intersection
                    if (priorityInt.highPriorityQueue.isEmpty()) {
                        // intersection is not full, so low priority can enter
                        priorityInt.fullIntersection = false;
                        // low priority time is 0, so we empty the queue
                        while (!priorityInt.lowPriorityQueue.isEmpty()) {
                            System.out.println("Car " + priorityInt.lowPriorityQueue.get(0).getId() + " with low priority has entered the intersection");
                            priorityInt.lowPriorityQueue.remove(priorityInt.lowPriorityQueue.get(0));
                        }
                    }

                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                    @Override
                    public void handle(Car car) {

                        // get instance of intersection class
                        Crosswalk crInt = (Crosswalk) Main.intersection;
                        Pedestrians people = Main.pedestrians;

                        synchronized (((Crosswalk) Main.intersection)) {
                            while (!people.isFinished()) {
                                if (((Crosswalk) Main.intersection).light) {
                                    System.out.println("Car " + car.getId() + " has now green light");
                                } else {
                                    System.out.println("Car " + car.getId() + " has now red light");
                                }
                                // wait until pedestrians notify this thread to change light
                                try {
                                    Main.intersection.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            // print one more time the current light
                            if (((Crosswalk) Main.intersection).light) {
                                System.out.println("Car " + car.getId() + " has now green light");
                            } else {
                                System.out.println("Car " + car.getId() + " has now red light");
                            }
                        }
                    }
                };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // get instance of intersection class
                    SimpleMaintenance sMainInt = (SimpleMaintenance) Main.intersection;

                    System.out.println("Car " + car.getId() + " from side number " +
                            car.getStartDirection() + " has reached the bottleneck");

                    // wait for threads to reach bottleneck
                    try {
                        sMainInt.bar.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    // start with direction 0
                    if (car.getStartDirection() == 0) {
                        // acquire current direction's semaphore
                        try {
                            sMainInt.s0.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Car " + car.getId() + " from side number " +
                                car.getStartDirection() + " has passed the bottleneck");
                        // increment number of cars that passed the bottleneck on this direction
                        sMainInt.lane0++;
                    } else {
                        // acquire current direction's semaphore
                        try {
                            sMainInt.s1.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Car " + car.getId() + " from side number " +
                                car.getStartDirection() + " has passed the bottleneck");
                        // increment number of cars that passed the bottleneck on this direction
                        sMainInt.lane1++;
                    }

                    // if numbers of cars on the first lane reached the allowed maximum
                    if (sMainInt.lane0 == sMainInt.x) {
                        // release the others direction semaphore
                        for (int i = 0; i < sMainInt.x; i++) {
                            sMainInt.s1.release();
                        }
                        // make current number of cars on this lane 0
                        sMainInt.lane0 = 0;
                    }

                    // same as above
                    if (sMainInt.lane1 == sMainInt.x) {
                        for (int i = 0; i < sMainInt.x; i++) {
                            sMainInt.s0.release();
                        }
                        sMainInt.lane1 = 0;
                    }
                }
            };
            case "complex_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // todo
                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // get instance of intersection class
                    Railroad rail = (Railroad) Main.intersection;

                    System.out.println("Car " + car.getId() + " from side number " +
                            car.getStartDirection() + " has stopped by the railroad");

                    // add cars to waiting lists according to the startDirection
                    if (car.getStartDirection() == 0) {
                        rail.carsWaitingLane0.add(car);
                    } else {
                        rail.carsWaitingLane1.add(car);
                    }

                    // wait for threads to reach railroad
                    try {
                        rail.bar.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    // trainPassed = true in order to print only once the given msg
                    if (!((Railroad) Main.intersection).trainPassed) {
                        ((Railroad) Main.intersection).trainPassed = true;
                        System.out.println("The train has passed, cars can now proceed");
                    }

                    // print cars in the same order using synchronized lists
                    if (car.getStartDirection() == 0) {
                        synchronized (rail.carsWaitingLane0) {
                            System.out.println("Car " + rail.carsWaitingLane0.get(0).getId() +
                                    " from side number " + rail.carsWaitingLane0.get(0).getStartDirection() +
                                    " has started driving");
                            rail.carsWaitingLane0.remove(0);
                        }
                    } else {
                        synchronized (rail.carsWaitingLane1) {
                            System.out.println("Car " + rail.carsWaitingLane1.get(0).getId() +
                                    " from side number " + rail.carsWaitingLane1.get(0).getStartDirection() +
                                    " has started driving");
                            rail.carsWaitingLane1.remove(0);
                        }
                    }
                }
            };
            default -> null;
        };
    }
}
