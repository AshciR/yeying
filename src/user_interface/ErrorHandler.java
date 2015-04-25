package user_interface;

/**
 * Class used to display the potential error messages, 
 * which may be caused by inappropriate user input. 
 * 
 * @author Richard Walker
 *
 */

public class ErrorHandler {
	
	/* Prevents this class from being instantiated */
	private ErrorHandler() {}
	
	/**
	 * Prints an error message that states that the
	 * user tried to input an invalid airport.
	 */
	public static void invalidPort(){
		System.out.println("That is not a valid Airport. ");	

	}
	
	/**
	 * Prints an error message that states that the
	 * departure airport can't be the same as the arrival.
	 */
	public static void samePort(){
		System.out.println("Arrival Aiport can't be the same as the Departure Airport!");
	}
	
	/**
	 * Prints an error message that states that the
	 * user tried to input an invalid date.
	 */
	public static void invalidDate(){
		System.out.println("That is not a valid Date");
	}
		
	/**
	 * Prints an error message that states the user tried 
	 * to select an invalid flight.
	 */
	public static void invalidFlight(){
		System.out.println("That is not a valid flight");
	}
	
	/**
	 * Prints an error message that states that the
	 * user tried to input a non-numeric character 
	 * for the date (day).
	 */
	public static void notANum(){
		System.out.println("That is not a number!");
	}
	
	/**
	 * Prints an error message that states that the
	 * user did not type "Yes" or "No" for their answer.
	 */
	public static void notYesOrNo(){
		System.out.println("Please type Y or N");
	}
	
	/**
	 * Prints an error message that states that the
	 * ticket could not be bought at this point in time.
	 */
	public static void invalidBuy(){
		System.out.println("I'm sorry, the ticket could not be bought"
				+ "at this point in time, try in a few minutes later");
	}
	
	
}
