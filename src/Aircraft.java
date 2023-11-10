
/* Aircraft class
 *
 * TP of SE (version 2020)
 *
 * AM
 */

import java.util.ArrayList;

public class Aircraft
{
   private int nRows;
   private int nSeatsPerRow;
   private int nAisles;
   private int [] aisleSeat;
   private int nEmergencyRows;
   private int [] emergencyRow;
   private int nFreeSeats;
   private volatile Customer [][] seatMap;
   private boolean color = true;

   // Constructor 1 (explicit definition of main attributes)
   public Aircraft(int nRows,int nSeatsPerRow,int[] aisleSeat,int[] emergencyRow)
   {
      try
      {
         // number of rows
         if (nRows < 3) throw new Exception("Aircraft: the aircraft needs at least 3 rows!");
         this.nRows = nRows;

         // number of seats per row
         if (nSeatsPerRow < 2) throw new Exception("Aircraft: the aircraft needs to have at least 2 seats per row!");
         this.nSeatsPerRow = nSeatsPerRow;

         // number of aisles (from 1 aisle, there are 2 corresponding seats
         if (aisleSeat.length < 2) throw new Exception("Aircraft: the aircraft needs to have at least 1 aisle!");
         this.nAisles = aisleSeat.length;

         // location of every aisle
         this.aisleSeat = new int [this.nAisles];
         for (int i = 0; i < this.nAisles; i++)
         {
            if (aisleSeat[i] == 1 || aisleSeat[i] == nAisles - 1)
               throw new Exception("Aircraft: aisles cannot be located next to the windows!");
            this.aisleSeat[i] = aisleSeat[i];
         }

         // number of emergency rows
         if (emergencyRow.length < 2) throw new Exception("Aircraft: the aircraft needs at least 2 security exists!");
         this.nEmergencyRows = emergencyRow.length;

         // location of emergency rows
         this.emergencyRow = new int [this.nEmergencyRows];
         for (int i = 0; i < this.nEmergencyRows; i++)  this.emergencyRow[i] = emergencyRow[i];

         // seat map (null Customer)
         this.seatMap = new Customer [nRows][nSeatsPerRow];
         for (int i = 0; i < nRows; i++)
         {
            for (int j = 0; j < nSeatsPerRow; j++)
            {
               this.seatMap[i][j] = null;
            }
         }

         // initializating counter of free seats
         this.nFreeSeats = nRows*nSeatsPerRow;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         System.exit(1);
      }
   }

   // Constructor 2 (loads a standard model)
   public Aircraft()
   {
      this(32,6,new int[] {3,4},new int[] {0,12,31});
   }

   // Get the Number of Rows
   public int getNumberOfRows()
   {
      return this.nRows;
   }

   // Get the Number of Seats per Row
   public int getSeatsPerRow()
   {
      return this.nSeatsPerRow;
   }

   // Get the List of emergency seats
   public ArrayList<Integer> getEmergencyRowList()
   {
      ArrayList<Integer> list = new ArrayList<Integer> (this.nEmergencyRows);
      for (int i = 0; i < this.nEmergencyRows; i++)  list.add(this.emergencyRow[i]);
      return list;
   }

   // Get the Number of emergency seat rows
   public int getNumberEmergencyRows()
   {
      return this.nEmergencyRows;
   }

   // Verify that a row index is in the bounds
   private boolean isRowInBounds(int row)
   {
      return row >= 0 && row < this.getNumberOfRows();
   }

   // Verify that a column index is in the bounds
   private boolean isColInBounds(int col)
   {
      return col >= 0 && col < this.getSeatsPerRow();
   }

   // Get the Customer in a specific seat
   public Customer getCustomer(int row,int col)
   {
      try
      {
         if (!this.isRowInBounds(row) || !this.isColInBounds(col))
            throw new Exception("Aircraft: impossible to clone: seat coordinates out of bounds");
      }
      catch (Exception e)
      {
         e.printStackTrace();
         System.exit(1);
      }

      if (seatMap[row][col] != null)
         return (Customer) seatMap[row][col].clone();
      return null;
   }

