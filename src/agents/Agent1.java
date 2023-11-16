package agents;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import air_simulation.Aircraft;
import air_simulation.Customer;

public class Agent1 extends Agent {

    public Agent1(Aircraft a, Semaphore aircraftSemaphore) {
        super(a, aircraftSemaphore, "Agent1");
    }

    /**
     * Wrote by @Agent1 and @Me (semaphore stuff)
     */
    @Override
    public void run() {
        boolean placed = false;
        Random R = new Random();
        ArrayList<Integer> emergRows = aircraft.getEmergencyRowList();

        // generating a new Customer
        Customer c = new Customer();

        // randomly pick a seat
        do {
            // OPTIMIZE: Those two line might be in the critical section (?)
            int row = R.nextInt(aircraft.getNumberOfRows());
            int col = R.nextInt(aircraft.getSeatsPerRow());

            try {
                aircraftSemaphore.acquire();
            } catch (InterruptedException e) {
                Logger.getGlobal().warning("Agent1: Semaphore Acquirement Interrupted!\n" + e);
                Thread.currentThread().interrupt();
            }

            /* v--------------------------- critical section ---------------------------v */
            // verifying whether the seat is free
            if (aircraft.isSeatEmpty(row, col)) {
                // if this is an emergency exit seat, and c is over60, then we skip
                if (!emergRows.contains(row) || !c.isOver60()
                        || aircraft.numberOfFreeSeats() <= aircraft.getSeatsPerRow()
                                * aircraft.getNumberEmergencyRows()) {
                    aircraft.add(c, row, col);
                    placed = true;
                }
            }
            /* ^--------------------------- critical section ---------------------------^ */

            aircraftSemaphore.release();
        } while (!placed && !aircraft.isFlightFull());

        // updating counter
        if (placed)
            numberOfCustomerServed++;
    }
}
