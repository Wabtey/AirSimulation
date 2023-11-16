package agents;

import java.util.concurrent.Semaphore;

import air_simulation.Aircraft;

public abstract class Agent extends Thread {

    protected int numberOfCustomerServed;
    protected Aircraft aircraft;
    protected Semaphore aircraftSemaphore;

    protected Agent(Aircraft a, Semaphore aircraftSemaphore) {
        numberOfCustomerServed = 0;
        aircraft = a;
        this.aircraftSemaphore = aircraftSemaphore;
    }

    public void reset() {
        numberOfCustomerServed = 0;
    }

    public int getNumberOfCustomerServed() {
        return numberOfCustomerServed;
    }

    @Override
    public void run() {
    }

}
