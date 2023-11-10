
/* AirSimulation class
 *
 * TP of SE (version 2020)
 *
 * AM
 */

import java.util.Random;
import java.util.ArrayList;

public class AirSimulation
{
   private int nAgent1;
   private int nAgent2;
   private int nAgent3;
   private int nAgent4;
   private Aircraft a;
   public final int nagents = 4;

   // Constructor
   public AirSimulation()
   {
      this.nAgent1 = 0;
      this.nAgent2 = 0;
      this.nAgent3 = 0;
      this.nAgent4 = 0;
      this.a = new Aircraft();  // standard model
   }

   // Reference to Aircraft
   public Aircraft getAircraftRef()
   {
      return this.a;
   }

   // Agent1
   public void agent1() throws InterruptedException
   {
      boolean placed = false;
      Random R = new Random();
      ArrayList<Integer> emergRows = this.a.getEmergencyRowList();

      // generating a new Customer
      Customer c = new Customer();

      // randomly pick a seat
      do
      {
         int row = R.nextInt(this.a.getNumberOfRows());
         int col = R.nextInt(this.a.getSeatsPerRow());

         // verifying whether the seat is free
         if (this.a.isSeatEmpty(row,col))
         {
            // if this is an emergency exit seat, and c is over60, then we skip
            if (!emergRows.contains(row) || !c.isOver60() || this.a.numberOfFreeSeats() <= this.a.getSeatsPerRow() * this.a.getNumberEmergencyRows())
            {
               this.a.add(c,row,col);
               placed = true;
            }
         }
      }
      while (!placed && !this.a.isFlightFull());

      // updating counter
      if (placed)  this.nAgent1++;
   }

   // Agent2
   public void agent2() throws InterruptedException
   {
      boolean placed = false;
      ArrayList<Integer> emergRows = this.a.getEmergencyRowList();

      // generating a new Customer
      Customer c = new Customer();

      // searching free seats on the seatMap
      int row = 0;
      while (!placed && !this.a.isFlightFull() && row < this.a.getNumberOfRows())
      {
         int col = 0;
         while (!placed && col < this.a.getSeatsPerRow())
         {
            // verifying whether the seat is free
            if (this.a.isSeatEmpty(row,col))
            {
               // if this is an emergency exit seat, and c needs assistence, then we skip
               if (!emergRows.contains(row) || !c.needsAssistence() || this.a.numberOfFreeSeats() <= this.a.getSeatsPerRow() * this.a.getNumberEmergencyRows())
               {
                  this.a.add(c,row,col);
                  placed = true;
               }
            }
            col++;
         }
         row++;
      }

      // updating counter
      if (placed)  this.nAgent2++;
   }

   // Agent3
   public void agent3() throws InterruptedException
   {
      Random R = new Random();

      // to be completed ...

      this.nAgent3++;
   }

   // Agent4: the virus
   public void agent4() throws InterruptedException
   {
      // to be completed ...

      this.nAgent4++;
   }

   // Resetting
   public void reset()
   {
      this.nAgent1 = 0;
      this.nAgent2 = 0;
      this.nAgent3 = 0;
      this.nAgent4 = 0;
      this.a.reset();
   }

   // Printing
   public String toString()
   {
      String print = "AirSimulation (agent1 : " + this.nAgent1 + ", agent2 : " + this.nAgent2 + ", " +
                                    "agent3 : " + this.nAgent3 + ", agent4 : " + this.nAgent4 + ")\n";
      print = print + a.toString();
      return print;
   }

   // Simulation in sequential (main)
   public static void main(String[] args) throws InterruptedException
   {
      System.out.println("\n** Sequential execution **\n");
      if (args != null && args.length > 0 && args[0] != null && args[0].equals("animation"))
      {
         AirSimulation s = new AirSimulation();
         while (!s.a.isFlightFull())
         {
            s.agent1();
            s.agent2();
            s.agent3();
            s.agent4();
            System.out.println(s + s.a.cleanString());
            Thread.sleep(100);
         }
         System.out.println(s);
      }
      else
      {
         AirSimulation s = new AirSimulation();
         while (!s.a.isFlightFull())
         {
            s.agent1();
            s.agent2();
            s.agent3();
            s.agent4();
         }
         System.out.println(s);
      }
   }
}

