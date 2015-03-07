/** Engineer: Richard Walker
 * Date: March 5. 2015
 * Description: Creates a DOM that parses an Airport XML file
 * 				given by Professor Blake Nelson for the CS 509 Project.
 * 				The class stores the information in 
 * 				a list of airport objects.
 */

package parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import flight_system.*;

public class AirportParser {
	
	/* List to hold the airplanes parsed from the XML */
	private ArrayList<Airport> airportList; 
	
	/**
	 * @param the constructor creates an empty 
	 *        list that is used to hold a the parsed airplanes
	 */
	public AirportParser() {
		this.airportList = new ArrayList<Airport>();
	}
	
	/* Returns a list of the parsed airplanes */
	public ArrayList<Airport> getAirportList() {
		return airportList;
	}
	
	/* Returns the number of airplanes in the list */
	public int getNumOfAirports() {
		return this.airportList.size();
	}

	/* Return the airport from the list that corresponds with the code */
	public Airport getAirport(String code){

		boolean notFound = true;  // used determine if an airport was found 
		Airport airport = null; // holds the airport that matches the code

		/* Iterator object for the airplane list */
		ListIterator<Airport> airportIterator = airportList.listIterator();

		/* Search this list until the airplane with the model is found */
		do {

			try {
				airport = airportIterator.next(); // get the next Airport in the list
				/* If the model matches, set the notFound flag to false */
				if (airport.getCode().equalsIgnoreCase(code)) {
					notFound = false; 
				}
			} catch (Exception e) {
				/* No such element (Airplane) found */
				airport = null;
				notFound = true; 
			}


		} while (notFound); // end while loop

		return airport; // Return the airport that matches the code
						// Will return null if there's no match

	}
	
	
	/* Prints the airplane list */
	public void printAirportList(){
		System.out.println("Printing the Airport XML data:");
		/* Print each Airplanes in the list */
		for (int i = 0; i < airportList.size(); i++) {
			System.out.println(this.airportList.get(i).toString());
		}

	}

	/* Static Method used to parse the airplane XML */
	public void parseAirportXML() {

		/* DOM Factory Builder */
		DocumentBuilderFactory dom_fac = DocumentBuilderFactory.newInstance();

		try {
			/* Builds the doc object that contains the 
			 * tree structure of the XML file */
			DocumentBuilder builder = dom_fac.newDocumentBuilder();
			Document doc = builder.parse("airports.xml"); // This is the root node

			/* Contains a list of all the airplanes from the Airplanes XML */
			NodeList airportNodeList = doc.getElementsByTagName("Airport");

			/* Iterate through all the airplanes in the airplane list */
			for (int i = 0; i < airportNodeList.getLength(); i++) {

				Node airportNode = airportNodeList.item(i); // Airport Node

				/* I know this node is an element, so can cast it as such */
				Element airport = (Element) airportNode;

				/* Get the Airplane Manufacturer and Model */
				String code = airport.getAttribute("Code");
				String name = airport.getAttribute("Name");
				
				/* Contains the 1st Class & Coach seat nodes */
				NodeList airportNodeChildren = airportNode.getChildNodes();

				/* 2nd child is the Latitude Node */
				String latitude = airportNodeChildren.item(1).getTextContent();
				
				/* 4th child is the Longitude Node */
				String longitude = airportNodeChildren.item(3).getTextContent();
				
				Location location = new Location(Double.parseDouble(latitude),
												 Double.parseDouble(longitude));
				
				/* Adds the parsed airplane to the airplane list */
				airportList.add(new Airport(code, name, location));
			}

		/* Exceptions required by the Parser */	

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	@Override
	public String toString() {
		return "AirportParser extracted" + this.getNumOfAirports() + "airports from the XML";
	}

	

}

