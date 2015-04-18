package graph;

import org.graphstream.graph.*;

import flight_system.Airport;

public class GraphEngine {
	
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
	
}
