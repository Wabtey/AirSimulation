package agents;

import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import air_simulation.Aircraft;
import air_simulation.Customer;

public class Agent4 extends Agent {

    public Agent4(Aircraft a, Semaphore aircraftSemaphore) {
        super(a, aircraftSemaphore, "Agent4");
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();

        while (!aircraft.isFlightFull()) {
            for (int i = 0; i < this.aircraft.getNumberOfRows(); i++) {
                for (int j = 0; j < this.aircraft.getSeatsPerRow(); j++) {

                    try {
                        aircraftSemaphore.acquire();
                    } catch (InterruptedException e) {
                        Logger.getGlobal().warning("Agent3: Semaphore Acquirement Interrupted!\n" + e);
                        Thread.currentThread().interrupt();
                    }

                    /* v--------------------------- critical section ---------------------------v */
                    Customer c = this.aircraft.getCustomer(i, j);
                    this.aircraft.freeSeat(i, j);
                    if (c != null)
                        this.aircraft.add(c, i, j);
                    /* ^--------------------------- critical section ---------------------------^ */

                    aircraftSemaphore.release();
                }
            }
            numberOfCustomerServed++;
        }

        long finish = System.currentTimeMillis();
        this.timeElapsed = finish - start;
    }
}
