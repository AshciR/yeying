package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import flight_system.Airplane;
import flight_system.Airport;
import flight_system.Date;
import flight_system.FlightLeg;
import flight_system.Location;
import flight_system.Month;
import flight_system.Time;

public class ExampleGraph {

	private Airplane airplane;
	private Date date;
	private ArrayList<Airport> airports;
	private Graph graph;

	/* Constructor */
	public ExampleGraph() {
		this.airplane = new Airplane("774", "Airbus", 20, 50);
		this.date = new Date(Month.May, 10, 2015);
		this.airports = makeTestAiports();
	};

	/* Test small made-up graph */
 	public void testGraph(){
		
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		ArrayList<LinkedList<Edge>> routes = new ArrayList<LinkedList<Edge>>();
		
		/* Make the graph */
		graph = makeGraph();
		
		/* Nodes */
		Node bos = graph.getNode("BOS");
		Node jfk = graph.getNode("JFK");
		Node atl = graph.getNode("ATL");
		Node mia = graph.getNode("MIA");
		Node sfo = graph.getNode("SFO");
		Node kgn = graph.getNode("KGN");
		
		/* Add the edges (departing flights) to the graph */
		edgeList = addTestEdges(graph);
		
		/* Verifying Graph Data */
		for (Edge edge : edgeList){
			printEdge(edge);
		}
		
//		/* Testing hasEdgeToward() */
//		System.out.println("BOS -> JFK? : " + bos.hasEdgeToward(jfk)); // BOS -> JFK Yes!
//		System.out.println("BOS -> ATL? : " + bos.hasEdgeToward(atl)); // BOS -> ATL No!
//				
//		/* Testing hasRoute */
//		System.out.println("\n------- Testing hasRoute --------");
//		testHasRoute(bos, atl); 
//		testHasRoute(atl, mia);
//		testHasRoute(bos, mia);		

		/* Testing timeHasRoute */
//		System.out.println("\n------- Testing timeHasRoute --------");
//		testTimeRoute(bos, jfk); // True - Direct Flight
//		testTimeRoute(bos, atl); // True - 1 connection	
//		testTimeRoute(bos, mia); // True - 2 connections 
//		testTimeRoute(atl, sfo); // False - 1 connection, but leaves too early
//		testTimeRoute(bos, kgn); // False - 3 connections
//		testTimeRoute(mia, sfo); // False - MIA -> ATL -> BOS -/> SFO
//		testTimeRoute(kgn, mia); // False - KGN -> BOS -> JFK -> ATL -> MIA too many stops
//		testTimeRoute(kgn, atl); // True - KGN -> BOS -> JFK -> ATL
//		testTimeRoute(jfk, mia); // True - JFK -> ATL -> MIA
				
		routes = getRoutes(mia, sfo, new ArrayList<Node>(), mia); // It returns 2 flights
	
		for (LinkedList<Edge> route : routes){
			System.out.println(route);
		}
		
	
	}
	
	/* Make the test airports */
	protected static ArrayList<Airport> makeTestAiports() {
		
		ArrayList<Airport> airports = new ArrayList<Airport>();
		
		/* Clean the list before you make it */
		airports.clear();

		/* Using just one location for all the airports */
		Location loc = new Location(33.641045, -84.427764, false);

		Airport bosPort = new Airport("BOS", "Boston Logan", loc);
		airports.add(bosPort);

		Airport jfkPort = new Airport("JFK", "John F. Kennedy", loc);
		airports.add(jfkPort);

		Airport atlPort = new Airport("ATL", "Hartsfield Jackson", loc);
		airports.add(atlPort);

		Airport miaPort = new Airport("MIA", "Miami", loc);
		airports.add(miaPort);

		Airport sfoPort = new Airport("SFO", "San Francisco", loc);
		airports.add(sfoPort);

		Airport kgnPort = new Airport("KGN", "Norman Manley", loc);
		airports.add(kgnPort);
		
		return airports;

	}

	/* Makes the graph for the small made-up graph */
	private Graph makeGraph() {
		graph = new MultiGraph("Test Flights");

		/* Allow easy creation of edges */
		graph.setAutoCreate(true);

		/* Making Graph */
		graph.addNode("BOS");
		graph.addNode("JFK");
		graph.addNode("ATL");
		graph.addNode("MIA");
		graph.addNode("SFO");
		graph.addNode("KGN");

		graph.addEdge("1000", "BOS", "JFK", true);
		graph.addEdge("1001", "BOS", "SFO", true);

		graph.addEdge("2000", "JFK", "ATL", true);
		graph.addEdge("2001", "JFK", "ATL", true);

		graph.addEdge("3000", "ATL", "BOS", true);
		graph.addEdge("3001", "ATL", "JFK", true);
		graph.addEdge("3002", "ATL", "MIA", true);
		graph.addEdge("3003", "ATL", "MIA", true);

		graph.addEdge("4000", "MIA", "KGN", true);
		graph.addEdge("4001", "MIA", "ATL", true);

		graph.addEdge("5000", "KGN", "BOS", true);

		return graph;

	}

