package graph;

import java.util.Iterator;

import org.graphstream.graph.*;

import flight_system.Airport;
import flight_system.FlightLeg;

public class GraphEngine implements FlightGraph{
	
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
		return timeHasRoute(depNode, arrNode, 0, null);
		
	}
	
	/* Determines if there's a route between two nodes, maximum of 2 connections  */
	private boolean timeHasRoute(Node depNode, Node arrNode, int con, Edge conEdge){
		
		int maxCon = 3; // maximum (3-1) connections
		
		Node nextConNode; // holds the next node for the recursion
		boolean found = false; // holds the result
		FlightLeg conEdgeInfo = null;
		
		/* If the there's a connecting edge, get the info */
		if (conEdge != null){
			conEdgeInfo = conEdge.getAttribute("fltInfo");
		}
		
		/* Get an iterator for the node's departing flights */
		Iterator<Edge> depFlights = depNode.getLeavingEdgeIterator();
		
		/* If less than 2 connections so far */
		if (con < maxCon){
			
			/* Checks if there's a direct Flight from the current node */
			if (depNode.hasEdgeToward(arrNode)) {

				/* If connecting edge is null, this means that this is the 1st check */
				if (conEdge == null){
					found = true;
				}
				/* Have to check if the connecting edge arrival time 
				 * is before the next potential edge depart time */
				else{
					
					/* Check the next flight leaving this node */
					while (depFlights.hasNext()) {
						Edge nxtFlt = depFlights.next();
						FlightLeg nxtFltInfo = nxtFlt.getAttribute("fltInfo");
						
						/* Does this flight leave after the previous one arrives? */
						if (nxtFltInfo.getDepartureTime().getTimeInMinutes() > conEdgeInfo.getArrivalTime().getTimeInMinutes()){
							found = true;
							break;
						}
						
					}
					
				}

			}
			/* No direct connection from the current node */
			else{
				
				/* While the departing node still has flights to search */
				while (depFlights.hasNext() && !found){
					
					/* The current edge (departing flight) info */
					Edge depFltEdge = depFlights.next();
					FlightLeg depFltsInfo = depFltEdge.getAttribute("fltInfo");
					
					/* The next potential Node */
					nextConNode = depFltEdge.getTargetNode();
					
					/* Get an iterator for the potential node's departing flights */
					Iterator<Edge> nxtDepFlights = nextConNode.getLeavingEdgeIterator();
					
					while(nxtDepFlights.hasNext()){
						
						Edge conFlt2nd = nxtDepFlights.next(); // the potential 2nd flight
						FlightLeg conFlt2ndInfo = conFlt2nd.getAttribute("fltInfo"); // get the flight info of the potential 2nd flight
						
						/* If the next connecting edge leaves after the previous edges arrives 
						 * then it is possible that the edge will get us to our final destination */
						if(conFlt2ndInfo.getDepartureTime().getTimeInMinutes() > depFltsInfo.getArrivalTime().getTimeInMinutes()){
							
							con++; // add one to the connections
							
							/* If the potential flight lands at our final destination, 
							 * then we have found the route */
							if(conFlt2nd.getTargetNode().equals(arrNode) && (con< (maxCon-1))){
								found = true;
								break; // stop checking for routes
							}
							/* Check to see if where that flight lands, has a connection to the final destination */
							else{
								if (timeHasRoute(conFlt2nd.getTargetNode(), arrNode, con, conFlt2nd)){
									found = true;
									break;
								}
							}
									
						}
			
					} // end while for next node's departing flights
					
				} // end while for current node's departing flights
				
			} // end if for no direct flight
				
		}
		/* Exceeded two connections, so return false */ 
		else{
			found = false;
		}
		
		/* Return the result */
		return found;
	}
	

	
}
