package flight_system;

import java.io.IOException;

import parsers.*;

public class Tester {

	public static void main(String[] args) throws IOException {

		
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
		if ( aParse.getAirport("XYZ") != null ){
			System.out.println("Found " + aParse.getAirport("XYZ").toString());
		}
		else{
			System.out.println("Airport not Found.");
		}	
	}
	
	@SuppressWarnings("unused")
	private static void testFlightParserClass() {

		System.out.println("\nFlightParser Tester");

		/* Make an Flight Parser */
		FlightParser fParse = new FlightParser();
		fParse.parseFlightXML();  // Parses the XML

		/* Print the number of Airplanes */
		System.out.println("There are " + fParse.getNumOfFlights() + " flights.\n" );	

		fParse.printFlightList(); // Prints the list of airports

		System.out.println();
		System.out.println("Looking for Flight with number '1781'\n");
		System.out.println("Found it!: " + fParse.getFlight(1781).toString());

		System.out.println();
		System.out.println("Looking for Flight with number '1825'\n");
		System.out.println("Found it!: " + fParse.getFlight(1825).toString());
		
		System.out.println();
		System.out.println("Looking for Flight with number '1817'\n");
		System.out.println("Found it!: " + fParse.getFlight(1817).toString());
	
		System.out.println();
		System.out.println("Looking for Flight with number '9999'\n");
		if ( fParse.getFlight(9999) != null ){
			System.out.println("Found " + fParse.getFlight(9999).toString());
		}
		else{
			System.out.println("Flight not Found.");
		}

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
		if ( aParse.getAirplane("ABC") != null ){
			System.out.println("Found " + aParse.getAirplane("ABC").toString());
		}
		else{
			System.out.println("Airplane not Found.");
		}
		
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
		
		/* Builds the test data */
		Location testLoc = new Location(39.177641,-76.668446);
		Airport depairport = new Airport("BOS","Logan",testLoc);
		Date date = new Date(Month.May,10,2015);
		System.out.println("Testing XMLGetter Class");

		XMLGetter test = XMLGetter.getInstance(); // create the test object
		
		System.out.println("\n"+ test.getAirportsXML()); // the XML String for airports
		System.out.println("\n"+ test.getAirplaneXML());// get the XML String for airplane
		System.out.println("\n"+ test.getFlightsXML(true,depairport,date));// the XML String for Flights
		System.out.println(test.toString()); // Print how many XMLs have been gotten
		
		test.resetDB();// test resetting database
		
		/* Turns the XML String into an XML file */
		java.io.FileWriter fw = new java.io.FileWriter("test-airports.xml");
		fw.write(test.getAirportsXML());
		fw.close();
		
		java.io.FileWriter fw1 = new java.io.FileWriter("test-airplane.xml");
		fw1.write(test.getAirplaneXML());
		fw1.close();
		
		java.io.FileWriter fw2 = new java.io.FileWriter("test-Flights.xml");
		fw2.write(test.getFlightsXML(false,depairport,date));
		fw2.close();
		
		System.out.println("\n"+test.getAirportsXML()); // the XML String
		System.out.println("\n"+test.getAirplaneXML());// the XML String for airplane
		System.out.println("\n"+ test.getFlightsXML(true,depairport,date));// the XML String for Flights
		test.resetDB();// test resetting database
		System.out.println(test.toString()); // 9 XMLs should have been gotten
	
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
	
	@SuppressWarnings("unused")
	private static void testTimeClass(){
		System.out.println("Testing Time Class\n");
		
		Time test = new Time(20,58);
		
		/* Printing the Time */
		System.out.println(test.toString());
		
		/* Printing the hours and minutes of the object */
		System.out.println("The hour is : "+test.getHours());
		System.out.println("The minutes are : "+test.getMinutes());
		System.out.println("The time in minutes is : "+test.getTimeInMinutes());
		System.out.println("The hour in 12 hour format is : "+test.getHoursIn12());
		System.out.println("The time is in AM : "+test.isAM());
	}
	
	@SuppressWarnings("unused")
	private static void testFlightLegClass(){
		System.out.println("Testing Flight Leg Class\n");
		
		/* Make an airplane for the flight */
		Airplane airplane = new Airplane("774", "Airbus", 20, 50);
		
		/* -- Departure Info -- */
		Time dTime = new Time(20,00);
		Date dDate = new Date(Month.March,18,2015);
		
		/* Location of the Departing Airport and Airport  */
		Location dLoc = new Location(33.641045,-84.427764);
		Airport dAirport = new Airport("BOS", "Boston Logan", dLoc);
		
		/* -- Arrival Info -- */
		Time aTime = new Time(21,15);
		Date aDate = new Date(Month.March,18,2015);
		
		/* Location of the Arriving Airport and Airport  */
		Location aLoc = new Location(33.641045,-84.427764);
		Airport aAirport = new Airport("JFK", "John F. Kennedy International", aLoc);
		
		FlightLeg test = new FlightLeg(airplane, 2000, 300, dTime, dDate, dAirport, 
										aTime, aDate, aAirport, 50.00, 15, 25.00, 25);
		
		/* Printing the Flight */
		System.out.println(test.toString());
		
	}
	
@SuppressWarnings("unused")
private static void testXMLPutter() {
		
	System.out.println("Testing XMLPutter Class");
	
	/* Reset the DB in before you purchase flight */
	XMLGetter resetter = XMLGetter.getInstance();
	resetter.resetDB();

	XMLPutter test = XMLPutter.getInstance(); // create the test object
	
	/* Make a ticket for the test case */
	String testTicket1 = test.makeTicket(1781, true); // Flight 1781 FirstClass
	String testTicket2 = test.makeTicket(1781, false); // Flight 1781 FirstClass
	
	System.out.println("\nTicket 1 info is:");
	System.out.println(testTicket1);
	
	System.out.println("\nTicket 2 info is:");
	System.out.println(testTicket2);
	
	/* Lock the database before we purchase the tickets */
	test.lockDB();
	test.buyTicket(testTicket1);
	test.buyTicket(testTicket2);
	
	/* Unlock after the purchase */
	test.unlockDB();
	
	/* Should Print the fact that 2 tickets were bought */
	System.out.println(test.toString());
	
	/* Reset the DB to default state */
	resetter.resetDB();
	
	}
}


