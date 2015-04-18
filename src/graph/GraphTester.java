package graph;

import java.util.ArrayList;
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
	}
	
	/* Test the small example graph */
	public void testExampleGraph(){
		ExampleGraph test = new ExampleGraph();
		test.testGraph();
	}
 		
}


