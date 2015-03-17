/** Engineer: Richard Walker
 * Date: March 17. 2015
 * Description: Creates a DOM that parses a Flight XML file
 * 				given by Professor Blake Nelson for the CS 509 Project.
 * 				The class stores the information in 
 * 				a list of Flight objects.
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

public class FlightParser {
	
	/* List to hold the flights parsed from the XML */
	// TODO private ArrayList<Airport> airportList; 
	
	/**
	 * @param the constructor creates an empty 
	 *        list that is used to hold a the parsed airplanes
	 */
	public FlightParser() {
		
		// TODO this.airportList = new ArrayList<Airport>();
	}
	
	// TODO
	/* Returns a list of the parsed airplanes */
//	public ArrayList<Airport> getFlightList() {
//		return airportList;
//	}
	
	// TODO
//	/* Returns the number of airplanes in the list */
//	public int getNumOfAirports() {
//		return this.airportList.size();
//	}
	
	// TODO
//	/* Return the airport from the list that corresponds with the code */
//	public Airport getAirport(String code){
//
//		boolean notFound = true;  // used determine if an airport was found 
//		Airport airport = null; // holds the airport that matches the code
//
//		/* Iterator object for the airplane list */
//		ListIterator<Airport> airportIterator = airportList.listIterator();
//
//		/* Search this list until the airplane with the model is found */
//		do {
//
//			try {
//				airport = airportIterator.next(); // get the next Airport in the list
//				/* If the model matches, set the notFound flag to false */
//				if (airport.getCode().equalsIgnoreCase(code)) {
//					notFound = false; 
//				}
//			} catch (Exception e) {
//				/* No such element (Airplane) found */
//				airport = null;
//				notFound = true; 
//			}
//
//
//		} while (notFound); // end while loop
//
//		return airport; // Return the airport that matches the code
//						// Will return null if there's no match
//
//	}
	
	// TODO
//	/* Prints the airplane list */
//	public void printAirportList(){
//		System.out.println("Printing the Airport XML data:");
//		/* Print each Airplanes in the list */
//		for (int i = 0; i < airportList.size(); i++) {
//			System.out.println(this.airportList.get(i).toString());
//		}
//
//	}

	/* Static Method used to parse the airplane XML */
	public void parseFlightXML() {

		/* DOM Factory Builder */
		DocumentBuilderFactory dom_fac = DocumentBuilderFactory.newInstance();

		try {
			/* Builds the doc object that contains the 
			 * tree structure of the XML file */
			DocumentBuilder builder = dom_fac.newDocumentBuilder();
			
			/* This is the root node */
			Document doc = builder.parse("flights_d_bos_2015_05_10.xml"); 

			/* Contains a list of all the flights from the Flight XML */
			NodeList flightNodeList = doc.getElementsByTagName("Flight");

			/* Iterate through all the flights in the airplane list */
			for (int i = 0; i < flightNodeList.getLength(); i++) {

				Node flightNode = flightNodeList.item(i); // Flight Node

				/* I know this node is an element, so can cast it as such */
				Element flight = (Element) flightNode;
				
				/* Get the Flight's Airplane, FLight Time, and Flight Number */
				String airplaneModel = flight.getAttribute("Airplane");
				String flightTime = flight.getAttribute("FlightTime");
				String flightNum = flight.getAttribute("Number");
				
				/* Contains the Flight Node children */
				NodeList flightNodeChildren = flightNode.getChildNodes();
				
				/* -- Departure Data -- */
				
				/* Get the children of the departure node */
				NodeList deptNodeChild = flightNodeChildren.item(1).getChildNodes();
				
				/* 2nd child is the Departure Airport Text Node */
				String depAirPortCode = deptNodeChild.item(1).getTextContent();
				
				/* 4th child is the Departure Time Text Node */
				String depTime = deptNodeChild.item(3).getTextContent();
				
				/* -------------------- */
				
				/* -- Arrival Data -- */
				
				/* Get the children of the arrival node */
				NodeList arrNodeChild = flightNodeChildren.item(3).getChildNodes();

				/* 2nd child is the Departure Airport Text Node */
				String arrAirPortCode = arrNodeChild.item(1).getTextContent();

				/* 4th child is the Departure Time Text Node */
				String arrTime = arrNodeChild.item(3).getTextContent();

				/* ------------------ */ 
	
				/* -- Seating Data -- */
			
				/* Get the children of the seating node */
				NodeList seatNodeChild = flightNodeChildren.item(5).getChildNodes();
				
				/* 2nd child is the First Class Node, which is an Element */
				Element firstClassSeat = (Element) seatNodeChild.item(1);
				
				/* Get the Flight's First Class Seat Price */
				String firstPrice = firstClassSeat.getAttribute("Price");
				
				/* Get the number of seats available in First Class */
				String firstClassSeats = seatNodeChild.item(1).getTextContent();
				
				/* 4th child is the Coach Class Node, which is an Element */
				Element coachClassSeat = (Element) seatNodeChild.item(3);
				
				/* Get the Flight's Coach Class Seat Price */
				String coachPrice = coachClassSeat.getAttribute("Price");
				
				/* Get the number of seats available in First Class */
				String coachClassSeats = seatNodeChild.item(3).getTextContent();
				
				/* ------------------- */
				
				/* Was used to test the method */
				printFlights(airplaneModel, flightTime, flightNum, depAirPortCode, 
							 depTime, arrAirPortCode, arrTime, firstPrice, 
							 firstClassSeats, coachPrice, coachClassSeats);
				
				// TODO -- Make the Flight Object, when the Flight Class is done
//				/* Adds the parsed airplane to the airplane list */
//				airportList.add(new Airport(code, name, location));
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

	private void printFlights(String airplaneModel, String flightTime,
			String flightNum, String depAirPortCode, String depTime,
			String arrAirPortCode, String arrTime, String firstPrice,
			String firstClassSeats, String coachPrice, String coachClassSeats) {
		
		System.out.println("FLight Plane: " + airplaneModel);
		System.out.println("FLight Time: " + flightTime);
		System.out.println("FLight Number: " + flightNum);
		System.out.println();

		System.out.println("Departure Info");
		System.out.println("Dept. Airport: " + depAirPortCode);
		System.out.println("Dept. Time: " + depTime);
		System.out.println();

		System.out.println("Arrival Info:");
		System.out.println("Arrival Airport: " + arrAirPortCode);
		System.out.println("Dept. Time: " + arrTime);
		System.out.println();

		System.out.println("Seating Info:");
		System.out.println("First Class Price: " + firstPrice);
		System.out.println("First Class Seats: " + firstClassSeats);
		System.out.println("Coach Class Price: " + coachPrice);
		System.out.println("Coach Class Seats: " + coachClassSeats);
		System.out.println();
		System.out.println("-------------------");
	}

	// TODO -- Re-implement when ready.
//	@Override
//	public String toString() {
//		return "AirportParser extracted" + this.getNumOfAirports() + "airports from the XML";
//	}

	

}

