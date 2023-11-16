package agents;

import java.util.ArrayList;
import java.util.Random;

import air_simulation.Aircraft;
import air_simulation.Customer;

public class Agent1 extends Agent {

    public Agent1(Aircraft a) {
        super(a);
    }

    @Override
    public void run() {
        boolean placed = false;
        Random R = new Random();
        ArrayList<Integer> emergRows = aircraft.getEmergencyRowList();

        // generating a new Customer
        Customer c = new Customer();

        // randomly pick a seat
        do {
            int row = R.nextInt(aircraft.getNumberOfRows());
            int col = R.nextInt(aircraft.getSeatsPerRow());

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
        } while (!placed && !aircraft.isFlightFull());

        // updating counter
        if (placed)
            numberOfCustomerServed++;
    }
}
