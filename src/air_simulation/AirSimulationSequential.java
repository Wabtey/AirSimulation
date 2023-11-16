package air_simulation;

import java.util.Random;
import java.util.ArrayList;

/* AirSimulation class in Sequential
 *
 * TP of SE (version 2020)
 *
 * AM
 */
public class AirSimulationSequential {

    private Aircraft a;
    public final int nagents = 4;

    private int nAgent1;
    private int nAgent2;
    private int nAgent3;
    private int nAgent4;

    /**
     * Constructor
     */
    public AirSimulationSequential() {
        this.a = new Aircraft(); // standard model
        this.nAgent1 = 0;
        this.nAgent2 = 0;
        this.nAgent3 = 0;
        this.nAgent4 = 0;
    }

    /**
     * Reference to Aircraft
     */
    public Aircraft getAircraftRef() {
        return this.a;
    }

    /**
     * Wrote by @Agent1
     */
    public void agent1() throws InterruptedException {
        boolean placed = false;
        Random R = new Random();
        ArrayList<Integer> emergRows = this.a.getEmergencyRowList();

        // generating a new Customer
        Customer c = new Customer();

        // randomly pick a seat
        do {
            // OPTIMIZE: Those two line might be in the critical section (?)
            int row = R.nextInt(this.a.getNumberOfRows());
            int col = R.nextInt(this.a.getSeatsPerRow());

            // verifying whether the seat is free
            if (this.a.isSeatEmpty(row, col)) {
                // if this is an emergency exit seat, and c is over60, then we skip
                if (!emergRows.contains(row) || !c.isOver60()
                        || this.a.numberOfFreeSeats() <= this.a.getSeatsPerRow() * this.a.getNumberEmergencyRows()) {
                    this.a.add(c, row, col);
                    placed = true;
                }
            }
        } while (!placed && !this.a.isFlightFull());

        // updating counter
        if (placed)
            this.nAgent1++;
    }

    /**
     * Agent2
     */
    public void agent2() throws InterruptedException {
        boolean placed = false;
        ArrayList<Integer> emergRows = this.a.getEmergencyRowList();

        // generating a new Customer
        Customer c = new Customer();

        // searching free seats on the seatMap
        int row = 0;
        while (!placed && !this.a.isFlightFull() && row < this.a.getNumberOfRows()) {
            int col = 0;
            while (!placed && col < this.a.getSeatsPerRow()) {
                // verifying whether the seat is free
                if (this.a.isSeatEmpty(row, col)) {
                    // if this is an emergency exit seat, and c needs assistence, then we skip
                    if (!emergRows.contains(row) || !c.needsAssistence() || this.a
                            .numberOfFreeSeats() <= this.a.getSeatsPerRow() * this.a.getNumberEmergencyRows()) {
                        this.a.add(c, row, col);
                        placed = true;
                    }
                }
                col++;
            }
            row++;
        }

        // updating counter
        if (placed)
            this.nAgent2++;
    }

    /**
     * Agent3
     */
    public void agent3() throws InterruptedException {
        Random R = new Random();

        int row1 = R.nextInt(this.a.getNumberOfRows());
        int col1 = R.nextInt(this.a.getSeatsPerRow());
        Customer c1 = this.a.getCustomer(row1, col1);

        int row2 = R.nextInt(this.a.getNumberOfRows());
        int col2 = R.nextInt(this.a.getSeatsPerRow());
        Customer c2 = this.a.getCustomer(row2, col2);

        if (c1 != null && c2 != null && c2.getFlyerLevel() > c1.getFlyerLevel()) {
            this.a.freeSeat(row1, col1);
            this.a.freeSeat(row2, col2);

            this.a.add(c2, row1, col1);
            this.a.add(c1, row2, col2);
        }

        this.nAgent3++;
    }

    /**
     * Agent4: the virus
     */
    public void agent4() throws InterruptedException {
        // to be completed ...

        this.nAgent4++;
    }

    /**
     * Resetting
     */
    public void reset() {
        this.a.reset();

        this.nAgent1 = 0;
        this.nAgent2 = 0;
        this.nAgent3 = 0;
        this.nAgent4 = 0;
    }

    /**
     * Printing
     */
    public String toString() {
        String print = "AirSimulation (agent1 : " + this.nAgent1 +
                ", agent2 : " + this.nAgent2 +
                ", agent3 : " + this.nAgent3 +
                ", agent4 : " + this.nAgent4 + ")\n";
        print = print + a.toString();
        return print;
    }

    public static void main(String[] args) throws InterruptedException {
        sequentialAirport(args);
    }

    /**
     * Simulation in sequential.
     */
    public static void sequentialAirport(String[] args) throws InterruptedException {
        System.out.println("\n** Sequential execution **\n");
        long start = System.currentTimeMillis();

        if (args != null && args.length > 0 && args[0] != null && args[0].equals("animation")) {
            AirSimulationSequential s = new AirSimulationSequential();

            while (!s.a.isFlightFull()) {
                s.agent1();
                s.agent2();
                s.agent3();
                s.agent4();

                System.out.println(s + s.a.cleanString());
                Thread.sleep(100);
            }

            System.out.println(s);
        } else {
            AirSimulationSequential s = new AirSimulationSequential();

            while (!s.a.isFlightFull()) {
                s.agent1();
                s.agent2();
                s.agent3();
                s.agent4();
            }

            System.out.println(s);
        }

        long finish = System.currentTimeMillis();
        float timeElapsed = finish - start;
        System.out.println("All cutomers served in " + timeElapsed / 1000 + "s");
    }
}
