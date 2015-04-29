package flight_system;

import graph.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import org.graphstream.graph.Edge;

import parsers.*;
import user_interface.*;

public class FlightSystem {

	/* Holds the states for the Flight System */
	private enum State {
		GetUserInfo, ShowFlights, Options, FilterFlights, 
		SortFlights, DetailFlights, BuyFlights, FinishState
		
	};

	private ArrayList<Airport> airportList;
	private ArrayList<Airplane> airplaneList;
	private ArrayList<Flight> originFlightList, returnFlightList, originFilter, returnFilter, sortList;
	private State state;
	private UserInfo userInfo;
	private IUserInterface iFace;
	private boolean hasBeenFiltered, hasBeenSorted; 

	/* The constructor */
	public FlightSystem(IUserInterface iFace) {
		this.airportList = new ArrayList<Airport>();
		this.airplaneList = new ArrayList<Airplane>();
		this.originFlightList = new ArrayList<Flight>();
		this.returnFlightList = new ArrayList<Flight>();
		this.originFilter = new ArrayList<Flight>();
		this.returnFilter = new ArrayList<Flight>();
		this.sortList = new ArrayList<Flight>();
		this.userInfo = new UserInfo(null, null, null, null, false, false);
		this.iFace = iFace;
		this.hasBeenFiltered = false;
		this.hasBeenSorted = false;
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
		
		/* Print a list of all the available airports */
		System.out.println("\nHere is a list of all the availalbe airports:\n");
		
		/* Used to print a new line in the airport list, 
		 * if the current line is over 4 airports long */
		int printCount = 0;
		
		for(Airport port : airportList){
			
			System.out.print(port.getCode() + "\t");
			printCount++;
			
			/* Print a new line */
			if (printCount >=4 ){
				System.out.println("\n");
				printCount = 0;
			}
			
		}
		
		/* Go to Get User Info State */
		this.state = State.GetUserInfo;

		/* Determines whether the user wants to continue */
		boolean userContinue = true;

		do {
			switch (state) {
			case GetUserInfo:
				
				/* If we are starting to get info about a new trip, 
				 * then we have not filtered before */
				hasBeenFiltered = false;
				hasBeenSorted = false;
				
				/* Updates the user's info */
				askUserForInfo();
				
				/* Go to Show Flights State */
				state = State.ShowFlights;
				
				break;

			case ShowFlights:
				
				/* If we have already filtered this search, 
				 * don't get new info yet. */
				if(!hasBeenFiltered && !hasBeenSorted){
					/* Gets the flights from the DB */
					getFlights(userInfo.getIsRoundTrip());
				}
				
				/* Shows the user the flight(s) if available */
				
				/* If there are no flights */
				if(!showFlights(hasBeenFiltered)){
					
					/* 1st Search doesn't give any results */
					if(!hasBeenFiltered && !hasBeenSorted){
						/* Then go to finish state */
						state = State.FinishState;
					}
					else{
						state = State.Options;
					}
						
				}
				/* There are flights, ask what the user would like to do */
				else{
					
					/* Go to the Options State */
					state = State.Options;
				}
				
				break;
			
			case Options:
				
				/* Holds whether User input a good date */
				boolean validOption = false;
				
				/* Holds the integer value for the option
				 * the user selects */
				int optionInt = 0;
				
				/* Keep asking the user for options until a valid number
				 * is inputed */
				do {
					/* Ask the user what they would like to do */
					String option = iFace.doWhatWithFlights();
			
					try {
						optionInt = Integer.parseInt(option);
						
						if (optionInt > 6 || optionInt < 1) {
							ErrorHandler.invalidOption();
						} else {
							validOption = true;
						}
						
						
					} catch (NumberFormatException e) {
						ErrorHandler.notANum();
					}
			
				} while (!validOption);
				
				/* If we got here, then we know the user inputed a valid option
				 * Go to the state that corresponds with the selected option */
				switch((optionInt)){
				
				/* Filter */
				case 1:
					state = State.FilterFlights;
					break;
				
				/* Sort */	
				case 2:
					state = State.SortFlights;
					break;
				
				/* Detail */
				case 3:
					state = State.DetailFlights;
					break;
				
				/* Buy */	
				case 4:
					state = State.BuyFlights;
					break;
				
				/* Search new Trip */	
				case 5:
					state = State.GetUserInfo;
					break;
				
				/* Close Program */	
				case 6:
					state = State.FinishState;
					break;
				}
				
			break; // finish options state	
			
			case FilterFlights:
				
				/* If they filtered the flights */
				hasBeenFiltered = filterFlights();
				
				/* Filter flights */
				if(hasBeenFiltered){
					
					/* Go back to show flights */
					state = State.ShowFlights;
					
				}
				/* Not been filtered */
				else{
					
					state = State.Options;
					
				}
				break;
				
			case SortFlights:
				
				/* Sort the flights */
				if(sortFlights()){
					state = State.ShowFlights;
					hasBeenSorted = true;
				}else{
					state = State.Options;
				}
				break;
				
			case DetailFlights:
				
				/* Asks the user if they want to see the detail flight info */
				showDetails();
				break;
				
			case BuyFlights:
								
				/* Get the flight for the flight the user wants to buy */
				Flight fltOpt = getFltOpt(); 
				
				/* Show the user what flight they selected */
				iFace.userFlightChoice(originFlightList.indexOf(fltOpt) + 1);
				
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
	
	private void initSys() {
	
		/* Display the welcome message */
		iFace.displayWelcomeMsg();
		
		/* The only DB Getter Object */
		XMLGetter dbGetter = XMLGetter.getInstance();
	
		System.out.println("\nGetting ready to serve all your flight needs...");
		
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
		
		System.out.println("All Done!");
		
	
	}

	private void askUserForInfo() {
	
		/* User Airports */
		Airport depAirport, arrAirport;
		Date depDate, returnDate = null;
		int day = 0; // day of departure
		int returnDay = 0; // day of return
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
		
		/* Ask if they want to make a round trip */
		String roundAns = iFace.wantRoundTrip();
		
		/* Loop until the user inputs Y or N */
		do{
			
			/* Tell the user to input Y or N */
			if(!validUserAns(roundAns, "Y", "N")){
				
				ErrorHandler.notYesOrNo();

				/* Ask the user again if they want detail about the flight */
				roundAns = iFace.wantRoundTrip();
			}
			
			
		}
		while(!validUserAns(roundAns, "Y", "N"));
		
		/* Holds whether the user wants to do a round trip */
		boolean roundTrip; 
		
		/* If they want a round trip */
		if(roundAns.startsWith("Y")){
			roundTrip = true;
		}
		else{
			roundTrip = false;
		}
			
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
		
		/* If they selected a round trip, ask for the return date */
		if(roundTrip){
			
			/* Holds whether User input a good date */
			boolean validReturnDate = false;
		
			do {
				/* Get the Departure Day */
				String dayStr = iFace.getReturnDate();
		
				try {
					returnDay = Integer.parseInt(dayStr);
					
					/* If the return day is less than the 
					 * departure day, or more than 18 */
					if (returnDay > 18) {
						ErrorHandler.invalidDate();
					} 
					else if (returnDay < depDate.getDay()){
						ErrorHandler.invalidReturn();
					}
					else {
						
						/* The return day is ok */
						validReturnDate = true;
					}
		
				} catch (NumberFormatException e) {
					ErrorHandler.notANum();
				}
		
			} while (!validReturnDate);
			
			/* Make the return date */
			returnDate = new Date(Month.May, returnDay, 2015);
			
		} // end get return trip date  
		
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
		if(roundTrip){userInfo.setReturnDate(returnDate);} //Set return date
		userInfo.setIsFirstClass(seat);
		userInfo.setIsRoundTrip(roundTrip);
		
		/* Echo the user's selection */
		System.out.println("\nSearching for flights based on this information:");
		userInfo.printUserInfo();
		
	}

	private void getFlights(boolean isRoundTrip) {
		
		/* Tell the user that the system is searching for flights */
		iFace.searchFlights();
		
		/* Empty the lists each time we're going to get new flight data */
		originFlightList.clear();
		returnFlightList.clear();
		
		/* Make the departure flight graph */
		GraphMaker gMakerDep = new GraphMaker(userInfo.getDepartureDate());
	
		/* Make the return flight graph */
		GraphMaker gMakerRet = new GraphMaker(userInfo.getReturnDate());

		/* Use the graph engine to find the departure flights */ 
		GraphEngine engineDep = new GraphEngine(gMakerDep.getGraph());
		
		/* Use the graph engine to find the return flights */ 
		GraphEngine engineRet = new GraphEngine(gMakerRet.getGraph());
		
		/* Search for the departure flights that meet the user's 
		 * requirements. Note, that it will return flights
		 * that have up to maximum 2 connections. */
		ArrayList<LinkedList<Edge>> availDepFlights = engineDep.getRoutes(userInfo.getDepartureAirport(), userInfo.getArrivalAirport(), 3, userInfo.getIsFirstClass());
		
		/* Search for the arrival flights that meet the user's 
		 * requirements. Note, that it will return flights
		 * that have up to maximum 2 connections. */
		ArrayList<LinkedList<Edge>> availRetFlights = engineRet.getRoutes(userInfo.getArrivalAirport(), userInfo.getDepartureAirport(), 3, userInfo.getIsFirstClass());
		
		/* Converts the graph edges, which are flights, into Flight objects
		 * that can be used throughout the rest of the program */
		for(LinkedList<Edge> flight : availDepFlights){
			
			/* Make a new flight */
			Flight addedFlight = new Flight();
			
			for (Edge edge : flight){
				
				/* Get the flight object from the edge */
				FlightLeg fLeg = edge.getAttribute("fltInfo");
				
				/* Add the Flight Leg to the flight */
				addedFlight.addFlightLeg(fLeg);
			}
			
			/* Add the flight to the flight list */
			originFlightList.add(addedFlight);
			
		}
		
		/* Converts the graph edges, which are flights, into Flight objects
		 * that can be used throughout the rest of the program */
		for(LinkedList<Edge> flight : availRetFlights){
			
			/* Make a new flight */
			Flight addedFlight = new Flight();
			
			for (Edge edge : flight){
				
				/* Get the flight object from the edge */
				FlightLeg fLeg = edge.getAttribute("fltInfo");
				
				/* Add the Flight Leg to the flight */
				addedFlight.addFlightLeg(fLeg);
			}
			
			/* Add the flight to the flight list */
			returnFlightList.add(addedFlight);
			
		}
		
		
	}

	private boolean showFlights(boolean hasBeenFiltered) {

		if(!hasBeenFiltered){
						
			/* If it's not a round trip */
			if(!userInfo.getIsRoundTrip()){
				
				/* If there departure flights */
				if (originFlightList.size() != 0)  {
					
					System.out.println("\n--- Departure Flights Info: ---");
					
					/* Tell the user that there are # of available flights */
					iFace.numOfFlights(originFlightList.size());

					/* Print all the available flights */
					for (Flight flight : originFlightList) {
						iFace.printFlightOption(originFlightList.indexOf(flight));
						flight.printFlight(userInfo.getIsFirstClass());
						System.out.println();
					}
					
					return true;
				
				}
				/* There are no departure flights */
				else{
					/* Tell the user there are no flights */
					iFace.noFlights();
					return false; // No flights
				}
						
			}
			/* if it is a round trip */
			else {
				
				/* If there are departure flights and return flights */
				if (originFlightList.size() != 0 && returnFlightList.size() !=0 )  {
					
					System.out.println("\n--- Departure Flights Info: ---");

					/* Tell the user that there are # of available flights */
					iFace.numOfFlights(originFlightList.size());

					/* Print all the available flights */
					for (Flight flight : originFlightList) {
						iFace.printFlightOption(originFlightList.indexOf(flight));
						flight.printFlight(userInfo.getIsFirstClass());
						System.out.println();
					}

					
					System.out.println("--- Arrival Flights Info: ---");

					/* Tell the user that there are # of available flights */
					iFace.numOfFlights(returnFlightList.size());

					/* Print all the available flights */
					for (Flight flight : returnFlightList) {
						iFace.printFlightOption(returnFlightList.indexOf(flight));
						flight.printFlight(userInfo.getIsFirstClass());
						System.out.println();
					}

					return true;
					
				}
				/* There are no flights */
				else{
					/* Tell the user there are no flights */
					iFace.noFlights();
					return false; // No flights
				}
					
				
			}
		}
		/* Has the flights been filtered, use the filter list */
		else{
						
			/* If it's not a round trip */
			if(!userInfo.getIsRoundTrip()){
				
				/* If there departure flights */
				if (originFilter.size() != 0)  {
					
					System.out.println("\n--- Departure Flights Info: ---");
					
					/* Tell the user that there are # of available flights */
					iFace.numOfFlights(originFilter.size());

					/* Print all the available flights */
					for (Flight flight : originFilter) {
						iFace.printFlightOption(originFilter.indexOf(flight));
						flight.printFlight(userInfo.getIsFirstClass());
						System.out.println();
					}
					
					return true;
				
				}
				/* There are no departure flights */
				else{
					/* Tell the user there are no flights */
					iFace.noFlights();
					return false; // No flights
				}
						
			}
			/* if it is a round trip */
			else {
				
				/* If there are departure flights and return flights */
				if (returnFilter.size() != 0 && originFilter.size() !=0 )  {
					
					System.out.println("\n--- Departure Flights Info: ---");

					/* Tell the user that there are # of available flights */
					iFace.numOfFlights(originFilter.size());

					/* Print all the available flights */
					for (Flight flight : originFilter) {
						iFace.printFlightOption(originFilter.indexOf(flight));
						flight.printFlight(userInfo.getIsFirstClass());
						System.out.println();
					}

					
					System.out.println("--- Arrival Flights Info: ---");

					/* Tell the user that there are # of available flights */
					iFace.numOfFlights(returnFilter.size());

					/* Print all the available flights */
					for (Flight flight : returnFilter) {
						iFace.printFlightOption(returnFilter.indexOf(flight));
						flight.printFlight(userInfo.getIsFirstClass());
						System.out.println();
					}

					return true;
					
				}
				/* There are no flights */
				else{
					/* Tell the user there are no flights */
					iFace.noFlights();
					return false; // No flights
				}
					
				
			}
		}
		
					
	}
		
	private boolean filterFlights() {
	
		boolean didFilter = false;
		
		/* Ask the user which list, if they chose a round trip */
		if (userInfo.getIsRoundTrip()) {
	
			/* Ask which flight list they want to filter */
			String depOrRetAns = iFace.askOriginOrReturn();
			/* Loop until the user inputs D or R */
			do {
	
				/* Tell the user to input Y or N */
				if (!validUserAns(depOrRetAns, "O", "R")) {
	
					ErrorHandler.invalidInput();
	
					/* Ask the user again if they want detail about the flight */
					depOrRetAns = iFace.askOriginOrReturn();
				}
	
			} while (!validUserAns(depOrRetAns, "O", "R"));
			
			/* They want to filter the origin destination times */
			if(depOrRetAns.startsWith("O")){
				didFilter = filterDepArr(originFlightList, userInfo.getDepartureAirport(), true);
				
				returnFilter.addAll(returnFlightList); 
				
			}
			/* The want to filter return flights */
			else{
				didFilter = filterDepArr(returnFlightList, userInfo.getArrivalAirport(), false);
				originFilter.addAll(originFlightList);
			}
			
		}
		/* it is a one-way trip, so only filter the departure */
		else{
			
			/* The original list has been filtered */
			didFilter = filterDepArr(originFlightList, userInfo.getDepartureAirport(), true);
	
		} // END OF ONE-WAY TRIP FILTER
		
		return didFilter;
		
	}

	/* Filters by departure or arrival time from a specific airport */
	private boolean filterDepArr(ArrayList<Flight> flightList, Airport airport, boolean origin) {
		
		boolean didFilter = false; // the return value
		
		FlightFilter depFilter = new FlightFilter(); // depart time filter
		FlightFilter arrFilter = new FlightFilter(); // arrival time filter
		
		/* ---------- DEPARTURE TIME FILTER ------------ */
	
		/* Ask the user if they want to filer by departure time */
		String filAns = iFace.askDepFilter();
	
		do {
	
			/* Tell the user to input Y or N */
			if (!validUserAns(filAns, "Y", "N")) {
	
				ErrorHandler.notYesOrNo();
	
				/* Ask the user again if they want detail about the flight */
				filAns = iFace.askDepFilter();
			}
	
		} while (!validUserAns(filAns, "Y", "N"));
	
		/* The user wants to filter by departure time */
		if (filAns.startsWith("Y")){
			
			didFilter = true;
			
			/* Get the time from the user */
			Time depTime = getUserInputTime(true);
	
			/* Ask them if they want the time before or after */
			String b4OrAfter = iFace.b4OrAfter();
	
			do {
	
				/* Tell the user to input Y or N */
				if (!validUserAns(b4OrAfter, "B", "A")) {
	
					ErrorHandler.invalidInput();
	
					/* Ask them if they want the time before or after */
					b4OrAfter = iFace.b4OrAfter();
				}
	
			} while (!validUserAns(b4OrAfter, "B", "A"));
	
			/* Add all the departure flights to the departure filter */
			depFilter.addFlightList(flightList);
	
			/* Filter by Departure flights by Before Departure time */
			if (b4OrAfter.startsWith("B")){
				
				if(origin){
					originFilter.clear(); // clean the list 1st.
					originFilter.addAll(depFilter.filterDepTime(depTime, false, airport));
				}
				else{
					returnFilter.clear();
					returnFilter.addAll(depFilter.filterDepTime(depTime, false, airport));
				}
				
	
			}
			/* Filter by Departure flights by After Departure time */
			else {
	
				if(origin){
					originFilter.clear(); // clean the list 1st.
					originFilter.addAll(depFilter.filterDepTime(depTime, true, airport));
				}
				else{
					returnFilter.clear(); // clean the list 1st.
					returnFilter.addAll(depFilter.filterDepTime(depTime, true, airport));
				}
				
			}
	
	
		} /* END ASKING FOR DEPARTURE TIME FILTER */
	
		/* ---------  ARRIVAL TIME FILTER  --------- */
	
		/* Ask the user if they want to filer by arrival time */
		filAns = iFace.askArrFilter();
	
		do {
	
			/* Tell the user to input Y or N */
			if (!validUserAns(filAns, "Y", "N")) {
	
				ErrorHandler.notYesOrNo();
	
				/* Ask the user again if they want detail about the flight */
				filAns = iFace.askArrFilter();
			}
	
		} while (!validUserAns(filAns, "Y", "N"));
	
		/* The user wants to filter by arrival time */
		if (filAns.startsWith("Y")){
	
			didFilter = true;
			
			/* Get the time from the user */
			Time arrTime = getUserInputTime(false);
	
			/* Ask them if they want the time before or after */
			String b4OrAfter = iFace.b4OrAfter();
	
			do {
	
				/* Tell the user to input Y or N */
				if (!validUserAns(b4OrAfter, "B", "A")) {
	
					ErrorHandler.invalidInput();
	
					/* Ask them if they want the time before or after */
					b4OrAfter = iFace.b4OrAfter();
				}
	
			} while (!validUserAns(b4OrAfter, "B", "A"));
	
			/* Add all the departure flights to the departure filter */
			arrFilter.addFlightList(flightList);
	
			/* Filter by Departure flights by Before Departure time */
			if (b4OrAfter.startsWith("B")){
				
				if(origin){
					originFilter.clear(); // clean the list 1st.
					originFilter.addAll(arrFilter.filterArrTime(arrTime, false, airport));
				}
				else{
					returnFilter.clear(); // clean the list 1st.
					returnFilter.addAll(arrFilter.filterArrTime(arrTime, false, airport));
				}
				
			}
			/* Filter by Departure flights by After Departure time */
			else {
				
				if(origin){
					originFilter.clear(); // clean the list 1st.
					originFilter.addAll(arrFilter.filterArrTime(arrTime, true, airport));
				}
				else{
					returnFilter.clear(); // clean the list 1st.
					returnFilter.addAll(arrFilter.filterArrTime(arrTime, true, airport));
				}
				
			}
	
		} // End asking user if they want to filter by arrival time
		return didFilter;
	}

	private boolean sortFlights() {
		
		boolean didSort = false;
		
		/* Ask the user which list, if they chose a round trip */
		if (userInfo.getIsRoundTrip()) {
			
			/* If the list has been filtered, 
			 * then use the filtered list for sorting */
			if(hasBeenFiltered){
				
				/* Ask the user if they want to sort the origin
				 * or the return flights */
				String sortOrgOrRetAns = iFace.sortOriginOrReturn();
				do {

					/* Tell the user to input Y or N */
					if (!validUserAns(sortOrgOrRetAns, "O", "R")) {

						ErrorHandler.invalidInput();

						/* Ask them if they want the time before or after */
						sortOrgOrRetAns = iFace.sortOriginOrReturn();
					}

				} while (!validUserAns(sortOrgOrRetAns, "O", "R"));
				
				/* If the user chose to sort the origin list */
				if(sortOrgOrRetAns.startsWith("O")){	
					if(askUserHowToSort(originFilter)){
						originFilter.clear();
						originFilter.addAll(sortList);
						didSort = true;
					}else{
						didSort = false;
					}
				}
				/* They chose to sort the Return list */
				else{
					if(askUserHowToSort(returnFilter)){
						returnFilter.clear();
						returnFilter.addAll(sortList);
						didSort = true;
					}else{
						didSort = false;
					}
				}
				
			}
			/* The list has not yet been filtered, use the original
			 * list for sorting */
			else{
				
				/* Ask the user if they want to sort the origin
				 * or the return flights */
				String sortOrgOrRetAns = iFace.sortOriginOrReturn();
				do {

					/* Tell the user to input Y or N */
					if (!validUserAns(sortOrgOrRetAns, "O", "R")) {

						ErrorHandler.invalidInput();

						/* Ask them if they want the time before or after */
						sortOrgOrRetAns = iFace.sortOriginOrReturn();
					}

				} while (!validUserAns(sortOrgOrRetAns, "O", "R"));
				
				/* If the user chose to sort the origin list */
				if(sortOrgOrRetAns.startsWith("O")){	
					if(askUserHowToSort(originFlightList)){
						originFlightList.clear();
						originFlightList.addAll(sortList);
						didSort = true;
					}else{
						didSort = false;
					}
				}
				/* They chose to sort the Return list */
				else{
					if(askUserHowToSort(returnFlightList)){
						returnFlightList.clear();
						returnFlightList.addAll(sortList);
						didSort = true;
					}
					else{
						didSort = false;
					}
					
				}
				
			}
			
		}
		/* It's a one way trip */
		else{
			
			/* If the list has been filtered, 
			 * then use the filtered list for sorting */
			if(hasBeenFiltered){
				
				if(askUserHowToSort(originFilter)){
					originFilter.clear();
					originFilter.addAll(sortList);
					
					didSort = true;
				}
				else{
					didSort = false;
				}
				
			}
			/* The list has not yet been filtered, use the original
			 * list for sorting */
			else{
				if(askUserHowToSort(originFlightList)){
					originFlightList.clear();
					originFlightList.addAll(sortList);
					
					didSort = true;
				}
				else{
					didSort = false;
				}
			}
			
		}
		
		return didSort;
		
	}
		

	private boolean askUserHowToSort(ArrayList<Flight> flightList) {

		/* Add the flights to the filter */
		FlightFilter filter = new FlightFilter(flightList);
		
		/* Default answer */
		boolean wasSorted = false;
		
		boolean validNumber = false;
		int sortOpt = 0;
		
		do {
			/* Integer that has the sort option */
			try {

				sortOpt = Integer.parseInt(iFace.sortBy());

				if (sortOpt < 1 || sortOpt > 6) {
					ErrorHandler.invalidSort();
				}
				else{
					validNumber = true;
				}
			
			/* Not a number */
			} catch (NumberFormatException e) {

				ErrorHandler.notANum();

			}
		} while (!validNumber);
		
		/* Ask the user if they want to sort by ascending or descending order */
		String ordAns = iFace.sortOrder();
		do {

			/* Tell the user to input Y or N */
			if (!validUserAns(ordAns, "A", "D")) {

				ErrorHandler.invalidInput();

				/* Ask them if they want the time before or after */
				ordAns = iFace.sortOriginOrReturn();
			}

		} while (!validUserAns(ordAns, "A", "D"));
		
		wasSorted = true;
		
		/* Clean the list that is used to hold the sorted flights */
		sortList.clear();
		
		/* Determine how to sort the list */
		switch (sortOpt){

		/* Departure time */
		case 1:

			/* Ascending */
			if(ordAns.startsWith("A")){
				sortList.addAll(filter.sortDepartTime(true));
			}
			/* Descending */
			else{
				sortList.addAll(filter.sortDepartTime(false));	
			}
			break;

		/* Arrival time */
		case 2:

			/* Ascending */
			if(ordAns.startsWith("A")){
				sortList.addAll(filter.sortArriveTime(true));
			}
			/* Descending */
			else{
				sortList.addAll(filter.sortArriveTime(false)); 	
			}
			break;

		/* Total Time */
		case 3:

			/* Ascending */
			if(ordAns.startsWith("A")){
				sortList.addAll(filter.sortTime(true));
			}
			/* Descending */
			else{
				sortList.addAll(filter.sortTime(false)); 	
			}
			break;

		/* Number of Connections */
		case 4:

			/* Ascending */
			if(ordAns.startsWith("A")){
				sortList.addAll(filter.sortConnect(true));
			}
			/* Descending */
			else{
				sortList.addAll(filter.sortConnect(false)); 	
			}
			break;

		/* Lay over Time */
		case 5:

			/* Ascending */
			if(ordAns.startsWith("A")){
				sortList.addAll(filter.sortLayover(true));
			}
			/* Descending */
			else{
				sortList.addAll(filter.sortLayover(false)); 	
			}
			break;

		/* Cost */
		case 6:	

			/* Ascending */
			if(ordAns.startsWith("A")){
				sortList.addAll(filter.sortPrice(true, userInfo.getIsFirstClass()));
			}
			/* Descending */
			else{
				sortList.addAll(filter.sortPrice(false, userInfo.getIsFirstClass())); 	
			}
			break;

		} // end of switch
		
		return wasSorted;
		
	}

	/* Asks the user if they want to get more detail 
	 * about a flight. If they do, then we print that info,
	 * if not, then we go to the buy flights state.
	 */
	private void showDetails() {
		
		/* Ask the user if they want to see more 
		 * detail about a flight */
		String detAns = iFace.wantDetail();
		
		/* Loop until the user inputs Y or N */
		do{
			
			/* Tell the user to input Y or N */
			if(!validUserAns(detAns, "Y", "N")){
				ErrorHandler.notYesOrNo();
			}
			
			/* Ask the user again if they want detail about the flight */
			detAns = iFace.wantDetail();
			
		}
		while(!validUserAns(detAns, "Y", "N"));
		
		/* if they don't want more detail about any of the flights */
		if (detAns.startsWith("N")){
			
			/* Then they know what flight they want to 
			
		}
			
			
		
		
		/* Get the flight option the user wants more info about */
		String fltOpt = iFace.getDetailFlight();
		
//		boolean intValid = false;
//		
//		/* Keep looping until the user inputs a 
//		do {
//			/* Try and convert the user input into a integer */
//			try {
//				Integer.parseInt(fltOpt);
//				intValid = true;
//			}
//			/* The user did not input a number */
//			catch (IndexOutOfBoundsException e) {
//				ErrorHandler.invalidFlight();
//				intValid = false;
//			}
////		} while (!intValid);
//
		
//
//		while (!validUserAns(detAns, "Y", "N"));
//		
//		boolean ansValid = false;
		
//		do {
//			/* Go back to user info */
//			if (detAns.startsWith("Y")) {
//				
//				/* Get the flight option the user wants
//				 * more info about */
//				String fltOpt = iFace.getDetailFlight();
//				
//				try {
//					int fltOptNum = Integer.parseInt(fltOpt);
//					
//					/* We know it's an integer */
//					try {
//						
//						/* Prints the detail info about the flight */
//						flightList.get(fltOptNum).printDetailFlight(userInfo.getIsFirstClass());
//						
//						String detAns2nd = iFace.getAnotherDetail();
//						
//						/* If they want detail about another flight */
//						if(detAns2nd.startsWith("Y")){
//							
//							ansValid = true;
//							
//							/* Call this function again */
//							showDetail();
//						}
//						else if (detAns2nd.startsWith("N")){
//							
//							ansValid = true;
//							/* Go the the buy state */
//							this.state = State.BuyFlights;
//						}
//						else{
//							/* Not a valid answer */
//							ErrorHandler.notYesOrNo();
//						}
//						
//					/* If the user tries to select an incorrect option */
//					} catch (IndexOutOfBoundsException e) {
//						ErrorHandler.invalidFlight();
//					}
//				
//				/* The user did not type a number */
//				} catch (NumberFormatException e) {
//					ErrorHandler.notANum();
//				}
//		
//			}
//			/* The user doesn't want any flight detail */
//			else if (detAns.startsWith("N")) {
//				
//				ansValid = true;
//		
//				/* Go the the buy state */
//				this.state = State.BuyFlights;
//			
//			} else {
//				/* Not a valid answer */
//				ErrorHandler.notYesOrNo();
//			}
//			
//		} while (!ansValid);
	
		
	}
	}

	private Time getUserInputTime(boolean depTime) {
		
		boolean validTime = false;
		
		Time filterTime = null; // the return value
		
		/* Ask the user what time they want to depart */
		iFace.askDepTime(depTime);
		
		do{
		/* Get the hour */
		String hourStr = iFace.getHours();
		String minsStr = iFace.getMinutes();
		
		/* Try to parse Hour */
			try {

				int hour = Integer.parseInt(hourStr);
				int mins = Integer.parseInt(minsStr);

				/* Check if the time could be valid */
				if ((hour > 24 || hour < 0) || (mins < 0 || mins > 59)) {
					ErrorHandler.invalidTime();
				} else {
					
					validTime = true;
					
					/* Make the time */
					filterTime = new Time(hour,mins);
					
				}

			} catch (NumberFormatException e) {
				ErrorHandler.invalidTime();
				
			}
		
		} while (!validTime);
		
		/* Make the time */
		return filterTime;
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
					
					fltOpt = originFlightList.get(fltNum - 1);
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
	
	/* Returns true if the user provides a valid answer */
	private boolean validUserAns(String userAns, String option1, String option2){
		
		/* If the user pick an appropriate option, then return true */
		return ( userAns.startsWith(option1) || userAns.startsWith(option2) );
			
	}
	
}
