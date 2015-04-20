package flight_system;

import java.util.ArrayList;
import java.util.Collections;

public class FlightFilter {
	private ArrayList<Flight> flightList;

	public FlightFilter(){
		this.flightList =  new ArrayList<Flight>();
	}
	
	public void addFlight(Flight flight){
		this.flightList.add(flight);
	}
	
	public Flight cheapestFlight(boolean isFirstClass){
		
		Flight cheapestFlight = flightList.get(0);
		for(Flight flight : flightList){
			if(flight.getTotalCost(isFirstClass)<cheapestFlight.getTotalCost(isFirstClass))
				cheapestFlight=flight;
		}
		return cheapestFlight;
	}
	
	public Flight shortestFlightTime(){
		Flight shortestFlight = flightList.get(0);
		for(Flight flight : flightList){
			if(flight.getTotalTime().getTimeInMinutes()<shortestFlight.getTotalTime().getTimeInMinutes())
				shortestFlight=flight;
		}
		return shortestFlight;
	}

	public Flight minLayover(){
		Flight minLayover = flightList.get(0);
		for(Flight flight : flightList){
			if(flight.getTotalLayoverTime().getTimeInMinutes()<minLayover.getTotalLayoverTime().getTimeInMinutes())
				minLayover=flight;
		}
		return minLayover;
	}
	
	public ArrayList<Flight> sortPrice(boolean ascending, boolean isFirstClass){
		ArrayList<Flight> sortedFlights=new ArrayList<Flight>();

		ArrayList<Double> flightPriceList = new ArrayList<Double>();

		for(Flight flight : flightList){
			flightPriceList.add(flight.getTotalCost(isFirstClass));
		}

		Collections.sort(flightPriceList); //ascending order
		
		/* If not ascending order, reverse the list */
		if (!ascending){
			Collections.reverse(flightPriceList);
		}

		for(Double price : flightPriceList){

			for(Flight flight : flightList){
				if (price == flight.getTotalCost(isFirstClass)){

					sortedFlights.add(flight);
				}
			}
		}

		return sortedFlights;
	}
	
	public ArrayList<Flight> sortTime(boolean ascending){
		ArrayList<Flight> sortedFlights=new ArrayList<Flight>();

		ArrayList<Integer> flightTimeList = new ArrayList<Integer>();

		for(Flight flight : flightList){
			flightTimeList.add(flight.getTotalTime().getTimeInMinutes());
		}

		Collections.sort(flightTimeList); //ascending order
		
		/* If not ascending order, reverse the list */
		if (!ascending){
			Collections.reverse(flightTimeList);
		}

		for(Integer time : flightTimeList){

			for(Flight flight : flightList){
				if (time == flight.getTotalTime().getTimeInMinutes()){

					sortedFlights.add(flight);
				}
			}
		}

		return sortedFlights;
	}
	
	public ArrayList<Flight> sortLayover(boolean ascending){
		ArrayList<Flight> sortedFlights=new ArrayList<Flight>();

		ArrayList<Integer> flightLayoverList = new ArrayList<Integer>();

		for(Flight flight : flightList){
			flightLayoverList.add(flight.getTotalLayoverTime().getTimeInMinutes());
		}

		Collections.sort(flightLayoverList); //ascending order
		
		/* If not ascending order, reverse the list */
		if (!ascending){
			Collections.reverse(flightLayoverList);
		}

		for(Integer layover : flightLayoverList){

			for(Flight flight : flightList){
				if (layover == flight.getTotalLayoverTime().getTimeInMinutes()){
					sortedFlights.add(flight);
				}
			}
		}

		return sortedFlights;
	}
	
	public ArrayList<Flight> sortConnect(boolean ascending){
		ArrayList<Flight> sortedFlights=new ArrayList<Flight>();

		ArrayList<Integer> flightConnectList = new ArrayList<Integer>();

		for(Flight flight : flightList){
			flightConnectList.add(flight.getNumOfConnection());
		}

		Collections.sort(flightConnectList); //ascending order
		
		/* If not ascending order, reverse the list */
		if (!ascending){
			Collections.reverse(flightConnectList);
		}

		for(Integer connect : flightConnectList){

			for(Flight flight : flightList){
				if (connect == flight.getNumOfConnection()){
					sortedFlights.add(flight);
				}
			}
		}

		return sortedFlights;
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