package graph;

import java.util.ArrayList;
import java.util.LinkedList;

import org.graphstream.graph.Edge;

import flight_system.Airport;

public interface IFlightGraph {
	
	/* Tells if there's a direct flight */
	public boolean hasDirectFlight(Airport depPort, Airport arrPort);
	
	/* Tells if there's a route between two airports */
	public boolean hasRoute(Airport depPort, Airport arrPort);
	
	/* Gets all the routes between two airports */
	public ArrayList<LinkedList<Edge>> getRoutes(Airport depPort, Airport arrPort, int maxFlights, boolean filterDir);
	
}
