package agents;

import air_simulation.Aircraft;

public abstract class Agent extends Thread {

    protected int numberOfCustomerServed;
    protected Aircraft aircraft;

    protected Agent(Aircraft a) {
        numberOfCustomerServed = 0;
        aircraft = a;
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