   // Looking for the Customer in the Aircraft with higher flyer frequency
   public Customer mostFrequentFlyer(int level)
   {
      int max = 0;
      Customer c = null;

      for (int i = 0; i < this.nRows; i++)
      {
         for (int j = 0; j < this.nSeatsPerRow; j++)
         {
            if (this.seatMap[i][j] != null)
            {
               int value = this.seatMap[i][j].getFlyerLevel();
               if (value <= level && max < value)
               {
                  c = (Customer) this.seatMap[i][j].clone();
                  max = value;
               }
            }
         }
      }

      return c;
   }

   // Get the Row Number of a given Customer
   public int getRowNumber(Customer c)
   {
      int row = -1;

      if (c != null)
      {
         for (int i = 0; i < this.nRows; i++)
         {
            for (int j = 0; j < this.nSeatsPerRow; j++)
            {
               if (this.seatMap[i][j] != null)  if (this.seatMap[i][j].equals(c))  row = i;
            }
         }
      }

      return row;
   }

   // Get the Seat Number in the Row of a given Customer
   public int getSeatNumberInTheRow(Customer c)
   {
      int col = -1;

      if (c != null)
      {
         for (int i = 0; i < this.nRows; i++)
         {
            for (int j = 0; j < this.nSeatsPerRow; j++)
            {
               if (this.seatMap[i][j] != null)  if (this.seatMap[i][j].equals(c))  col = j;
            }
         }
      }
      return col;
   }
 
   // Checking whether a seat is busy or not
   public boolean isSeatEmpty(int row,int col)
   {
      try
      {
         if (!this.isRowInBounds(row) || !this.isColInBounds(col))
            throw new Exception("Aircraft: impossible to verify whether the seat is empty or not: seat coordinates out of bounds");
      }
      catch (Exception e)
      {
         e.printStackTrace();
         System.exit(1);
      }
      return this.seatMap[row][col] == null;
   }

   // Counting the number of free seats
   public int numberOfFreeSeats()
   {
      return this.nFreeSeats;
   }

   // Checking whether the flight is full
   public boolean isFlightFull()
   {
      return this.nFreeSeats == 0;
   }

   // Freeing a seat of the Aircraft map
   public void freeSeat(int row,int col)
   {
      try
      {
         if (!this.isRowInBounds(row) || !this.isColInBounds(col))
            throw new Exception("Aircraft: impossible to free seat: seat coordinates out of bounds");
      }
      catch (Exception e)
      {
         e.printStackTrace();
         System.exit(1);
      }

      if (this.seatMap[row][col] != null)
      {
         synchronized(this)
         {
            this.nFreeSeats++;
         }
      }
      this.seatMap[row][col] = null;
   }

   // Placing a Customer in a given Aircraft seat
   public void add(Customer c,int row,int col)
   {
      try
      {
         if (c == null) throw new Exception("Aircraft: impossible to add Customer to seat: the given Customer is null");
         if (!this.isRowInBounds(row) || !this.isColInBounds(col))
            throw new Exception("Aircraft: impossible to add Customer to seat: seat coordinates out of bounds");
         if (this.seatMap[row][col] != null)
            throw new Exception("Aircraft: the seat (" + row + "," + col + ") is not empty");
      }
      catch (Exception e)
      {
         e.printStackTrace();
         System.exit(1);
      }

      this.seatMap[row][col] = (Customer) c.clone();
      synchronized(this)
      {
         this.nFreeSeats--;
      }
   }

   // Resetting an empty Aircraft
   public void reset()
   {
      for (int i = 0; i < this.nRows; i++)
      {
         for (int j = 0; j < this.nSeatsPerRow; j++)
         {
            this.seatMap[i][j] = null;
         }
      }
      this.nFreeSeats = this.nRows * this.nSeatsPerRow;
   }

   // Switch off colors in toString (for an incompatibility with the color system)
   public void switchoffColors()
   {
      this.color = false;
   }

   // Clean screen 
   // (creates a String that make the cursor point at the beginning of the last output from toString)
   public String cleanString()
   {
      String print = "";
      if (this.color)
      {
         for (int i = 0; i < 4 + this.getSeatsPerRow() + this.nAisles/2; i++)  print = print + "\033[F";
         print = print + "\r";
      }
      return print;
   }

