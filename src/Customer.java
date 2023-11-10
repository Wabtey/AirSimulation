
/* Customer class
 *
 * TP of SE (version 2020)
 *
 * AM
 */

import java.util.Random;

public class Customer
{
   private int age;
   private int frequentFlyer;
   private int ticketNumber;
   private int flightCost;
   private boolean specialAssistence;

   // Constructor 1 (generates a Customer with specified attributes)
   public Customer(int age,int frequentFlyer,int ticketNumber,int flightCost,boolean specialAssistence)
   {
      this.age = age;
      this.frequentFlyer = frequentFlyer;
      this.ticketNumber = ticketNumber;
      this.flightCost = flightCost;
      this.specialAssistence = specialAssistence;
   }

   // Constructor 2 (generates a Customer with random attributes, the input is the seed)
   public Customer(long seed)
   {
      Random rand = new Random(seed);
      this.age = rand.nextInt(60) + 20;
      this.frequentFlyer = rand.nextInt(10);
      this.ticketNumber = 12345678 + rand.nextInt(87654321);
      this.flightCost = rand.nextInt(1500) + 500;
      double p = rand.nextDouble();
      this.specialAssistence = false;
      if (p < 0.2)  this.specialAssistence = true;
   }

   // Constructor 3 (generates a Customer with random attributes, the seed is random as well)
   public Customer()
   {
      Random rand = new Random();
      this.age = rand.nextInt(60) + 20;
      this.frequentFlyer = rand.nextInt(10);
      this.ticketNumber = 12345678 + rand.nextInt(87654321);
      this.flightCost = rand.nextInt(1500) + 500;
      double p = rand.nextDouble();
      this.specialAssistence = false;
      if (p < 0.2)  this.specialAssistence = true;
   }

   // Get Customer Flyer Level
   public int getFlyerLevel()
   {
      return this.frequentFlyer;
   }

   // Get Flight Cost
   public int getFlightCost()
   {
      return this.flightCost;
   }

   // Checking whether the Customer is over 60
   public boolean isOver60()
   {
      return this.age > 60;
   }

   // Checking whether the Customer needs special assistence
   public boolean needsAssistence()
   {
      return this.specialAssistence;
   }

   // Resetting Customer attributes to "0"
   private void reset()
   {
      this.age = 0;
      this.frequentFlyer = 0;
      this.ticketNumber = 0;
      this.flightCost = 0;
      this.specialAssistence = false;
   }

   @Override
   public Object clone()
   {
      return new Customer(this.age,this.frequentFlyer,this.ticketNumber,this.flightCost,this.specialAssistence);
   }

   @Override
   public boolean equals(Object o)
   {
      boolean answer = true;

      // pre-verifications
      if (o == null)  return !answer;
      boolean isCustomer = (o instanceof Customer);
      if (!isCustomer)  return !answer;

      // comparing the two Customers
      Customer c = (Customer) o;
      answer = answer & (this.age == c.age);
      answer = answer & (this.frequentFlyer == c.frequentFlyer);
      answer = answer & (this.ticketNumber == c.ticketNumber);
      answer = answer & (this.flightCost == c.flightCost);
      answer = answer & (this.specialAssistence == c.specialAssistence);

      return answer;
   }

   @Override
   public int hashCode()
   {
      return Integer.hashCode(this.age) + Integer.hashCode(this.frequentFlyer) + 
             Integer.hashCode(this.ticketNumber) + Integer.hashCode(this.flightCost) +
             Boolean.hashCode(this.specialAssistence);
   }

   // Printing
   public String toString()
   {
      String print;
      print = "(Customer age " + this.age;
      print = print + "; flyer level = " + this.frequentFlyer;
      print = print + "; ticket number = " + this.ticketNumber;
      print = print + "; flight cost = " + this.flightCost;
      if (this.specialAssistence)
         print = print + "; needs special assistence)";
      else
         print = print + ")";
      return print;
   }

   // main (testing)
   public static void main (String [] args)
   {
      System.out.println("Testing Customer Class\n");
      Customer c1 = new Customer(20,1,100,1000,true);
      System.out.println("Customer with specified attributes:\n" + c1);
      Customer c2 = new Customer();
      System.out.println("Customer with random attributes:\n" + c2);
      System.out.println("Testing 'equals' : " + !c1.equals(c2));
      Customer c3 = new Customer(999);
      System.out.println("Customer with random attributes but fixed seed:\n" + c3);
      System.out.println("Testing 'equals' : " + !c2.equals(c3));
      System.out.println("Testing 'equals' : " + c3.equals(new Customer(999)));
      Customer c4 = (Customer) c2.clone();
      System.out.println("Testing 'clone' and 'equals' : " + c2.equals(c4));
      c4.reset();
      System.out.println("Testing 'reset' : " + c4.equals(new Customer()));
      System.out.println();
   }
}

