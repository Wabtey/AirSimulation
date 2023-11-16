package air_simulation;

/* AirSimulation class
 *
 * TP of SE (version 2020)
 *
 * AM
 */

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import agents.Agent;
// import agents.Agent1;
import agents.Agent2;
import agents.Agent3;
import agents.Agent4;

import java.util.ArrayList;

public class AirSimulationParallel {
    private Aircraft a;
    public final int nagents = 4;

    private int nAgent1Parallel;
    private Agent agent2;
    private Agent agent3;
    private Agent agent4;
    private Semaphore aircraftSemaphore = new Semaphore(1);

    /**
     * Constructor
     */
    public AirSimulationParallel() {
        this.a = new Aircraft(); // standard model
        this.nAgent1Parallel = 0;
        // this.agent1 = new Agent1(a); // This agent is managed directly by the Main
        this.agent2 = new Agent2(a, aircraftSemaphore);
        this.agent3 = new Agent3(a, aircraftSemaphore);
        this.agent4 = new Agent4(a, aircraftSemaphore);
    }

    /**
     * Reference to Aircraft
     */
    public Aircraft getAircraftRef() {
        return this.a;
    }

    /**
     * Wrote by @Agent1 and @Me (semaphore stuff)
     */
    public void agent1Parallel() throws InterruptedException {
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

            try {
                aircraftSemaphore.acquire();
            } catch (InterruptedException e) {
                Logger.getGlobal().warning("Agent1: Semaphore Acquirement Interrupted!\n" + e);
                Thread.currentThread().interrupt();
            }

            /* v--------------------------- critical section ---------------------------v */
            // verifying whether the seat is free
            if (this.a.isSeatEmpty(row, col)) {
                // if this is an emergency exit seat, and c is over60, then we skip
                if (!emergRows.contains(row) || !c.isOver60()
                        || this.a.numberOfFreeSeats() <= this.a.getSeatsPerRow() * this.a.getNumberEmergencyRows()) {
                    this.a.add(c, row, col);
                    placed = true;
                }
            }
            /* ^--------------------------- critical section ---------------------------^ */

            aircraftSemaphore.release();
        } while (!placed && !this.a.isFlightFull());

        // updating counter
        if (placed)
            this.nAgent1Parallel++;
    }

    /**
     * Resetting
     */
    public void reset() {
        this.a.reset();

        this.nAgent1Parallel = 0;
        this.agent2.reset();
        this.agent3.reset();
        this.agent4.reset();
    }

    /**
     * Printing
     */
    public String toString() {
        String print = "AirSimulation (agent1 : " + this.nAgent1Parallel +
                ", agent2 : " + this.agent2.getNumberOfCustomerServed() +
                ", agent3 : " + this.agent3.getNumberOfCustomerServed() +
                ", agent4 : " + this.agent4.getNumberOfCustomerServed() + ")\n";
        print = print + a.toString();
        return print;
    }

    /**
     * Simulation in parallel (main + 3 other threads).
     * The other threads must be started just once.
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n** Parallel execution **\n");
        long start = System.currentTimeMillis();

        AirSimulationParallel s = new AirSimulationParallel();
        s.agent2.start();
        s.agent3.start();
        // s.agent4.start();

        if (args != null && args.length > 0 && args[0] != null && args[0].equals("animation")) {
            s.agent2.activateAnimation();
            s.agent3.activateAnimation();
            // s.agent4.activateAnimation();

            while (!s.a.isFlightFull()) {
                s.agent1Parallel();

                // -- Execute after that agent1Parallel finished one placement --
                System.out.println(s + s.a.cleanString());
                Thread.sleep(105);
            }
        } else {
            while (!s.a.isFlightFull())
                s.agent1Parallel();
        }

        s.agent2.join();
        s.agent3.join();
        // s.agent4.join();

        System.out.println(s);

        System.out.println(s.agent2.getAgentTime());
        System.out.println(s.agent3.getAgentTime());
        // System.out.println(s.agent4.getAgentTime());

        long finish = System.currentTimeMillis();
        float timeElapsed = finish - start;
        System.out.println("All cutomers served in " + timeElapsed / 1000 + "s");
    }
}
