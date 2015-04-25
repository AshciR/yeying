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
		System.out.print("\nWhat day do you want to leave? ");
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
	public void confirmFlight(int fltNum) {
		System.out.println("The ticket for your flight: " + fltNum + " was bought.");
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

}
