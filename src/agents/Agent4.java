package agents;

import air_simulation.Aircraft;

public class Agent4 extends Agent {

    public Agent4(Aircraft a) {
        super(a);
    }

    @Override
    public void run() {
        while (!aircraft.isFlightFull())
            numberOfCustomerServed++;
    }
}
