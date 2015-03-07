/** Engineer: Richard Walker
 * Date: March 5. 2015
 * Description: Creates a DOM that parses an Airplane XML file
 * 				given by Professor Blake Nelson for the CS 509 Project.
 * 				Currently the Class just prints the information to the 
 * 				console, but it will be modified to store the information in 
 * 				a list of airplane objects.
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

import flight_system.Airplane;

public class AirplaneParser {
	
	/* List to hold the airplanes parsed from the XML */
	private ArrayList<Airplane> airplaneList; 
	
	/**
	 * @param the constructor creates an empty 
	 *        list that is used to hold a the parsed airplanes
	 */
	public AirplaneParser() {
		this.airplaneList = new ArrayList<Airplane>();
	}
	
	/* Returns a list of the parsed airplanes */
	public ArrayList<Airplane> getAirplaneList() {
		return airplaneList;
	}
	
	/* Returns the number of airplanes in the list */
	public int getNumOfAirplanes() {
		return this.airplaneList.size();
	}
	
	/* Return the airplane from the list that corresponds with the model number */
	public Airplane getAirplane(String model){
		
		boolean notFound = true;  // used determine if a model was found 
		Airplane airplane = null; // holds the airplane that matches the model
		
		/* Iterator object for the airplane list */
		ListIterator<Airplane> airplaneIterator = airplaneList.listIterator();
		
		/* Search this list until the airplane with the model is found */
		do {

			try {
				airplane = airplaneIterator.next(); // get the next Airplane in the list
				/* If the model matches, set the notFound flag to false */
				if (airplane.getModel().equalsIgnoreCase(model)) {
					notFound = false; 
				}
			} catch (Exception e) {
				/* No such element (Airplane) found */
				airplane = null;
				notFound = true; 
			}
			
	
		} while (notFound); // end while loop

		return airplane; // Return the airplane that matches the model
						 // Will return null if there's no match
		
	}
	
	/* Prints the airplane list */
	public void printAirplaneList(){
		System.out.println("Printing the Airplane XML data:");
		/* Print each Airplanes in the list */
		for (int i = 0; i < airplaneList.size(); i++) {
			System.out.println(this.airplaneList.get(i).toString());
		}

	}

	/* Static Method used to parse the airplane XML */
	public void parseAirplaneXML() {

		/* DOM Factory Builder */
		DocumentBuilderFactory dom_fac = DocumentBuilderFactory.newInstance();

		try {
			/* Builds the doc object that contains the 
			 * tree structure of the XML file */
			DocumentBuilder builder = dom_fac.newDocumentBuilder();
			Document doc = builder.parse("airplanes.xml"); // This is the root node

			/* Contains a list of all the airplanes from the Airplanes XML */
			NodeList airplaneNodeList = doc.getElementsByTagName("Airplane");

			/* Iterate through all the airplanes in the airplane list */
			for (int i = 0; i < airplaneNodeList.getLength(); i++) {

				Node airplaneNode = airplaneNodeList.item(i); // Airplane Node

				/* I know this node is an element, so can cast it as such */
				Element airplane = (Element) airplaneNode;

				/* Get the Airplane Manufacturer and Model */
				String model = airplane.getAttribute("Model");
				String manufacturer = airplane.getAttribute("Manufacturer");
				
				/* Contains the 1st Class & Coach seat nodes */
				NodeList airplaneNodeChildren = airplaneNode.getChildNodes();

				/* 2nd child is the First Class Seat Node */
				String firstClassSeats = airplaneNodeChildren.item(1).getTextContent();
				
				/* 4th child is the Coach Seat Node */
				String coachSeats = airplaneNodeChildren.item(3).getTextContent();
				
				/* Adds the parsed airplane to the airplane list */
				airplaneList.add(new Airplane(model, 
						manufacturer, 
						Integer.parseInt(firstClassSeats), 
						Integer.parseInt(coachSeats)
						));
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
		return "AirplaneParser extracted" + getNumOfAirplanes() + "airplanes from the XML";
	}

	

}

