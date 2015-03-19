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
	private int firstClassSeatsAvail; // Number of seats available
	private double coachClassPrice;
	private int coachClassSeatsAvail; // Number of seats available
	
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
			double firstClassPrice, int firstClassSeatsAvail,
			double coachClassPrice, int coachClassSeatsAvail) {
	
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
		this.firstClassSeatsAvail = firstClassSeatsAvail;
		this.coachClassPrice = coachClassPrice;
		this.coachClassSeatsAvail = coachClassSeatsAvail;
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
		return firstClassSeatsAvail;
	}

	public double getCoachClassPrice() {
		return coachClassPrice;
	}

	public int getCoachClassSeats() {
		return coachClassSeatsAvail;
	}

	@Override
	public String toString() {
		
		NumberFormat formatter = new DecimalFormat("#0.00");     
		System.out.println(formatter.format(4.0));
		
		return "This flight leg number is: " + flightNum + " and the plane model is " + airplane.getModel() + ".\n" +
			   "This flight leaves " + depatureAirport.getName() + " at " + departureTime.toString() + ".\n" +
			   "It arrives at " + arrivalAirport.getName() + " at " + arrivalTime.toString() + ".\n" + 
			   "First Class seats: " + firstClassSeatsAvail + " at $" + firstClassPrice + " per seat.\n" +
			   "Coach Class seats: " + coachClassSeatsAvail + " at $" + coachClassPrice + " per seat.\n";
		
	}
	
	
}
