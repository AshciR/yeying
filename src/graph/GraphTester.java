package graph;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class GraphTester {

	private GraphTester(){};
	
	public static void testGraph(){
		Graph graph = new SingleGraph("Tutorial 1");
		
		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addNode("D");
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");
		graph.addEdge("AD", "A", "D");

		graph.display();

	}
	
	
}