   // Printing
   public String toString()
   {
      int exit = 0;
      String print = "";

      for (int i = 0; i < this.nRows; i++)
      {
         if (this.emergencyRow[exit] == i)
         {
            print = print + "-|";
            if (exit < this.nEmergencyRows - 1)  exit++;
         }
         else
         {
            print = print + "--";
         }
      }
      print = print + "-\n";

      int aisle = 0;
      for (int j = 0; j < this.nSeatsPerRow; j++)
      {
         if (aisle < this.nAisles - 1)
         {
            if (this.aisleSeat[aisle] == j && this.aisleSeat[aisle+1] == j + 1)
            {
               for (int k = 0; k < this.nRows; k++)  print = print + "--";
               print = print + "-\n";
               aisle = aisle + 2;
            }
         }

         for (int i = 0; i < this.nRows; i++)
         {
            if (this.seatMap[i][j] == null)
            {
               print = print + " \033[1mx\033[0m";
            }
            else
            {
               if (this.color)
               {
                  if (this.seatMap[i][j].needsAssistence())
                     print = print + " \033[31;1m" + this.seatMap[i][j].getFlyerLevel() + "\033[0m";
                  else if (this.seatMap[i][j].isOver60())
                     print = print + " \033[33;1m" + this.seatMap[i][j].getFlyerLevel() + "\033[0m";
                  else
                     print = print + " \033[32;1m" + this.seatMap[i][j].getFlyerLevel() + "\033[0m";
               }
               else
               {
                  print = print + " " + this.seatMap[i][j].getFlyerLevel();
               }
            }
         }
         print = print + "\n";
      }

      exit = 0;
      for (int i = 0; i < this.nRows; i++)
      {
         if (this.emergencyRow[exit] == i)
         {
            print = print + "-|";
            if (exit < this.nEmergencyRows - 1)  exit++;
         }
         else
         {
            print = print + "--";
         }
      }
      print = print + "-\n";

      return print;
   }
   
   // main
   public static void main(String [] args)
   {
      System.out.println("Testing Aircraft class\n");
      Aircraft a = new Aircraft();
      Customer mff = null;  int mffl = -1;
      for (int i = 0; i < a.getNumberOfRows(); i++)
      {
         for (int j = 0; j < a.getSeatsPerRow(); j++)
         {
            Customer C = new Customer((i+1)*(j+10));
            if (mffl < C.getFlyerLevel())
            {
               mffl = C.getFlyerLevel();
               mff = C;
            }
            a.add(C,i,j);
         }
      }
      //a.switchoffColors();  // uncomment to switch off colors
      System.out.println("Aircraft:\n" + a);

      // testing Aircraft methods
      System.out.println("Test 'getNumberOfRows' : " + (a.getNumberOfRows() == 32));
      System.out.println("Test 'getSeatsPerRow' : " + (a.getSeatsPerRow() == 6));
      ArrayList<Integer> L = a.getEmergencyRowList();
      System.out.println("Test 'getEmergencyRowList' : " + (L.contains(0) && L.contains(12) & L.contains(31)));
      Customer C = new Customer(10);
      System.out.println("Test 'getCustomer' : " + (C.equals(a.getCustomer(0,0))));
      System.out.println("Test 'mostFrequentFlyer' : " + (mff.equals(a.mostFrequentFlyer(10))));
      C = a.getCustomer(1,1);
      System.out.println("Test 'getRowNumber' : " + (a.getRowNumber(C) == 1));
      System.out.println("Test 'getSeatNumberInTheRow' : " + (a.getSeatNumberInTheRow(C) == 1));
      System.out.println("Test 'isSeatEmpty' : " + !a.isSeatEmpty(1,1));
      a.freeSeat(1,1);
      System.out.println("Test 'freeSeat' and 'isSeatEmpty' : " + a.isSeatEmpty(1,1));
      a.freeSeat(2,2);
      System.out.println("Test 'numberOfFreeSeats' : " + (a.numberOfFreeSeats() == 2));
      System.out.println("Test 'isFlightFull' : " + !a.isFlightFull());
      a.add(new Customer(99),1,1);
      a.add(new Customer(999),2,2);
      System.out.println("Test 'add' and 'isFlightFull' : " + a.isFlightFull());
      a.reset();
      System.out.println("Test 'reset' : " + (!a.isFlightFull() && a.numberOfFreeSeats() == 32*6));
   }
}