	private void testTimeRoute(Node dep, Node arr) {
		System.out.println("There's a route between " + dep + " and " + arr
				+ ": " + timeHasRoute(dep, arr, 0, null, new ArrayList<Node>() ));
	
	}

	private void testHasRoute(Node dep, Node arr) {
		System.out.println("There's a route between " + dep + " and " + arr
				+ ": " + simpleHasRoute(dep, arr, 0));
	}

	private void printEdge(Edge edge) {
		System.out.println("Is this edge directed? " + edge.isDirected());
		System.out.println("The flight number is: " + edge.getId());
		System.out.println("The flight has : " + edge.getAttributeCount()
				+ " attribute");
		System.out.println("The flight time is : "
				+ edge.getAttribute("fltInfo"));
	}

	private ArrayList<Edge> addTestEdges(Graph graph) {

		/* Holds the list of edges to be returned */
		ArrayList<Edge> edgeList = new ArrayList<Edge>();

		edgeList.clear(); // clear the list, each time make edges

		/* Flight Times */
		// BOS
		Time d1000 = new Time(10, 00);
		Time a1000 = new Time(10, 30);

		Time d1001 = new Time(12, 00);
		Time a1001 = new Time(15, 00);

		// JFK
		Time d2000 = new Time(11, 00);
		Time a2000 = new Time(11, 59);

		Time d2001 = new Time(9, 00);
		Time a2001 = new Time(10, 00);

		// ATL
		Time d3000 = new Time(12, 00);
		Time a3000 = new Time(13, 00);

		Time d3001 = new Time(12, 00);
		Time a3001 = new Time(12, 59);

		Time d3002 = new Time(12, 00);
		Time a3002 = new Time(12, 30);
		
		Time d3003 = new Time(12, 15);
		Time a3003 = new Time(12, 45);
		
		// MIA
		Time d4000 = new Time(13, 00);
		Time a4000 = new Time(14, 00);

		Time d4001 = new Time(9, 00);
		Time a4001 = new Time(9, 30);

		// KGN
		Time d5000 = new Time(9, 00);
		Time a5000 = new Time(9, 59);

		/* Making the Flight Legs */
		FlightLeg f1000 = new FlightLeg(airplane, 1000, 30, d1000, date,
				airports.get(0), a1000, date, airports.get(1), 50.00, 15,
				25.00, 25);

		FlightLeg f1001 = new FlightLeg(airplane, 1001, 180, d1001, date,
				airports.get(0), a1001, date, airports.get(4), 50.00, 15,
				25.00, 25);

		FlightLeg f2000 = new FlightLeg(airplane, 2000, 59, d2000, date,
				airports.get(1), a2000, date, airports.get(2), 50.00, 15,
				25.00, 25);

		FlightLeg f2001 = new FlightLeg(airplane, 2001, 60, d2001, date,
				airports.get(1), a2001, date, airports.get(2), 50.00, 15,
				25.00, 25);

		FlightLeg f3000 = new FlightLeg(airplane, 3000, 60, d3000, date,
				airports.get(2), a3000, date, airports.get(0), 50.00, 15,
				25.00, 25);

		FlightLeg f3001 = new FlightLeg(airplane, 3001, 59, d3001, date,
				airports.get(2), a3001, date, airports.get(1), 50.00, 15,
				25.00, 25);

		FlightLeg f3002 = new FlightLeg(airplane, 3002, 30, d3002, date,
				airports.get(2), a3002, date, airports.get(3), 50.00, 15,
				25.00, 25);

		FlightLeg f3003 = new FlightLeg(airplane, 3003, 30, d3003, date,
				airports.get(2), a3003, date, airports.get(3), 50.00, 15,
				25.00, 25);
		
		FlightLeg f4000 = new FlightLeg(airplane, 4000, 60, d4000, date,
				airports.get(3), a4000, date, airports.get(5), 50.00, 15,
				25.00, 25);

		FlightLeg f4001 = new FlightLeg(airplane, 4001, 30, d4001, date,
				airports.get(3), a4001, date, airports.get(2), 50.00, 15,
				25.00, 25);

		FlightLeg f5000 = new FlightLeg(airplane, 5001, 30, d5000, date,
				airports.get(5), a5000, date, airports.get(0), 50.00, 15,
				25.00, 25);

		/* Edges */
		Edge flight1000 = graph.getEdge("1000");
		flight1000.addAttribute("fltInfo", f1000);
		edgeList.add(flight1000);

		Edge flight1001 = graph.getEdge("1001");
		flight1001.addAttribute("fltInfo", f1001);
		edgeList.add(flight1001);

		Edge flight2000 = graph.getEdge("2000");
		flight2000.addAttribute("fltInfo", f2000);
		edgeList.add(flight2000);

		Edge flight2001 = graph.getEdge("2001");
		flight2001.addAttribute("fltInfo", f2001);
		edgeList.add(flight2001);

		Edge flight3000 = graph.getEdge("3000");
		flight3000.addAttribute("fltInfo", f3000);
		edgeList.add(flight3000);

		Edge flight3001 = graph.getEdge("3001");
		flight3001.addAttribute("fltInfo", f3001);
		edgeList.add(flight3001);

		Edge flight3002 = graph.getEdge("3002");
		flight3002.addAttribute("fltInfo", f3002);
		edgeList.add(flight3002);

		Edge flight3003 = graph.getEdge("3003");
		flight3003.addAttribute("fltInfo", f3003);
		edgeList.add(flight3003);
		
		Edge flight4000 = graph.getEdge("4000");
		flight4000.addAttribute("fltInfo", f4000);
		edgeList.add(flight4000);

		Edge flight4001 = graph.getEdge("4001");
		flight4001.addAttribute("fltInfo", f4001);
		edgeList.add(flight4001);

		Edge flight5000 = graph.getEdge("5000");
		flight5000.addAttribute("fltInfo", f5000);
		edgeList.add(flight5000);

		return edgeList;
	}

