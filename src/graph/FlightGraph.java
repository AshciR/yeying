package graph;

import flight_system.Airport;

public interface FlightGraph {
	
	/* Tells if there's a direct flight */
	public boolean hasDirectFlight(Airport depPort, Airport arrPort);
	
	/* The interface for the engine to tell if there's a route 
	 * between two airport */
	public boolean hasRoute(Airport depPort, Airport arrPort);
	
}
