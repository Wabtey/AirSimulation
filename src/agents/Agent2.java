package agents;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import air_simulation.Aircraft;
import air_simulation.Customer;

public class Agent2 extends Agent {

    public Agent2(Aircraft a, Semaphore aircraftSemaphore) {
        super(a, aircraftSemaphore);
    }

    /**
     * Wrote by @Agent2 and @Me (semaphore stuff)
     */
    @Override
    public void run() {
        long start = System.currentTimeMillis();

        while (!aircraft.isFlightFull()) {
            boolean placed = false;
            ArrayList<Integer> emergRows = aircraft.getEmergencyRowList();

            // generating a new Customer
            Customer c = new Customer();

            // searching free seats on the seatMap
            int row = 0;
            while (!placed && !aircraft.isFlightFull() && row < aircraft.getNumberOfRows()) {
                int col = 0;
                while (!placed && col < aircraft.getSeatsPerRow()) {

                    try {
                        aircraftSemaphore.acquire();
                    } catch (InterruptedException e) {
                        Logger.getGlobal().warning("Agent2: Semaphore Acquirement Interrupted!\n" + e);
                        Thread.currentThread().interrupt();
                    }

                    /* v--------------------------- critical section ---------------------------v */
                    // verifying whether the seat is free
                    if (aircraft.isSeatEmpty(row, col)) {
                        // if this is an emergency exit seat, and c needs assistence, then we skip
                        if (!emergRows.contains(row) || !c.needsAssistence() || aircraft
                                .numberOfFreeSeats() <= aircraft.getSeatsPerRow() * aircraft.getNumberEmergencyRows()) {
                            aircraft.add(c, row, col);
                            placed = true;
                        }
                    }
                    /* ^--------------------------- critical section ---------------------------^ */

                    aircraftSemaphore.release();
                    col++;
                }
                row++;
            }

            // updating counter
            if (placed)
                numberOfCustomerServed++;

            /* ------------------------- Wait the next customer ------------------------- */
            // try {
            // Thread.sleep(100);
            // } catch (InterruptedException e) {
            // Logger.getGlobal().warning("Agent2 Sleep Interrupted!");
            // /* Clean up whatever needs to be handled before interrupting */
            // Thread.currentThread().interrupt();
            // }
        }

        long finish = System.currentTimeMillis();
        float timeElapsed = finish - start;
        System.out.println("Agent2's tasks completed in " + timeElapsed / 1000 + "s");
    }

}
