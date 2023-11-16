package agents;

import java.util.concurrent.Semaphore;

import air_simulation.Aircraft;

public class Agent4 extends Agent {

    public Agent4(Aircraft a, Semaphore aircraftSemaphore) {
        super(a, aircraftSemaphore);
    }

    @Override
    public void run() {
        // long start = System.currentTimeMillis();

        while (!aircraft.isFlightFull())
            numberOfCustomerServed++;

        // long finish = System.currentTimeMillis();
        // float timeElapsed = finish - start;
        // System.out.println("Agent4's tasks completed in " + timeElapsed / 1000 +
        // "s");
    }
}
