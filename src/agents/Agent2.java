package agents;

import java.util.ArrayList;

import air_simulation.Aircraft;
import air_simulation.Customer;

public class Agent2 extends Agent {

    public Agent2(Aircraft a) {
        super(a);
    }

    /**
     * Wrote by @Agent2
     */
    @Override
    public void run() {
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
                    // verifying whether the seat is free
                    if (aircraft.isSeatEmpty(row, col)) {
                        // if this is an emergency exit seat, and c needs assistence, then we skip
                        if (!emergRows.contains(row) || !c.needsAssistence() || aircraft
                                .numberOfFreeSeats() <= aircraft.getSeatsPerRow() * aircraft.getNumberEmergencyRows()) {
                            aircraft.add(c, row, col);
                            placed = true;
                        }
                    }
                    col++;
                }
                row++;
            }

            // updating counter
            if (placed)
                numberOfCustomerServed++;
        }
    }

}
