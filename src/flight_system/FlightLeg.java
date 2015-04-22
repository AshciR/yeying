/** Engineer: Richard Walker
 * Date: March 18. 2015
 * Description: Class used to represent a flight leg 
 * 				for the flight reservation system.
 */

package flight_system;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FlightLeg {
	
	/* The Fields */
	private Airplane airplane;
	private int flightNum;
	private int flightDuration;
	
	/* Departure Info */
	private Time departureTime;
	private Date departureDate;
	private Airport depatureAirport;
	
	/* Arrival Info */
	private Time arrivalTime;
	private Date arrivalDate;
	private Airport arrivalAirport;
	
	/* Seating Info */
	private double firstClassPrice;
	private int firstClassSeatsOcc; // Number of seats occupied
	private double coachClassPrice;
	private int coachClassSeatsOcc; // Number of seats occupied
	
	/**
	 * @param airplane
	 * @param flightNum
	 * @param flightDuration
	 * @param departureTime
	 * @param departureDate
	 * @param depatureAirport
	 * @param arrivalTime
	 * @param arrivalDate
	 * @param arrivalAirport
	 * @param firstClassPrice
	 * @param firstClassSeatsAvail
	 * @param coachClassPrice
	 * @param coachClassSeatsAvail
	 */
	public FlightLeg(Airplane airplane, int flightNum, int flightDuration,
			Time departureTime, Date departureDate, Airport depatureAirport,
			Time arrivalTime, Date arrivalDate, Airport arrivalAirport,
			double firstClassPrice, int firstClassSeatsOcc,
			double coachClassPrice, int coachClassSeatsOcc) {
	
		this.airplane = airplane;
		this.flightNum = flightNum;
		this.flightDuration = flightDuration;
		this.departureTime = departureTime;
		this.departureDate = departureDate;
		this.depatureAirport = depatureAirport;
		this.arrivalTime = arrivalTime;
		this.arrivalDate = arrivalDate;
		this.arrivalAirport = arrivalAirport;
		this.firstClassPrice = firstClassPrice;
		this.firstClassSeatsOcc = firstClassSeatsOcc;
		this.coachClassPrice = coachClassPrice;
		this.coachClassSeatsOcc = coachClassSeatsOcc;
	}

	/* The getter methods */
	public Airplane getAirplane() {
		return airplane;
	}

	public int getFlightNum() {
		return flightNum;
	}

	public int getFlightDuration() {
		return flightDuration;
	}

	public Time getDepartureTime() {
		return departureTime;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public Airport getDepatureAirport() {
		return depatureAirport;
	}

	public Time getArrivalTime() {
		return arrivalTime;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public Airport getArrivalAirport() {
		return arrivalAirport;
	}

	public double getFirstClassPrice() {
		return firstClassPrice;
	}

	public int getFirstClassSeats() {
		return firstClassSeatsOcc;
	}
	
	/**
	 * Gets the number of First Class seats that are on this flight. 
	 * <p> 
	 * @return the number of seats available on the flight. 
	 * <p>
	 * If the flight is full it will return 0.
	 * If the flight is over-packed, it will return the number of seats
	 * it is over packed by as a negative number. E.g. If the plane can
	 * only seat 10, yet the flight info says it has 12 seats booked, then
	 * it will return -2. 
	 */
	public int getFirstClassSeatsAvail(){
		return airplane.getFirstClassSeats() - firstClassSeatsOcc;
	}
	
	public double getCoachClassPrice() {
		return coachClassPrice;
	}

	public int getCoachClassSeats() {
		return coachClassSeatsOcc;
	}
	
	/**
	 * Gets the number of Coach seats that are on this flight. 
	 * <p> 
	 * @return the number of seats available on the flight. 
	 * <p>
	 * If the flight is full it will return 0.
	 * If the flight is over-packed, it will return the number of seats
	 * it is over packed by as a negative number. E.g. If the plane can
	 * only seat 10, yet the flight info says it has 12 seats booked, then
	 * it will return -2. 
	 */
	public int getCoachClassSeatsAvail(){
		return airplane.getCoachSeats() - coachClassSeatsOcc;
	}
	
	/**
	 * Gets the information about the flight in a format thats
	 * more useful for the user.
	 * <p>
	 * The only difference between this and {@link #toString} is that
	 * this method displays how many seats are available on a flight,
	 * as opposed to the number of seats occupied.
	 * 
	 * @return a string that contains the information about the flight, 
	 * in a user-friendly format.
	 */
	public String userToString(){
		
		NumberFormat priceFormat = new DecimalFormat("#.00");     

		return "This flight leg number is: " + flightNum + " and the plane model is " + airplane.getModel() + ".\n" +
		"This flight leaves " + depatureAirport.getName() + " at " + departureTime.toString() + ".\n" +
		"It arrives at " + arrivalAirport.getName() + " at " + arrivalTime.toString() + ".\n" + 
		"Available First Class seats: " + getFirstClassSeatsAvail() + " at $" + priceFormat.format(firstClassPrice) + " per seat.\n" +
		"Available Coach Class seats: " + getCoachClassSeatsAvail() + " at $" + priceFormat.format(coachClassPrice) + " per seat.\n";

	}
	
	@Override
	public String toString() {
		
		NumberFormat priceFormat = new DecimalFormat("#.00");     
		
		return "This flight leg number is: " + flightNum + " and the plane model is " + airplane.getModel() + ".\n" +
			   "This flight leaves " + depatureAirport.getName() + " at " + departureTime.toString() + ".\n" +
			   "It arrives at " + arrivalAirport.getName() + " at " + arrivalTime.toString() + ".\n" + 
			   "First Class seats: " + firstClassSeatsOcc + " at $" + priceFormat.format(firstClassPrice) + " per seat.\n" +
			   "Coach Class seats: " + coachClassSeatsOcc + " at $" + priceFormat.format(coachClassPrice) + " per seat.\n";
		
	}
	
	
}
