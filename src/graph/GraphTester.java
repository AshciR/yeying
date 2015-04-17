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
	
	public GraphTester(){
		this.airplane = new Airplane("774", "Airbus", 20, 50);
		this.date = new Date(Month.March, 10, 2015);
		this.airports = new ArrayList<Airport>();
		makeTestAiports();
	};
	
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
		
		Time d1001 = new Time(14, 00);
		Time a1001 = new Time(17, 00);
		
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
		
		return edgeList;
	}
	
	/* Test Graph With Actual Data */
	public void testFlightGraph(){
		FlightGraph test = new FlightGraph(date);
		test.displayGraph();
	}
	
	
	/* Test small made-up graph */
	public void testGraph(){
		
		Graph graph = makeGraph();
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		
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
		System.out.println("--------");
		System.out.println("There's a route between " + bos + " and " + atl + ": " + hasRoute(bos,atl,0)+"\n"); 
		System.out.println("There's a route between " + atl + " and " + mia + ": " + hasRoute(atl,mia,0)+"\n");
		System.out.println("There's a route between " + bos + " and " + mia + ": " + hasRoute(bos,mia,0)+"\n");		
		System.out.println("There's a route between " + bos + " and " + kgn + ": " + hasRoute(bos,kgn,0)+"\n");
		System.out.println("There's a route between " + sfo + " and " + bos + ": " + hasRoute(sfo,bos,0)+"\n");
		System.out.println("There's a route between " + jfk + " and " + kgn + ": " + hasRoute(jfk,kgn,0)+"\n");
		System.out.println("There's a route between " + kgn + " and " + sfo + ": " + hasRoute(kgn,sfo,0)+"\n");

	}

	private static void printEdge(Edge edge) {
		System.out.println("Is this edge directed? " + edge.isDirected());
		System.out.println("The flight number is: " + edge.getId());
		System.out.println("The flight has : " + edge.getAttributeCount() + " attribute");
		System.out.println("The flight time is : " + edge.getAttribute("fltInfo"));
	}
	
	/* Determines if there's a route between two nodes, maximum of 2 connections  */
	private static boolean hasRoute(Node depNode, Node arrNode, int con){
		
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
					if (hasRoute(nextConNode, arrNode, con + 1)){
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
	
	/* Makes the graph for the small made-up graph */
	private static Graph makeGraph(){
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
			
		return graph;

	}

}


