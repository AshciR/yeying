package graph;

import java.util.ArrayList;
import java.util.Iterator;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import flight_system.Airplane;
import flight_system.Airport;
import flight_system.Date;
import flight_system.FlightLeg;
import flight_system.Location;
import flight_system.Month;
import flight_system.Time;

public class GraphTester {
	
	private Airplane airplane;
	private Date date;
	private ArrayList<Airport> airports;
	
	/* Constructor */
	public GraphTester(){
		this.airplane = new Airplane("774", "Airbus", 20, 50);
		this.date = new Date(Month.May, 10, 2015);
		this.airports = new ArrayList<Airport>();
		makeTestAiports();
	};
	
	/* Test GraphMaker With Actual Data */
	public void testGraphMaker(){
		GraphMaker test = new GraphMaker(date);
		System.out.println("This graph has " + test.getGraph().getEdgeCount() + " edges.");
		System.out.println("This graph has " + test.getGraph().getNodeCount() + " nodes.");
	}
	
	/* Test the Graph Engine with actual data */
	public void testGraphEngine(){
		
		GraphMaker maker = new GraphMaker(date);
		IFlightGraph engine = new GraphEngine(maker.getGraph());
				
		System.out.println("Node for the 1st Airport is: " + ((GraphEngine) engine).getNode(this.airports.get(0)));
		
		/* Flight from BOS -> SFO */
		System.out.println("There is a flight from BOS to SFO: " + engine.hasDirectFlight(airports.get(0), airports.get(4))); 
		
		/* Flight from BOS -> ATL */
		System.out.println("There is a flight between BOS to ATL: " + engine.hasRoute(airports.get(0), airports.get(2)));
	
		/* Flight from BOS -> ATL -> JFK */
		System.out.println("There is a flight between BOS to JFK: " + engine.hasRoute(airports.get(0), airports.get(1)));
		
		/* Flight from BOS -> MIA */
		System.out.println("There is a flight between BOS to MIA: " + engine.hasRoute(airports.get(0), airports.get(3)));
	}
	
	/* Test small made-up graph */
 	public void testGraph(){
		
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		
		/* Make the graph */
		Graph graph = makeGraph();
		
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
		
		/* Testing hasEdgeToward() */
		System.out.println("\nBOS -> JFK? : " + bos.hasEdgeToward(jfk)); // BOS -> JFK Yes!
		System.out.println("BOS -> ATL? : " + bos.hasEdgeToward(atl)); // BOS -> ATL No!
				
		/* Testing hasRoute */
		System.out.println("Testing hasRoute--------");
		testHasRoute(bos, atl); 
		testHasRoute(atl, mia);
		testHasRoute(bos, mia);		
		
		/* Testing timeHasRoute */
		System.out.println("Testing timeHasRoute--------");
		testTimeRoute(bos, jfk); // True - Direct Flight
		testTimeRoute(bos, atl); // True - 1 connection	
		testTimeRoute(bos, mia); // True - 2 connections 
		testTimeRoute(atl, sfo); // False - 1 connection, but leaves too early
		testTimeRoute(bos, kgn); // False - 3 connections
		testTimeRoute(mia, sfo); // False - MIA -> ATL -> BOS -/> SFO
		testTimeRoute(kgn, mia); // False - KGN -> BOS -> JFK -> ATL -> MIA too many stops
		testTimeRoute(kgn, atl); // True - KGN -> BOS -> JFK -> ATL
	}

	private void testTimeRoute(Node dep, Node arr) {
		System.out.println("There's a route between " + dep + " and " + arr + ": " + timeHasRoute(dep,arr,0,null)+"\n");
		
	}

	private void testHasRoute(Node dep, Node arr) {
		System.out.println("There's a route between " + dep + " and " + arr + ": " + simpleHasRoute(dep,arr,0)+"\n");
	}

