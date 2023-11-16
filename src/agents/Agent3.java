package agents;

import java.util.Random;

import air_simulation.Aircraft;
import air_simulation.Customer;

public class Agent3 extends Agent {

    public Agent3(Aircraft a) {
        super(a);
    }

    /**
     * Wrote by @Agent3
     */
    @Override
    public void run() {
        while (!aircraft.isFlightFull()) {
            Random R = new Random();

            int row1 = R.nextInt(aircraft.getNumberOfRows());
            int col1 = R.nextInt(aircraft.getSeatsPerRow());
            Customer c1 = aircraft.getCustomer(row1, col1);

            int row2 = R.nextInt(aircraft.getNumberOfRows());
            int col2 = R.nextInt(aircraft.getSeatsPerRow());
            Customer c2 = aircraft.getCustomer(row2, col2);

            if (c1 != null && c2 != null && c2.getFlyerLevel() > c1.getFlyerLevel()) {
                aircraft.freeSeat(row1, col1);
                aircraft.freeSeat(row2, col2);

                aircraft.add(c2, row1, col1);
                aircraft.add(c1, row2, col2);
            }

            numberOfCustomerServed++;
        }
    }
}
