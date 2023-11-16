package agents;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import air_simulation.Aircraft;
import air_simulation.Customer;

public class Agent3 extends Agent {

    public Agent3(Aircraft a, Semaphore aircraftSemaphore) {
        super(a, aircraftSemaphore, "Agent3");
    }

    /**
     * Wrote by @Agent3 and @Me (semaphore stuff)
     */
    @Override
    public void run() {
        long start = System.currentTimeMillis();

        while (!aircraft.isFlightFull()) {
            Random R = new Random();

            int row1 = R.nextInt(aircraft.getNumberOfRows());
            int col1 = R.nextInt(aircraft.getSeatsPerRow());
            Customer c1 = aircraft.getCustomer(row1, col1);

            int row2 = R.nextInt(aircraft.getNumberOfRows());
            int col2 = R.nextInt(aircraft.getSeatsPerRow());
            Customer c2 = aircraft.getCustomer(row2, col2);

            if (c1 != null && c2 != null && c2.getFlyerLevel() > c1.getFlyerLevel()) {

                try {
                    aircraftSemaphore.acquire();
                } catch (InterruptedException e) {
                    Logger.getGlobal().warning("Agent3: Semaphore Acquirement Interrupted!\n" + e);
                    Thread.currentThread().interrupt();
                }

                /* v--------------------------- critical section ---------------------------v */
                aircraft.freeSeat(row1, col1);
                aircraft.freeSeat(row2, col2);

                aircraft.add(c2, row1, col1);
                aircraft.add(c1, row2, col2);
                /* ^--------------------------- critical section ---------------------------^ */

                aircraftSemaphore.release();
            }

            numberOfCustomerServed++;

            /* ------------------------- Wait the next customer ------------------------- */
            if (Boolean.TRUE.equals(animation)) {
                try {
                    Thread.sleep(105);
                } catch (InterruptedException e) {
                    Logger.getGlobal().warning("Agent3 Sleep Interrupted!");
                    /* Clean up whatever needs to be handled before interrupting */
                    Thread.currentThread().interrupt();
                }
            }
        }

        long finish = System.currentTimeMillis();
        this.timeElapsed = finish - start;

    }
}
