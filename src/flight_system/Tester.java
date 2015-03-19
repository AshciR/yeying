package flight_system;

import java.io.IOException;

import parsers.*;

public class Tester {

	public static void main(String[] args) {
		
	}
	
	@SuppressWarnings("unused")
	private static void testLocationClass(){
		
		System.out.println("\nLocation Tester");
		
		/* Make a test location */
		Location testLoc = new Location(33.641045,-84.427764);
		
		/* Print the object */
		System.out.println(testLoc.toString());
		System.out.println(testLoc.getLatitude());
		System.out.println(testLoc.getLongitude());
	}
	
	@SuppressWarnings("unused")
	private static void testAirportParserClass() {

		System.out.println("\nAirplaneParser Tester");

		/* Make an Airplane Parser */
		AirportParser aParse = AirportParser.getInstance();
		aParse.parseAirportXML();  // Parses the XML

		/* Print the number of Airplanes */
		System.out.println("There are " + aParse.getNumOfAirports() + " airports.\n" );	

		aParse.printAirportList(); // Prints the list of airports

		System.out.println();
		System.out.println("Looking for Airport with code 'BOS'");
		System.out.println("Found " + aParse.getAirport("BOS").toString());

		System.out.println();
		System.out.println("Looking for Airport with code 'SFO'");
		System.out.println("Found " + aParse.getAirport("SFO").toString());
		
		System.out.println();
		System.out.println("Looking for Airport with code 'ATL'");
		System.out.println("Found " + aParse.getAirport("ATL").toString());
		
		System.out.println();
		System.out.println("Looking for Airport with code 'LAX'");
		System.out.println("Found " + aParse.getAirport("LAX").toString());

		System.out.println();
		System.out.println("Looking for Airport with code 'XYZ'");
		System.out.println("Found " + aParse.getAirport("XYZ").toString());

	}
	
	@SuppressWarnings("unused")
	private static void testAirplaneParserClass() {
		
		System.out.println("\nAirplaneParser Tester");
		
		/* Make an Airplane Parser */
		AirplaneParser aParse = AirplaneParser.getInstance();
		aParse.parseAirplaneXML();  // Parses the XML
		
		/* Print the number of Airplanes */
		System.out.println("There are " + aParse.getNumOfAirplanes() + " airplanes.\n" );	
		
		aParse.printAirplaneList(); // Prints the list of airplanes
		
		System.out.println();
		System.out.println("Looking for Airplane Model 'A310'");
		System.out.println("Found " + aParse.getAirplane("A310").toString());
		
		System.out.println();
		System.out.println("Looking for Airplane Model '777'");
		System.out.println("Found " + aParse.getAirplane("777").toString());
		
		System.out.println();
		System.out.println("Looking for Airplane Model 'ABC'");
		System.out.println("Found " + aParse.getAirplane("ABC").toString());
		
	}
	
	@SuppressWarnings("unused")
	private static void testFlightParserClass() {

		System.out.println("FLightParser Tester\n");

		/* Make an Flight Parser */
		FlightParser aParse = new FlightParser();
		aParse.parseFlightXML();  // Parses the XML
	}

	@SuppressWarnings("unused")
	private static void testAirPortClass(){
		System.out.println("\nAirport Tester");
		
		Location testLoc = new Location(33.641045,-84.427764);
		
		/* Testing the Airport Class */
		Airport airport = new Airport("BOS", "Boston Logan", testLoc);
		
		System.out.println(airport.toString());

	}
	
	@SuppressWarnings("unused")
	private static void testAirPlaneClass() {
		
		System.out.println("\nAirplane Tester");
		
		/* Testing the Airplane Class */
		Airplane airplane1 = new Airplane("774", "Airbus", 20, 50);
		Airplane airplane2 = new Airplane("777", "Airbus", 15, 60);
		Airplane airplane3 = new Airplane("123", "Boeing", 10, 100);
		Airplane airplane4 = new Airplane("456", "Boeing", 10, 70);
		
		System.out.println(airplane1.toString());
		System.out.println();
		System.out.println(airplane2.toString());
		System.out.println();
		System.out.println(airplane3.toString());
		System.out.println();
		System.out.println(airplane4.toString());
		System.out.println();
	}

	@SuppressWarnings("unused")
	private static void testXMLGetter() throws IOException {
		
		System.out.println("Testing XMLGetter Class");

		XMLGetter test = new XMLGetter(); // create the test object
		
		System.out.println("\n"+ test.getAirportsXML()); // the XML String
		System.out.println(test.toString()); // Print how many XMLs have been gotten
		
		/* Turns the XML String into an XML file */
		java.io.FileWriter fw = new java.io.FileWriter("test-airports.xml");
		fw.write(test.getAirportsXML());
		fw.close();
		
		
		System.out.println("\n"+test.getAirportsXML()); // the XML String
		System.out.println(test.toString()); // 3 XMLs should have been gotten
	
	}
		
	@SuppressWarnings("unused")
	private static void testDateClass(){
		System.out.println("Testing Date Class");
		
		/* Making a Date object called test */
		Date test = new Date(Month.November,21,1991);
		
		/* Printing the Date */
		System.out.println(test.toString());
		
		/* Printing the month, day, and year of the object */
		System.out.println("The Month of this Date object is£º" + test.getMonth());
		System.out.println("The Day of this Date object is£º" + test.getDay());
		System.out.println("The Year of this Date object is£º" + test.getYear());
		
		Date richardBday = new Date(Month.August,22,1990);
		System.out.println("Richard is born in the month of: " + richardBday.getMonth());

	}
}