	/* Make the test airports */
	private void makeTestAiports(){
		
		/* Clean the list before you make it */
		this.airports.clear();
		
		/* Using just one location for all the airports */
		Location loc = new Location(33.641045, -84.427764, false);
		
		Airport bosPort = new Airport("BOS", "Boston Logan", loc);
		this.airports.add(bosPort);
		
		Airport jfkPort = new Airport("JFK", "John F. Kennedy", loc);
		this.airports.add(jfkPort);
		
		Airport atlPort = new Airport("ATL", "Hartsfield Jackson", loc);
		this.airports.add(atlPort);
		
		Airport miaPort = new Airport("MIA", "Miami", loc);
		this.airports.add(miaPort);
		
		Airport sfoPort = new Airport("SFO", "San Francisco", loc);
		this.airports.add(sfoPort);
		
		Airport kgnPort = new Airport("KGN", "Norman Manley", loc);
		this.airports.add(kgnPort);	
		
	}

	private ArrayList<Edge> addTestEdges(Graph graph){
		
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
				airports.get(0), a1000, date, airports.get(1), 50.00, 15, 25.00, 25);
		
		FlightLeg f1001 = new FlightLeg(airplane, 1001, 180, d1001, date,
				airports.get(0), a1001, date, airports.get(4), 50.00, 15, 25.00, 25);
		
		FlightLeg f2000 = new FlightLeg(airplane, 2000, 59, d2000, date,
				airports.get(1), a2000, date, airports.get(2), 50.00, 15, 25.00, 25);
		
		FlightLeg f2001 = new FlightLeg(airplane, 2001, 60, d2001, date,
				airports.get(1), a2001, date, airports.get(2), 50.00, 15, 25.00, 25);
		
		FlightLeg f3000 = new FlightLeg(airplane, 3000, 60, d3000, date,
				airports.get(2), a3000, date, airports.get(0), 50.00, 15, 25.00, 25);
		
		FlightLeg f3001 = new FlightLeg(airplane, 3001, 59, d3001, date,
				airports.get(2), a3001, date, airports.get(1), 50.00, 15, 25.00, 25);
		
		FlightLeg f3002 = new FlightLeg(airplane, 3002, 30, d3002, date,
				airports.get(2), a3002, date, airports.get(3), 50.00, 15, 25.00, 25);
		
		FlightLeg f4000 = new FlightLeg(airplane, 4000, 60, d4000, date,
				airports.get(3), a4000, date, airports.get(5), 50.00, 15, 25.00, 25);
		
		FlightLeg f4001 = new FlightLeg(airplane, 4001, 30, d4001, date,
				airports.get(3), a4001, date, airports.get(2), 50.00, 15, 25.00, 25);
		
		FlightLeg f5000 = new FlightLeg(airplane, 5001, 30, d5000, date,
				airports.get(5), a5000, date, airports.get(0), 50.00, 15, 25.00, 25);
		
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

	private void printEdge(Edge edge) {
		System.out.println("Is this edge directed? " + edge.isDirected());
		System.out.println("The flight number is: " + edge.getId());
		System.out.println("The flight has : " + edge.getAttributeCount() + " attribute");
		System.out.println("The flight time is : " + edge.getAttribute("fltInfo"));
	}
	
	/* Makes the graph for the small made-up graph */
	private Graph makeGraph(){
		Graph graph = new MultiGraph("Test Flights");

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

		graph.addEdge("4000", "MIA", "KGN", true);
		graph.addEdge("4001", "MIA", "ATL", true);
		
		graph.addEdge("5000", "KGN", "BOS", true);
			
		return graph;

	}

	/* Determines if there's a route between two nodes, maximum of 2 connections  */
	private boolean simpleHasRoute(Node depNode, Node arrNode, int con){
		
		Node nextConNode; // holds the next node for the recursion
		boolean found = false; // holds the result
		
		/* If less than 2 connections so far */
		if (con < 3){
			
			/* Checks if there's a direct Flight from the current node */
			if (depNode.hasEdgeToward(arrNode)) {
	
				found = true;
	
			}
			/* No direct connection from the current node */
			else{
				
				/* Get an iterator for the departing flights */
				Iterator<Edge> depFlights = depNode.getLeavingEdgeIterator();
				
				/* While there are still flights to search */
				while (depFlights.hasNext()){
					nextConNode = depFlights.next().getTargetNode();
					
					/* Recursive call to search for connections */
					if (simpleHasRoute(nextConNode, arrNode, con + 1)){
						found = true;
						break; // found a route, stop searching
					};
					
				}
				
			}
			
				
		}
		/* Exceeded two connections, so return false */ 
		else{
			found = false;
		}
		
		/* Return the result */
		return found;
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


