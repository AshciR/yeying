package flight_system;

import graph.*;

import java.util.ArrayList;
import java.util.LinkedList;

import org.graphstream.graph.Edge;

import parsers.*;
import user_interface.*;

public class FlightSystem {

	/* Holds the states for the Flight System */
	private enum State {
		GetUserInfo, ShowFlights, BuyFlights, FinishState, DetailFlights
	};

	private ArrayList<Airport> airportList;
	private ArrayList<Airplane> airplaneList;
	private ArrayList<Flight> flightList, showFlightList;
	private State state;
	private UserInfo userInfo;
	private IUserInterface iFace;

	/* The constructor */
	public FlightSystem(IUserInterface iFace) {
		this.airportList = new ArrayList<Airport>();
		this.airplaneList = new ArrayList<Airplane>();
		this.flightList = new ArrayList<Flight>();
		this.showFlightList = new ArrayList<Flight>();
		this.userInfo = new UserInfo(null, null, null, false);
		this.iFace = iFace;
	};
	
	/* Getter Functions */
	public ArrayList<Airport> getAirportList() {
		return airportList;
	}

	public ArrayList<Airplane> getAirplaneList() {
		return airplaneList;
	}

	/* Method that runs the prototype */
	public void run() {
		
		/* Initialize System */
		initSys();

		/* Go to Get User Info State */
		this.state = State.GetUserInfo;

		/* Determines whether the user wants to continue */
		boolean userContinue = true;

		do {
			switch (state) {
			case GetUserInfo:

				/* Updates the user's info */
				askUserForInfo();
				break;

			case ShowFlights:

				/* Gets the flights from the DB */
				getFlights();

				/* Shows the user the flight(s) if available */
				showFlights();
				break;
			
			case DetailFlights:
				
				/* Asks the user if they want to see the detail flight info */
				showDetail();
				break;
				
			case BuyFlights:
								
				/* Get the flight for the flight the user wants to buy */
				Flight fltOpt = getFltOpt(); 
				
				/* Show the user what flight they selected */
				iFace.userFlightChoice(flightList.indexOf(fltOpt) + 1);
				
				/* Print the details about the flight, again, just to confirm
				 * with the user */
				fltOpt.printDetailFlight(userInfo.getIsFirstClass());
				
				/* Result from trying to buy the flight */
				int result = buyFlt(fltOpt);
				
				/* Find out the result from trying to buy the ticket */
				switch (result){
				
				/* Successful buy */
				case 200:
					/* Tell user the ticket was bought */
					iFace.confirmFlight();
					
					/* Print the flight again to confirm */
					fltOpt.printFlight(userInfo.getIsFirstClass());
					
					/* Go to finished state */
					state = State.FinishState;
					break;
				
				/* The flight doesn't exist anymore */
				case 304:
					iFace.flightDisappear();
					state = State.ShowFlights;
					break;
				
				/* We don't have the lock on the database */	
				case 412:
					iFace.dataBaseLocked();
					state = State.ShowFlights;
					break;
				
				/* Something else happened */
				default:
					ErrorHandler.whoops();
					state = State.ShowFlights;
					break;
				}
				
				break;
				
			case FinishState:
				
				/* Asks if the user is finished */
				userContinue = finishSys();
				break;

			default:
				break;
			}
			
		} while (userContinue == true);

	}
	
