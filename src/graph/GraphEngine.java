package graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.graphstream.graph.*;

import flight_system.Airport;
import flight_system.FlightLeg;
import flight_system.Time;

public class GraphEngine implements IFlightGraph{
	
	Graph flightGraph;

	/* Constructor */
	public GraphEngine(Graph flightGraph) {
		this.flightGraph = flightGraph;
	} 
		
	/* Get the node corresponding to an airport */
	public Node getNode(Airport airport){
		return this.flightGraph.getNode(airport.getCode());
	}
	
	/* Tells if there's a direct flight */
	public boolean hasDirectFlight(Airport depPort, Airport arrPort){
		
		/* Convert Airports to Nodes */
		Node depNode = this.flightGraph.getNode(depPort.getCode());
		Node arrNode = this.flightGraph.getNode(arrPort.getCode());
		
		return depNode.hasEdgeToward(arrNode);
		
	}
	
	/* The interface for the engine to tell if there's a route 
	 * between two airport */
	public boolean hasRoute(Airport depPort, Airport arrPort){
		
		/* Convert Airports to Nodes */
		Node depNode = getNode(depPort);
		Node arrNode = getNode(arrPort);
		
		/* Call the private helper function */
		return timeHasRoute(depNode, arrNode, 0, null, new ArrayList<Node>());
		
	}
	
	public ArrayList<LinkedList<Edge>> getRoutes(Airport depPort, Airport arrPort, int maxFlights){
		
		/* Convert Airports to Nodes */
		Node depNode = getNode(depPort);
		Node arrNode = getNode(arrPort);
		
		/* The list of routes to be returned */
		ArrayList<LinkedList<Edge>> routes = new ArrayList<LinkedList<Edge>>();
		
		/* Call the private method */
		routes = getRoutes(depNode, arrNode, new ArrayList<Node>(), depNode, 0);
		
		/* Filter out the invalid flights*/
		routes = routeFilter(routes, maxFlights);
		
		return routes;
		
	}
	
	/* Determines if there's a route between two nodes, maximum of 2 connections */
	private boolean timeHasRoute(Node depNode, Node arrNode, int con, Edge conEdge, ArrayList<Node> visited) {
		
		int maxCon = 3; // maximum 2 connections
		boolean found = false; // holds the result
		FlightLeg conEdgeInfo = null;
		
		/* If we've been to this node already, we no it's not a route */
		if (visited.contains(depNode) ){
			
			/* If this is a connection check, add this departing node to visited list */
			if (!(conEdge == null)){
				visited.add(depNode);
			}
			
			return false;
		}
		
		/* If less than 2 connections so far */
		else if (con < maxCon) {
			
			/* Get an iterator for the node's departing flights */
			Iterator<Edge> depFlights = depNode.getLeavingEdgeIterator();
			
			/* Checks if there's a direct Flight from the current node */
			if (depNode.hasEdgeToward(arrNode)) {
	
				/*
				 * If connecting edge is null, this means 
				 * that this is the 1st check
				 */
				if (conEdge == null) {
					found = true;
				}
				/*
				 * Have to check if the connecting edge arrival time is before
				 * the next potential edge depart time
				 */
				else {
					
					/* There's a connecting edge, get the info */
					if (conEdge != null) {
						conEdgeInfo = conEdge.getAttribute("fltInfo");
					}
					
					/* Check the next flight leaving this node */
					while (depFlights.hasNext()) {
						
						/* Get the next flight that leaves node */
						Edge nxtFlt = depFlights.next();
						FlightLeg nxtFltInfo = nxtFlt.getAttribute("fltInfo");
						
						boolean beenHere = visited.contains(nxtFlt.getTargetNode());
						
						/*
						 * Does this flight leave after the previous one
						 * arrives, and it doesn't land where we have passed through already
						 */
						if (nxtFltInfo.getDepartureTime().getTimeInMinutes() > conEdgeInfo.getArrivalTime().getTimeInMinutes() && !beenHere){
							found = true;
							break;
						}
	
					} // end while depFlights
	
				} // end if has connecting edges
	
			} // end if has a direct flight
			
			/* No direct connection from the current node */
			else {
				
				/* While the departing node still has flights to search */
				while (depFlights.hasNext() && !found) {
					
					/* The current edge (departing flight) info */
					Edge depFltEdge = depFlights.next();
					FlightLeg depFltsInfo = depFltEdge.getAttribute("fltInfo");
					
					/* The next potential Node */
					Node nextConNode = depFltEdge.getTargetNode();
					
					/*
					 * Get an iterator for the potential node's departing
					 * flights
					 */
					Iterator<Edge> nxtDepFlights = nextConNode.getLeavingEdgeIterator();
					
					/* While the 1st connection has flights to check, and doesn't
					 * land at the airport we're trying to leave from
					 */
					while (nxtDepFlights.hasNext()) {
						
						Edge conFlt2nd = nxtDepFlights.next(); // the potential 2nd flight
						
						if( !(conFlt2nd.getTargetNode().equals(depNode)) ){
							/* Get the flight info of the next potential flight */
							FlightLeg conFlt2ndInfo = conFlt2nd.getAttribute("fltInfo"); 
																
							/*
							 * If the next connecting edge leaves after the previous
							 * edges arrives then it is possible that the edge will
							 * get us to our final destination
							 */
							if (conFlt2ndInfo.getDepartureTime().getTimeInMinutes() > depFltsInfo.getArrivalTime().getTimeInMinutes()) {
		
								con++; // add one to the connections
								
								/* Where the connecting flight lands */
								Node conFltLand = conFlt2nd.getTargetNode(); 
								
								/*
								 * If the potential flight lands at our final
								 * destination, then we have found the route
								 */
								if (conFltLand.equals(arrNode) && (con < (maxCon - 1))) {
									found = true;
									break; // stop checking for routes
								}
								/*
								 * Check to see if where that flight lands, has a
								 * connection to the final destination
								 */
								else {
									
									/* Add the port where the 1st flight landed, to the visited list */
									visited.add(nextConNode);
									
									if (timeHasRoute(conFltLand, arrNode, con, conFlt2nd, visited)) {
										found = true;
										break;
									}
									
									
								}
		
							}
						
						} // end if the connection doesn't land where we started from
					
					} // end while for next node's departing flights
						
					/* We checked the node where the first flight would
					 * have landed, but it doesn't contain a connection to
					 * the final destination, so add it to the list of nodes 
					 * we already visited */
					visited.add(nextConNode);	
					
				} // end while for current node's departing flights
				
			
			} // end if for no direct flight
	
		}
		/* Exceeded two connections, so return false */
		else {
			found = false;
		}
	
		/* Return the result */
		return found;
	}	

