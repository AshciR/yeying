package graph;

import java.util.Iterator;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class GraphTester {

	private GraphTester(){};
	
	public static void testGraph(){
		
		Graph graph = makeGraph();
		
		/* Nodes */
		Node bos = graph.getNode("BOS");
		Node jfk = graph.getNode("JFK");
		Node atl = graph.getNode("ATL");
		Node mia = graph.getNode("MIA");
		Node sfo = graph.getNode("SFO");
		Node kgn = graph.getNode("KGN");
		
		/* Edges */
		Edge flight1000 = graph.getEdge("1000");
		flight1000.addAttribute("depTime", 12);
		
		Edge flight2000 = graph.getEdge("2000");
		flight2000.addAttribute("depTime", 13);
		
		Edge flight2001 = graph.getEdge("2001");
		flight2001.addAttribute("depTime", 14);
		
		Edge flight2002 = graph.getEdge("3000");
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
		
		System.out.println("BOS -> JFK? : " + bos.hasEdgeToward(jfk)); // BOS -> JFK Yes!
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

		graph.addEdge("3001", "ATL", "JFK", true);
		graph.addEdge("3000", "ATL", "BOS", true);
		graph.addEdge("3002", "ATL", "MIA", true);

		graph.addEdge("4000", "MIA", "KGN", true);
		
		return graph;

	}

}