	/* Determines if there's a route between two nodes, maximum of 2 connections */
	private boolean simpleHasRoute(Node depNode, Node arrNode, int con) {
	
		Node nextConNode; // holds the next node for the recursion
		boolean found = false; // holds the result
	
		/* If less than 2 connections so far */
		if (con < 3) {
	
			/* Checks if there's a direct Flight from the current node */
			if (depNode.hasEdgeToward(arrNode)) {
	
				found = true;
	
			}
			/* No direct connection from the current node */
			else {
	
				/* Get an iterator for the departing flights */
				Iterator<Edge> depFlights = depNode.getLeavingEdgeIterator();
	
				/* While there are still flights to search */
				while (depFlights.hasNext()) {
					nextConNode = depFlights.next().getTargetNode();
	
					/* Recursive call to search for connections */
					if (simpleHasRoute(nextConNode, arrNode, con + 1)) {
						found = true;
						break; // found a route, stop searching
					}
					;
	
				}
	
			}
	
		}
		/* Exceeded two connections, so return false */
		else {
			found = false;
		}
	
		/* Return the result */
		return found;
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
	
	private ArrayList<LinkedList<Edge>> getRoutes(Node depNode, Node arrNode, ArrayList<Node> visited, Node originDepNode){
		
		/* List to hold all the routes */
		ArrayList<LinkedList<Edge>> routes = new ArrayList<LinkedList<Edge>>();
		
		/* Get a list of all the flights leaving the departure airport */
		Iterator<Edge> depNodeFlights = depNode.getEachLeavingEdge().iterator();
				
		/* While there are flights left to check from 
		 * the departing airport */
		while(depNodeFlights.hasNext()){
			
			/* Get the next flight that leaves this airport */
			Edge flight = depNodeFlights.next();
			
			/* Making sure that original departure you get */
			if (originDepNode.equals(flight.getSourceNode())){
				visited.clear();
				visited.add(originDepNode);
			}
			
			/* If where you would land is not in the places
			 * that were checked before */
			if(!visited.contains(flight.getTargetNode())){
				
				/* If this flight lands at the our final
				 * destination, then we know it is a route */
				if(flight.getTargetNode().equals(arrNode)){
					
					/* Make a linked list to store the route */
					LinkedList<Edge> currentRoute = new LinkedList<Edge>();
					
					/* Add the flight to the current route */
					currentRoute.add(flight);
					
					/* Add the current route to the routes list */
					routes.add(currentRoute);
					
					
				}
				/* There are no direct flights, 
				 * so let's check for connections */
				else{
					
					/* Add this departure node to the visited list */
					visited.add(depNode);
					
					/* Store all the routes from the connection airport */
					ArrayList<LinkedList<Edge>> returnedRoutes = getRoutes(flight.getTargetNode(), arrNode, visited, originDepNode);
					
					for( LinkedList<Edge> route : returnedRoutes){
							
						/* Add the route from the connecting flight to the original route */
						route.addFirst(flight);
						
						/* Make a linked list to store the route */
						LinkedList<Edge> addedRoute = new LinkedList<Edge>();
						
						/* Make the new Linked List */
						addedRoute = route;
						
						/* Add the concatenated route to the routes list */
						routes.add(addedRoute);
						
					
					}
					
				}
				
			}
		
		}
		
		return routes;
		
	}
}
