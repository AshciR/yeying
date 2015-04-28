package user_interface;

import java.util.Scanner;

/**
 * Class that implements a text user interface for the flight system program.
 * @author Richard Walker
 */

public class TextInterface implements IUserInterface {
	
	/* Scanner object */
	Scanner userInput;
	
	/**
	 * Makes a new text interface object. It initializes a Scanner
	 * object that is used for input from the user.
	 */
	public TextInterface() {
		/* Make the field a new Scanner object */
		userInput = new Scanner(System.in);
			
	}

	@Override
	public void displayWelcomeMsg() {
		System.out.println("\nWelcome to YeYing's Flight Reservation System!");
	}

	@Override
	public String getDepartureAirport() {
		/* Get the Depart Airport from the user */
		System.out.print("\nWhat is the code for your Departure Airport? ");
		return userInput.nextLine();
	}

	@Override
	public String getArrivalAirport() {
		/* Get the Arrival Airport from the user */
		System.out.print("\nWhat is the code for your Arrival Airport? ");
		return userInput.nextLine();
	}

	@Override
	public String getLeavingDate() {
		/* Get the Departure Day */
		System.out.print("\nPlease note that currently we only have flights between"
				+ "May 8, 2015 and May 18, 2015.");
		System.out.print("What day do you want to leave? ");
		return userInput.nextLine();
	}

	@Override
	public String roundTrip() {
		System.out.print("\nDo you want to book a round trip? ");
		return userInput.nextLine();
	}
	
	@Override
	public String getReturnDate() {
		/* Get the Return Day */
		System.out.print("\nWhat day do you want to return? ");
		return userInput.nextLine();
	}

	@Override
	public String isFirstClassSeat() {
		/* Ask if they want First Class */
		System.out.print("\nDo you want first class? (Y/N) \n");
		return userInput.nextLine().toUpperCase();
		
	}

	@Override
	public void numOfFlights(int numOfFlights) {
		System.out.println("\nThere are " + numOfFlights + " available flights: \n");
	}

	@Override
	public void noFlights() {
		System.out.println("\nSorry, there are no flights matching flights: \n");
	}

	@Override
	public String bookFlight() {
		/* Get the Flight Number */
		System.out.print("\nPlease enter the number for the flight you wish to purchase ");
		return userInput.nextLine();
	}

	@Override
	public void confirmFlight() {
		System.out.println("The ticket for your flight was bought sucessfully.");
	}

	@Override
	public String bookAnother() {
		/* Ask if they want continue */
		System.out.print("Do you want book another flight? ");
		return userInput.nextLine().toUpperCase();
	}
	
	@Override
	public void goodbyeMsg() {
		System.out.print("\nThank you for using our system!\n" + "Goodbye!");
	}

	@Override
	public void printFlightOption(int index) {
		System.out.println("Flight Option: " + (index + 1) );
	}

	@Override
	public String wantDetail() {
		/* Ask if they want flight detail */
		System.out.print("\nDo you want more detail about a particular flight?\n"
						 +"(Yes or No)");
		return userInput.nextLine().toUpperCase();
	}

	@Override
	public String getDetailFlight() {
		/* Ask what flight option they want detail about */
		System.out.print("\nWhich flight do you want more detail about?\n"
						+ "(Enter the flight option number)");
		return userInput.nextLine().toUpperCase();
	}

	@Override
	public String getAnotherDetail() {
		System.out.print("\nDo you want detail about another particular flight?\n"
				+"(Yes or No)");
		return userInput.nextLine().toUpperCase();
	}

	@Override
	public void userFlightChoice(int index) {
		System.out.println("You selcted flight option: " + index);	
	}

	@Override
	public void flightDisappear() {
		System.out.println("Whoops! This flight has just been bought out.");
	}

	@Override
	public void dataBaseLocked() {
		System.out.println("Sorry! We can't purchase this flight for you right now. "
				+ "Please try again in a few minutes");
		
	}

	@Override
	public String doFilter() {
		System.out.println("Would you like to filter the flights?\n"
				+ "(Yes or No)");
		return userInput.nextLine().toUpperCase();
	}

	@Override
	public String sortBy() {
		System.out.println("How would you like to filter the flights?\n");
		System.out.println("1: Departure time\n" +
						   "2: Arrival Time\n" +
						   "3: Total Time\n" +
						   "4: Number of Connections\n" +
						   "5: Layover Time\n" +
						   "6: Cost");		
		return userInput.nextLine();
	}

	@Override
	public String sortOrder() {
		System.out.println("Do you want the list in ascending order or descending order\n"
		+ "(A or D)");
		return userInput.nextLine().toUpperCase();
	}

	@Override
	public String askDepFilter() {
		System.out.println("Would you like to filter by departure times?\n"
				+ "(Yes or No)");
		return userInput.nextLine().toUpperCase();
	}

	@Override
	public String askArrFilter() {
		System.out.println("Would you like to filter by arrival times?\n"
				+ "(Yes or No)");
		return userInput.nextLine().toUpperCase();
	}

	@Override
	public String b4OrAfter() {
		System.out.println("Do you want flights before this time, or after this time?\n"
				+ "(B or A)");
		return userInput.nextLine().toUpperCase();
	}

	@Override
	public void askDepTime() {
		System.out.println("What time do you want to depart?");
	}

	@Override
	public String getHours() {
		System.out.println("Hour: ");
		return userInput.nextLine();
	}

	@Override
	public String getMinutes() {
		System.out.println("Minutes: ");
		return userInput.nextLine();
	}

	@Override
	public void searchFlights() {
		System.out.println("Searching for flights...");		
	}
	
	
	
}