	/* Asks the user if they want to get more detail 
	 * about a flight. If they do, then we print that info,
	 * if not, then we go to the buy flights state.
	 */
	private void showDetail() {
		
		String detAns = iFace.wantDetail();
		boolean ansValid = false;
		
		do {
			/* Go back to user info */
			if (detAns.startsWith("Y")) {
				
				/* Get the flight option the user wants
				 * more info about */
				String fltOpt = iFace.getDetailFlight();
				
				try {
					int fltOptNum = Integer.parseInt(fltOpt);
					
					/* We know it's an integer */
					try {
						
						/* Prints the detail info about the flight */
						flightList.get(fltOptNum).printDetailFlight(userInfo.getIsFirstClass());
						
						String detAns2nd = iFace.getAnotherDetail();
						
						/* If they want detail about another flight */
						if(detAns2nd.startsWith("Y")){
							
							ansValid = true;
							
							/* Call this function again */
							showDetail();
						}
						else if (detAns2nd.startsWith("N")){
							
							ansValid = true;
							/* Go the the buy state */
							this.state = State.BuyFlights;
						}
						else{
							/* Not a valid answer */
							ErrorHandler.notYesOrNo();
						}
						
					/* If the user tries to select an incorrect option */
					} catch (IndexOutOfBoundsException e) {
						ErrorHandler.invalidFlight();
					}
				
				/* The user did not type a number */
				} catch (NumberFormatException e) {
					ErrorHandler.notANum();
				}
		
			}
			/* The user doesn't want any flight detail */
			else if (detAns.startsWith("N")) {
				
				ansValid = true;
		
				/* Go the the buy state */
				this.state = State.BuyFlights;
			
			} else {
				/* Not a valid answer */
				ErrorHandler.notYesOrNo();
			}
			
		} while (!ansValid);

		
	}

	private int buyFlt(Flight flight) {
		
		/* Get the XML Putter Instance */
		XMLPutter dbPutter = XMLPutter.getInstance();

		/* Make the ticket */
		String ticket = dbPutter.makeTicket(flight, userInfo.getIsFirstClass());

		/* Result from buy ticket, will be using this to
		 * determine what happened */
		return dbPutter.buyTicket(ticket);

	}

	
	private Flight getFltOpt() {
		
		/* Holds whether User input an available flight */
		boolean validFlight = false;
		
		/* The flight option that will be returned */
		Flight fltOpt = null;
		
		/* Integer version of the user's flight option */
		int fltNum;
		
		do {
			
			/* Get the Flight Option the user wants to purchase */
			String fltNumStr = iFace.bookFlight();
			
			/* Try converting string to integer */
			try {
				
				fltNum = Integer.parseInt(fltNumStr);
				
				/* Look to see if the flight option is in
				 * the list */
				try {
					
					fltOpt = flightList.get(fltNum - 1);
					validFlight = true;
				
				/* Not the in the list! */
				} catch (IndexOutOfBoundsException e) {
					ErrorHandler.invalidFlight();
				}
			
			/* Not a number! */
			} catch (NumberFormatException e) {
				ErrorHandler.notANum();
			}

		} while (!validFlight);
		
		return fltOpt;
	}

	private boolean finishSys() {
		
		boolean userContinue = false;
		boolean ansValid = false;
		
		/* Ask if they want continue */
		iFace.bookAnother();
		String ans = iFace.bookAnother();
		
		do {
			
			/* Go back to user info */
			if (ans.startsWith("Y")) {
				state = State.GetUserInfo;
				ansValid = true;
				
				userContinue = true;
			}
			/* Close Program */
			else if (ans.startsWith("N")) {
				
				iFace.goodbyeMsg();
			
				/* Reset our DB to the original state */
				
				/* The only DB Getter Object */
				XMLGetter dbGetter = XMLGetter.getInstance();
				dbGetter.resetDB();
				
				ansValid = true;
				userContinue = false;
			
			} else {
				ErrorHandler.notYesOrNo();
			}

		} while (!ansValid);

		return userContinue;
		
	}

	private void showFlights() {
		
		/* Needed to get rid of past results */
		showFlightList.clear();
		
		/* If there flights */
		if (showFlightList.size() != 0) {
			
			/* Tell the user that there are # of available flights */
			iFace.numOfFlights(flightList.size());
			
			/* Print all the available flights */
			for (Flight flight : flightList) {
				iFace.printFlightOption(flightList.indexOf(flight));
				flight.printFlight(userInfo.getIsFirstClass());
			}
			
			this.state = State.DetailFlights; // go to the buy flight state
			
		}
		/* No matching flights */
		else {
			
			/* Tell the user there are no flights */
			iFace.noFlights();
			this.state = State.FinishState; // go to the finished state
		}

	}

