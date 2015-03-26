package flight_system;

import java.util.ArrayList;
import java.util.Scanner;

import parsers.*;

public class Prototype {
	
	/* Holds the states for the Flight System */
	private enum State{GetUserInfo, ShowFlights, BuyFlights}; 
	private ArrayList<Airport> airportList;
	private ArrayList<Airplane> airplaneList;
	private State state;
	
	/* The constructor */
	public Prototype(){};
	
	/* Method that runs the prototype */
	public void run(){
		
		UserInfo userInfo;
		
		/* Initialize System */
		initSys(); 
		
		/* Go to Get User Info State */
		this.state = State.GetUserInfo;
		
		/* Determines whether the user wants to continue */
		boolean userContinue = false; 
		
		do {
			switch (state) {
			case GetUserInfo:
				
				/* Stores the user info in this object */
				userInfo = askUserForInfo();
				
				/* Go to Show Flights State */
				state = State.ShowFlights;
				break;
			
			case ShowFlights:
				break;
			case BuyFlights:
				break;

			default:
				break;
			}
		} while (userContinue  == true);
		
	}

	private UserInfo askUserForInfo() {
		
		System.out.println("\nWelcome to YeYing's Flight Reservation System!\n");
		
		/* User Airports */
		Airport depAirport, arrAirport; 
		Date depDate;
		int day = 0; // day of departure
		boolean seat = false; // coach by default
		
		/* Scanner object */
		Scanner userInput = new Scanner(System.in);
		
		/* Airport parser object */
		AirportParser portParser = AirportParser.getInstance();
		
		do {
			/* Get the Depart Airport from the user */
			System.out.print("\nWhat is your Departure Airport? ");
			String depAirportStr = userInput.nextLine(); // -- RESUME AFTER DINNER!
			
			/* Create the departure Airport object */
			depAirport = portParser.getAirport(depAirportStr);
			
			/* Invalid Data */
			if (depAirport == null){
				System.out.println("That is not a valid Departure Airport");
			}
			
		} while (depAirport == null);
		
		/* Holds whether User input a airport */
		boolean validPort = true; 
		
		do {
			
			/* Get the Arrival Airport from the user */
			System.out.print("\nWhat is your Arrival Airport? ");
			String arrAirportStr = userInput.nextLine();
			
			/* Create the departure Airport object */
			arrAirport = portParser.getAirport(arrAirportStr);
			
			/* Validates Arrival Airport */
			if (arrAirport == null) {
				System.out.println("That is not a valid Arrival Airport");
				validPort = false;
			}
			else if ( arrAirport.getCode().equalsIgnoreCase( depAirport.getCode() ) ) {
				System.out.println("Arrival Aiport can't be the same as Departure!");
				validPort = false;
			}
			else{
				validPort = true;
			}
		
		} while (!validPort);
		
		/* Holds whether User input a good date */
		boolean validDate = false; 
		
		do {
			/* Get the Departure Day */
			System.out.print("\nWhat day do you want to leave? ");
			String dayStr = userInput.nextLine();
			
			try {
				day = Integer.parseInt(dayStr);
				
				if (day > 18 || day < 8) {
					System.out.println("That is not a valid Date");
				}
				else{
					validDate = true;
				}
				
			} catch (NumberFormatException e) {
				System.out.println("That is not a number!");
			}
			
		} while (!validDate);
		
		/* Makes the date object */
		depDate = new Date(Month.May, day, 2015);
		
		/* Holds whether User inputs a valid seat */
		boolean validSeat = false; 
		
		do {
			/* Ask if they want First Class */
			System.out.print("\nDo you want first class? (Y/N) ");
			String classType = userInput.nextLine();
			classType = classType.toUpperCase();
			
			/* 1st Class */
			if (classType.startsWith("Y")) {
				seat = true;
				validSeat = true;
			}
			/* Coach */
			else if (classType.startsWith("N")) {
				seat = false;
				validSeat = true;
			}
			else{
				System.out.println("Please type Y or N");
			}
			
		} while (!validSeat);
		
		/* Makes the user info object */
		UserInfo userInfo = new UserInfo(depAirport, arrAirport, depDate, seat); 
		
		return userInfo;
		
	}

	private void initSys() {
		
		/* The only DB Getter Object */
		XMLGetter dbGetter = XMLGetter.getInstance();
		
		/* Get the list of airports from the DB */
		String airportsXMLString = dbGetter.getAirportsXML();
		
		/* Get the list of airplanes from the DB */
		String airplanesXMLString = dbGetter.getAirplaneXML();
		
		/* Parsing the Airplane and Airport Info */
		AirportParser portParser = AirportParser.getInstance();
		portParser.parseAirportXML(airportsXMLString);
		airportList = portParser.getAirportList(); //puts in local list
		
		AirplaneParser planeParser = AirplaneParser.getInstance();
		planeParser.parseAirplaneXML(airplanesXMLString);
		airplaneList = planeParser.getAirplaneList(); //puts in local list
	
		
	}
	
}
