package flight_system;

import parsers.*;

public class Tester {

	public static void main(String[] args) {
		
		testAirportParserClass();
		
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
	
	private static void testAirportParserClass() {
		
		System.out.println("\nAirportParser Tester");
		
		/* Make an Airport Parser */
		AirportParser aParse = new AirportParser();
		aParse.parseAirportXML();  // Parses the XML
		
		/* Print the number of Airports */
		System.out.println("There are " + aParse.getNumOfAirports() + " airports.\n" );	
		
		aParse.printAirportList(); // Prints the list of airplanes
	}
	
	@SuppressWarnings("unused")
	private static void testAirplaneParserClass() {
		
		System.out.println("\nAirplaneParser Tester");
		
		/* Make an Airplane Parser */
		AirplaneParser aParse = new AirplaneParser();
		aParse.parseAirplaneXML();  // Parses the XML
		
		/* Print the number of Airplanes */
		System.out.println("There are " + aParse.getNumOfAirplanes() + " airplanes.\n" );	
		
		aParse.printAirplaneList(); // Prints the list of airplanes
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

}