	/* Returns all the possible routes from one airport to another */
	private ArrayList<LinkedList<Edge>> getRoutes(Node depNode, Node arrNode, ArrayList<Node> visited, Node originDepNode, int depth) {

		/* List to hold all the routes */
		ArrayList<LinkedList<Edge>> routes = new ArrayList<LinkedList<Edge>>();

		/* If > 3, then too many connections,
		 * return an empty route list */
		if (depth > 3){
			return routes;
		}
		/* We haven't exceeded the maximum connections yet, 
		 * keep searching */
		else{
			
			/* Get a list of all the flights leaving the departure airport */
			Iterator<Edge> depNodeFlights = depNode.getEachLeavingEdge().iterator();

			/*
			 * While there are flights left to check from the departing airport
			 */
			while (depNodeFlights.hasNext()) {

				/* Get the next flight that leaves this airport */
				Edge flight = depNodeFlights.next();
				
				/* Making sure that original departure not the starting node */
				if (originDepNode.equals(flight.getSourceNode())) {
					visited.clear();
					visited.add(originDepNode);
				}

				/*
				 * If where you would land is not in the places that were checked
				 * before
				 */
				if (!visited.contains(flight.getTargetNode())) {

					/*
					 * If this flight lands at the our final destination, then we
					 * know it is a route
					 */
					if (flight.getTargetNode().equals(arrNode)) {

						/* Make a linked list to store the route */
						LinkedList<Edge> currentRoute = new LinkedList<Edge>();

						/* Add the flight to the current route */
						currentRoute.add(flight);

						/* Add the current route to the routes list */
						routes.add(currentRoute);

					}
					/*
					 * There are no direct flights, so let's check for connections
					 */
					else {

						/* Increase the number of connections we have
						 * gone to so far by 1. */
						depth++;
						
						/* Add this departure node to the visited list */
						visited.add(depNode);

						/* Store all the routes from the connection airport */
						ArrayList<LinkedList<Edge>> returnedRoutes = getRoutes(flight.getTargetNode(), arrNode, visited, originDepNode, depth);

						for (LinkedList<Edge> route : returnedRoutes) {

							/*
							 * Add the route from the connecting flight to the
							 * original route
							 */
							route.addFirst(flight);

							/* Make a linked list to store the route */
							LinkedList<Edge> addedRoute = new LinkedList<Edge>();

							/* Make the new Linked List */
							addedRoute = route;

							/* Add the concatenated route to the routes list */
							routes.add(addedRoute);

						}

					}
					
					/* Once we find our destination, we have to set
					 * depth back to 0 for the next depth search for flight */
					depth = 0;
					
				}

			}

			return routes;
			
		}
		
	}
	
	/* Returns a new list routes, that contain only routes that are 
	 * chronologically possible */
	private ArrayList<LinkedList<Edge>> routeFilter(ArrayList<LinkedList<Edge>> routes, int maxFlights){
		
		ArrayList<LinkedList<Edge>> filteredRoutes = new ArrayList<LinkedList<Edge>>();
			
		/* Go through all the routes in the list */
		for (LinkedList<Edge> route : routes){
			
			/* If the route is not valid, 
			 * remove it from the list */
			if ( isRouteValid(route, maxFlights) ){
				filteredRoutes.add(route);
			}
			
		}
		
		/* Return the new filtered list */
		return filteredRoutes;
		
	}
	
	/* Returns true if the flights in the route are in 
	 * chronological order */
	private boolean isRouteValid(LinkedList<Edge> route, int maxFlights) {

		/* If the route size is 1, i.e. it 
		 * is a direct flight, return true */
		if(route.size() == 1 ){
			return true;
		}
		/* If the route has more than 3 flights,
		 * i.e more than 2 connections, then it's not valid
		 */
		else if (route.size() > maxFlights){
			return false;
		}
		/* Check if the connections are in chronological order */
		else{
			
			/* Go through the linked list */
			for (int i = 0; i < (route.size() - 1); i++) {
				
				/* Get the flights' info */
				Edge flightLeg = route.get(i);
				FlightLeg fltInfo = flightLeg.getAttribute("fltInfo");
				Time fltInfoTime = fltInfo.getArrivalTime();
				
				Edge flightLegNxt = route.get(i+1);
				FlightLeg fltNxtInfo = flightLegNxt.getAttribute("fltInfo");
				Time fltNxtInfoTime = fltNxtInfo.getDepartureTime();
				
				/* If this flight arrives after the next flight leaves */
				if(fltInfoTime.compareTo(fltNxtInfoTime) >= 0){
					return false;
				}
		
			}
			
			/* The flights are in chronological order */
			return true;
		}
		
	}
	
	
}
