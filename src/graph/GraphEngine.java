package graph;

import java.util.ArrayList;
import java.util.Iterator;

import org.graphstream.graph.*;

import flight_system.Airport;
import flight_system.FlightLeg;

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
}
