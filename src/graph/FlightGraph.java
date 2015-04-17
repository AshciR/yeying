package graph;

import java.util.Iterator;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import parsers.AirportParser;
import parsers.FlightParser;
import parsers.XMLGetter;
import flight_system.*;

public class FlightGraph {
	
	private Date date;
	private Graph graph;
	
	public FlightGraph(Date date) {
		this.date = date;
		
		/* Graph Identifier */
		String graphName = "" +date.getMonth() + "_" + date.getDay() + "_" + date.getYear();
		
		/* Create Graph */
		this.graph = new MultiGraph(graphName);

		/* Allow easy creation of edges */
		graph.setAutoCreate(true);
		
		makeGraph();
		
	}
	
	
	/* Getters */
	public Date getDate() {
		return date;
	}

	public Graph getGraph() {
		return graph;
	}
	
	/* Show the graph */
	public void displayGraph(){
		this.graph.display();
	}

	private void makeGraph(){
		
		/* XML Getter Singleton */
		XMLGetter getter = XMLGetter.getInstance();
		
		/* Get the Airport Parser object */
		AirportParser portParser = AirportParser.getInstance();
		
		/* Adds the nodes to the graph */
		addNodes(portParser, getter);
		
		/* Adds the edges to the graph */
		addEdges(getter, portParser);
			
	}

	/* Adds Edges to the graph */
	private void addEdges(XMLGetter getter, AirportParser portParser) {
		/* Flight Parser object used to store the departing flights */
		FlightParser fParser = new FlightParser(getter.getAirplaneXML(), getter.getAirportsXML());
		
		/* Iterator for nodes */
		Iterator<Node> nodeIterator = this.graph.iterator();
		
		/* Get all the departing flights from every airport */
		while (nodeIterator.hasNext()){
			
			String airportCode = nodeIterator.next().getId();
			
			Airport airport = portParser.getAirport(airportCode);
			
			/* Get the flights */
			fParser.parseFlightXML(getter.getFlightsXML(true, airport, this.date));
			
			/* Add all the edges (departing flights) */
			for (FlightLeg flight : fParser.getFlightList()){
				graph.addEdge(Integer.toString(flight.getFlightNum()), airportCode, flight.getArrivalAirport().getCode(), true);
			}
			
			/* Clean the list */
			fParser.clearFlightList();
			
		}
	}

	
	/* Adds the Airport Nodes to the graph */
	private void addNodes(AirportParser portParser, XMLGetter getter) {
		
		/* If it doesn't have the airport list already, get the data from the server */
		if (!portParser.hasAirportList()){
			portParser.parseAirportXML(getter.getAirportsXML());
		}
		
		/* Add all the airport nodes to the graph */
		for(Airport airport : portParser.getAirportList() ){
			
			/* Add the Airport Node */
			this.graph.addNode(airport.getCode());
			
		}
		
	}
	
}
