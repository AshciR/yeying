package flight_system;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class used to represent a flight filter based on flights.
 * @author Daoheng guo
 **/

public class FlightFilter {
	private ArrayList<Flight> flightList;
	
	/**
	 * Creates an flight filter object without any flights in the list.
	 */
	public FlightFilter(){
		this.flightList =  new ArrayList<Flight>();
	}
	
	/**
	 * Add a flight to flightfilter
	 */
	public void addFlight(Flight flight){
		this.flightList.add(flight);
	}
	
	/**
	 * This method returns the cheapest flight from the flight list.
	 * @param isFirstClass true if you want the flight with the cheapest
	 * first class seat. False if you want the flight with the cheapest coach
	 * class seat.
	 * @return the cheapest flight in the list.
	 */
	public Flight cheapestFlight(boolean isFirstClass){
		
		Flight cheapestFlight = flightList.get(0);
		for(Flight flight : flightList){
			if(flight.getTotalCost(isFirstClass)<cheapestFlight.getTotalCost(isFirstClass))
				cheapestFlight=flight;
		}
		return cheapestFlight;
	}
	
	/**
	 * This method returns the shortest flight time from the list
	 * @return The shortest flight in the list
	 */
	public Flight shortestFlightTime(){
		Flight shortestFlight = flightList.get(0);
		for(Flight flight : flightList){
			if(flight.getTotalTime().getTimeInMinutes()<shortestFlight.getTotalTime().getTimeInMinutes())
				shortestFlight=flight;
		}
		return shortestFlight;
	}

	/**
	 * This method returns the minimum layover time.
	 * @return the minimum layover time in the list
	 */
	public Flight minLayover(){
		Flight minLayover = flightList.get(0);
		for(Flight flight : flightList){
			if(flight.getTotalLayoverTime().getTimeInMinutes()<minLayover.getTotalLayoverTime().getTimeInMinutes())
				minLayover=flight;
		}
		return minLayover;
	}
	
	/**
	 * This method sort the flightlist based on the price
	 * @param ascending ture if you want the sort price returned in ascending order
	 * false if you want the sort price returned in descending order
	 * @param isFirstClass ture if you want the sort price returned is first class
	 * false if you want the sort price returned is coach class
	 * @return the sorted flights in a order decide by you
	 */
	public ArrayList<Flight> sortPrice(boolean ascending, boolean isFirstClass){

		ArrayList<Flight> sortedFlights = new ArrayList<Flight>();
		sortedFlights.addAll(this.flightList);
		if(isFirstClass){
			Collections.sort(sortedFlights, Flight.FirstClassPriceComparator);
		}
		else
			Collections.sort(sortedFlights, Flight.CoachClassPriceComparator);

		if (ascending){
			return sortedFlights;
		}
		else{
			Collections.reverse(sortedFlights);
			return sortedFlights;
		}
	}
	
	/**
	 * This method returns the sorted flightlist based on the time
	 * @param ascending ture if you want the sorted returned in ascending order of time
	 * false if you want the sorted returned in descending order of time
	 * @return the sorted flights in a order decide by you
	 */
	public ArrayList<Flight> sortTime(boolean ascending){
		ArrayList<Flight> sortedFlights = new ArrayList<Flight>();
		sortedFlights.addAll(this.flightList);
		
		Collections.sort(sortedFlights, Flight.TotalTimeComparator);
		
		if (ascending){
			return sortedFlights;
		}
		else{
			Collections.reverse(sortedFlights);
			return sortedFlights;
		}
		
	}
	
	/**
	 * This method sort the flightlist based on the layovertime
	 * @param ascending ture if you want the sorted returned in ascending order of layover time
	 * false if you want the sorted returned in descending order of layover time
	 * @return the sorted flights in a order decide by you
	 */
	public ArrayList<Flight> sortLayover(boolean ascending){
		ArrayList<Flight> sortedFlights = new ArrayList<Flight>();
		sortedFlights.addAll(this.flightList);
		
		Collections.sort(sortedFlights, Flight.TotalLayoverComparator);
		
		if (ascending){
			return sortedFlights;
		}
		else{
			Collections.reverse(sortedFlights);
			return sortedFlights;
		}
	}
	
	/**
	 * This method sort the flightlist based on the number of connections
	 * @param ascending ture if you want the sort price returned in ascending order of number of connections
	 * false if you want the sort price returned in descending order of connections
	 * @return the sorted flights in a order decide by you
	 */
	public ArrayList<Flight> sortConnect(boolean ascending){
		ArrayList<Flight> sortedFlights = new ArrayList<Flight>();
		sortedFlights.addAll(this.flightList);
		
		Collections.sort(sortedFlights, Flight.ConnectionComparator);
		
		if (ascending){
			return sortedFlights;
		}
		else{
			Collections.reverse(sortedFlights);
			return sortedFlights;
		}
	}
	
	@Override
	public String toString() {
		return "the cheapest flight is: "+cheapestFlight(true)+".\n"+
				"the shortest flight time is: "+shortestFlightTime()+".\n"+
				"the minimum Layover is: "+minLayover()+".\n"+
				"FlightFilter [sortcheap()=" + sortPrice(true,true) + ", sortshortest()="
				+ sortTime(true) + ", sortLayover()=" + sortLayover(true)
				+ ", sortConnect()=" + sortConnect(true) + "]";
	}
	
}