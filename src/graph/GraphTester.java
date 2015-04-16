package graph;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class GraphTester {

	private GraphTester(){};
	
	public static void testGraph(){
		
		Graph graph = new MultiGraph("Test Flights");
		
		/* Allow easy creation of edges */
		graph.setAutoCreate(true);
		
		/* Makes Graph */
		graph.addNode("BOS");
		graph.addNode("JFK");
		graph.addNode("ATL");
		graph.addNode("MIA");
		
		graph.addEdge("1000", "BOS", "JFK", true);
		graph.addEdge("2000", "JFK", "ATL", true);
		graph.addEdge("2001", "ATL", "JFK", true);
		graph.addEdge("2002", "JFK", "ATL", true);
		graph.addEdge("3000", "ATL", "BOS", true);
		graph.addEdge("4000", "ATL", "MIA", true);
		
		/* Nodes */
		Node bos = graph.getNode("BOS");
		Node jfk = graph.getNode("JFK");
		Node atl = graph.getNode("ATL");
		Node mia = graph.getNode("MIA");
		
		/* Edges */
		Edge flight1000 = graph.getEdge("1000");
		flight1000.addAttribute("depTime", "12:00");
		
		Edge flight2000 = graph.getEdge("2000");
		flight2000.addAttribute("depTime", "13:00");
		
		Edge flight2001 = graph.getEdge("2001");
		flight2001.addAttribute("depTime", "14:00");
		
		Edge flight2002 = graph.getEdge("2002");
		flight2002.addAttribute("depTime", "15:00");
		
		/* Verifying Graph Data */
		System.out.println("The flight number is: " + flight1000.getId());
		System.out.println("The flight has : " + flight1000.getAttributeCount() + " attribute");
		System.out.println("The flight time is : " + flight1000.getAttribute("depTime"));
		System.out.println("Is this edge directed? " + flight1000.isDirected());
		
		System.out.println("The flight number is: " + flight2000.getId());
		System.out.println("The flight has : " + flight2000.getAttributeCount() + " attribute");
		System.out.println("The flight time is : " + flight2000.getAttribute("depTime"));
		System.out.println("Is this edge directed? " + flight2000.isDirected());
		
		System.out.println("The flight number is: " + flight2001.getId());
		System.out.println("The flight has : " + flight2001.getAttributeCount() + " attribute");
		System.out.println("The flight time is : " + flight2001.getAttribute("depTime"));
		System.out.println("Is this edge directed? " + flight2001.isDirected());
		
		System.out.println("The flight number is: " + flight2002.getId());
		System.out.println("The flight has : " + flight2002.getAttributeCount() + " attribute");
		System.out.println("The flight time is : " + flight2002.getAttribute("depTime"));
		System.out.println("Is this edge directed? " + flight2002.isDirected());
		
		System.out.println("BOS -> ATL? : " + bos.hasEdgeFrom(jfk)); // BOS -> ATL No!
		System.out.println("BOS -> JFK? : " + bos.hasEdgeFrom(atl)); // BOS -> JFK Yes!
		
		System.out.println("Theres a route between " + bos + " and " + atl + ": " + hasRoute(bos,atl)); 
		
		graph.display();

	}
	
	/* Check if there's a route */
	private static boolean hasRoute(Node depNode, Node arrNode){
		
		System.out.println("Arrive same as depart " + depNode.equals(arrNode));
		return true;
		
	}
	
}
