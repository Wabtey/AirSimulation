package agents;

import java.util.concurrent.Semaphore;

import air_simulation.Aircraft;

public abstract class Agent extends Thread {

    protected int numberOfCustomerServed;
    protected long timeElapsed;
    protected String name;
    protected Boolean animation;
    protected Aircraft aircraft;
    protected Semaphore aircraftSemaphore;

    protected Agent(Aircraft a, Semaphore aircraftSemaphore, String name) {
        numberOfCustomerServed = 0;
        this.animation = false;
        this.name = name;
        aircraft = a;
        this.aircraftSemaphore = aircraftSemaphore;
    }

    public void reset() {
        numberOfCustomerServed = 0;
    }

    public int getNumberOfCustomerServed() {
        return numberOfCustomerServed;
    }

    public String getAgentTime() {
        return name + "'s tasks completed in " + timeElapsed / 1000 + "s";
    }

    public void activateAnimation() {
        this.animation = true;
    }

    @Override
    public void run() {
    }

}
