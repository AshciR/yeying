package user_interface;

/** 
 * Interface used to specify what a user interface for the flight system 
 * needs to at least do. Used to abstract the model (flight system) from the view
 * (the user interface). 
 * <p>
 * 
 * @author Richard Walker
 */
public interface IUserInterface {
	
	/**
	 * Displays the welcome message.
	 */
	public void displayWelcomeMsg();
	
	/**
	 * Used to ask the user for the departure airport's 3-digit code.
	 * @return the departure airport as a string from the user.
	 */
	public String getDepartureAirport();
	
	/**
	 * Used to ask the user for the arrival airport's 3-digit code.
	 * @return the arrival airport as a string from the user.
	 */
	public String getArrivalAirport();
	
	/**
	 * Used to ask the user what date they want to leave.
	 * @return the departure date (day) as a string from the user.
	 */
	public String getLeavingDate();
	
	/**
	 * Ask user if they want to book a round trip. 
	 * @return the user's answer. Should be a "yes" or a "no".
	 */
	public String roundTrip();
	
	/**
	 * Used to ask the user what date they want to return.
	 * @return the return date (day) as a string from the user.
	 */
	public String getReturnDate();
	
	/**
	 * Used to ask the user if they want 1st class seating or not.
	 * <p>
	 * The user answer should start with a 'y' for yes
	 * and a 'n' for no.
	 * @return the "yes" or "no" as a string.
	 */
	public String isFirstClassSeat();
	
	/**
	 * Displays a message that tells the user how many flights 
	 * fit their search criteria.
	 * @param numOfFlights the number of available flights
	 */
	public void numOfFlights(int numOfFlights);
	
	/**
	 * Displays a message that tells the user there are no flights 
	 * that fit their search criteria.
	 */
	public void noFlights();
	
	/**
	 * Asks the user which flight they would like to book.
	 */
	public String bookFlight();
	
	/**
	 * Tells the user what flight they bought.
	 */
	public void confirmFlight(int fltNum);
	
	/**
	 * Asks the user if they would like to book another flight.
	 */
	public String bookAnother();

	/**
	 * Tell the user goodbye and thanks for using the system.
	 */
	public void goodbyeMsg();
	
	
}