	private void getFlights() {
		
		/* Make the flight graph based on the user's 
		 * flight information */
		GraphMaker gMaker = new GraphMaker(userInfo.getDepartureDate());

		/* Use the graph engine to find the flights after
		 * the graph is made */
		GraphEngine engine = new GraphEngine(gMaker.getGraph());
		
		/* Search for the flights that meet the user's 
		 * requirements. Note, that it will return flights
		 * that have up to maximum 2 connections. */
		ArrayList<LinkedList<Edge>> availFlights = engine.getRoutes(userInfo.getDepartureAirport(), userInfo.getArrivalAirport(), 3, userInfo.getIsFirstClass());
		
		/* Converts the graph edges, which are flights, into Flight objects
		 * that can be used throughout the rest of the program */
		for(LinkedList<Edge> flight : availFlights){
			
			/* Make a new flight */
			Flight addedFlight = new Flight();
			
			for (Edge edge : flight){
				
				/* Get the flight object from the edge */
				FlightLeg fLeg = edge.getAttribute("fltInfo");
				
				/* Add the Flight Leg to the flight */
				addedFlight.addFlightLeg(fLeg);
			}
			
			/* Add the flight to the flight list */
			flightList.add(addedFlight);
			
		}
		
	}

	private void askUserForInfo() {

		/* User Airports */
		Airport depAirport, arrAirport;
		Date depDate;
		int day = 0; // day of departure
		boolean seat = false; // coach by default

		/* Airport parser object */
		AirportParser portParser = AirportParser.getInstance();

		do {
			
			/* Get the Depart Airport from the user */
			String depAirportStr = iFace.getDepartureAirport();
		
			/* Create the departure Airport object */
			depAirport = portParser.getAirport(depAirportStr);

			/* Invalid Data */
			if (depAirport == null) {
				ErrorHandler.invalidPort();
			}

		} while (depAirport == null);

		/* Holds whether User input a airport */
		boolean validPort = true;

		do {

			/* Get the Arrival Airport from the user */
			String arrAirportStr = iFace.getArrivalAirport();

			/* Create the departure Airport object */
			arrAirport = portParser.getAirport(arrAirportStr);

			/* Validates Arrival Airport */
			if (arrAirport == null) {
				ErrorHandler.invalidPort();
				validPort = false;
			} else if (arrAirport.getCode().equalsIgnoreCase(depAirport.getCode())) {
				ErrorHandler.samePort();
				validPort = false;
			} else {
				validPort = true;
			}

		} while (!validPort);

		/* Holds whether User input a good date */
		boolean validDate = false;

		do {
			/* Get the Departure Day */
			String dayStr = iFace.getLeavingDate();

			try {
				day = Integer.parseInt(dayStr);

				if (day > 18 || day < 8) {
					ErrorHandler.invalidDate();
				} else {
					validDate = true;
				}

			} catch (NumberFormatException e) {
				ErrorHandler.notANum();
			}

		} while (!validDate);

		/* Makes the date object */
		depDate = new Date(Month.May, day, 2015);

		/* Holds whether User inputs a valid seat */
		boolean validSeat = false;

		do {
			/* Ask if they want First Class */
			String classType = iFace.isFirstClassSeat();
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
			} else {
				ErrorHandler.notYesOrNo();
			}

		} while (!validSeat);

		/* Set the User info */
		userInfo.setArrivalAirport(arrAirport);
		userInfo.setDepartureAirport(depAirport);
		userInfo.setDepartureDate(depDate);
		userInfo.setIsFirstClass(seat);
		
		/* Go to Show Flights State */
		state = State.ShowFlights;
		
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
		airportList = portParser.getAirportList(); // puts in local list

		AirplaneParser planeParser = AirplaneParser.getInstance();
		planeParser.parseAirplaneXML(airplanesXMLString);
		airplaneList = planeParser.getAirplaneList(); // puts in local list
		
		iFace.displayWelcomeMsg();
	
	}

}
