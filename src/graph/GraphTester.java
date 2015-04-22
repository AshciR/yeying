package graph;

import java.util.ArrayList;
import java.util.LinkedList;

import org.graphstream.graph.Edge;

import flight_system.*;

public class GraphTester {
	
	/* Constructor */
	public GraphTester(){
		
	};
	
	/* Test GraphMaker With Actual Data */
	public void testGraphMaker(){
		GraphMaker test = new GraphMaker(new Date(Month.May, 10, 2015));
		System.out.println("This graph has " + test.getGraph().getEdgeCount() + " edges.");
		System.out.println("This graph has " + test.getGraph().getNodeCount() + " nodes.");
		test.displayGraph();
	}
	
	/* Test the Graph Engine with actual data */
	public void testGraphEngine(){
		
		ArrayList<Airport> airports = ExampleGraph.makeTestAiports(); 
		
		GraphMaker maker = new GraphMaker(new Date(Month.May, 10, 2015));
		IFlightGraph engine = new GraphEngine(maker.getGraph());
				
		System.out.println("Node for the 1st Airport is: " + ((GraphEngine) engine).getNode(airports.get(0)));
		
		/* Flight from BOS -> SFO */
		System.out.println("There is a flight from BOS to SFO: " + engine.hasDirectFlight(airports.get(0), airports.get(4))); 
		
		/* Flight from BOS -> ATL */
		System.out.println("There is a flight between BOS to ATL: " + engine.hasRoute(airports.get(0), airports.get(2)));
	
		/* Flight from BOS -> ATL -> JFK */
		System.out.println("There is a flight between BOS to JFK: " + engine.hasRoute(airports.get(0), airports.get(1)));
		
		/* Flight from BOS -> MIA */
		System.out.println("There is a flight between BOS to MIA: " + engine.hasRoute(airports.get(0), airports.get(3)));
		
		/* Get all the flights from BOS to SFO with maximum of 3 flights, regardless of direction */
		ArrayList<LinkedList<Edge>> flights = engine.getRoutes(airports.get(2), airports.get(0), 2, false);
		
		/* Get all the flights from BOS to SFO with maximum of 3 flights, regardless of direction */
		ArrayList<LinkedList<Edge>> flightsDir = engine.getRoutes(airports.get(2), airports.get(0), 2, true);
		
		System.out.println("\nThere are " + flights.size() + " flights.");
		
		System.out.println("--- Here are the flights disregarding the direction: --- ");
		/* Print the flights */
		for (LinkedList<Edge> flight : flights){
			System.out.println(flight); 
		}
		
		System.out.println("\nThere are " + flightsDir.size() + " flights in a general direction.");
		
		System.out.println("\n--- Here are the flights with a general direction: --- ");
		/* Print the flights */
		for (LinkedList<Edge> flight : flightsDir){
			System.out.println(flight); 
		}
		
	}
	
	/* Test the small example graph */
	public void testExampleGraph(){
		ExampleGraph test = new ExampleGraph();
		test.testGraph();
	}
 		
}


