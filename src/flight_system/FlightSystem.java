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
		SortFights, DetailFlights, BuyFlights, FinishState
		
	};

	private ArrayList<Airport> airportList;
	private ArrayList<Airplane> airplaneList;
	private ArrayList<Flight> depFlightList, returnFlightList, filterFlights;
	private State state;
	private UserInfo userInfo;
	private IUserInterface iFace;

	/* The constructor */
	public FlightSystem(IUserInterface iFace) {
		this.airportList = new ArrayList<Airport>();
		this.airplaneList = new ArrayList<Airplane>();
		this.depFlightList = new ArrayList<Flight>();
		this.returnFlightList = new ArrayList<Flight>();
		this.filterFlights = new ArrayList<Flight>();
		this.userInfo = new UserInfo(null, null, null, null, false, false);
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

				/* Updates the user's info */
				askUserForInfo();
				
				/* Go to Show Flights State */
				state = State.ShowFlights;
				
				break;

			case ShowFlights:

				/* Gets the flights from the DB */
				getFlights(userInfo.getIsRoundTrip());

				/* Shows the user the flight(s) if available */
				
				/* If there are no flights */
				if(!showFlights()){
					
					/* Then go to finish state */
					state = State.FinishState;
					
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
					state = State.SortFights;
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
			
			case DetailFlights:
				
				/* Asks the user if they want to see the detail flight info */
				showDetails();
				break;
				
			case BuyFlights:
								
				/* Get the flight for the flight the user wants to buy */
				Flight fltOpt = getFltOpt(); 
				
				/* Show the user what flight they selected */
				iFace.userFlightChoice(depFlightList.indexOf(fltOpt) + 1);
				
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
		depFlightList.clear();
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
			depFlightList.add(addedFlight);
			
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

	private boolean showFlights() {

		/* Show the un-filtered list of flights ordered by price initially.
		 * Sort the price based on the user's seating choice. */
		if(userInfo.getIsFirstClass()){

			/* Sort flights by price (by default) */
			Collections.sort(depFlightList, Flight.FirstClassPriceComparator);
			
			if(userInfo.getIsRoundTrip()){
				/* Sort flights by price (by default) */
				Collections.sort(returnFlightList, Flight.FirstClassPriceComparator);
			}

		}
		else{

			/* Sort flights by price (by default) */
			Collections.sort(depFlightList, Flight.CoachClassPriceComparator);
			
			if(userInfo.getIsRoundTrip()){
				/* Sort flights by price (by default) */
				Collections.sort(returnFlightList, Flight.CoachClassPriceComparator);
			}

		}
		
		/* If it's not a round trip */
		if(!userInfo.getIsRoundTrip()){
			
			/* If there departure flights */
			if (depFlightList.size() != 0)  {
				
				System.out.println("\n--- Departure Flights Info: ---");
				
				/* Tell the user that there are # of available flights */
				iFace.numOfFlights(depFlightList.size());

				/* Print all the available flights */
				for (Flight flight : depFlightList) {
					iFace.printFlightOption(depFlightList.indexOf(flight));
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
			if (depFlightList.size() != 0 && returnFlightList.size() !=0 )  {
				
				System.out.println("\n--- Departure Flights Info: ---");

				/* Tell the user that there are # of available flights */
				iFace.numOfFlights(depFlightList.size());

				/* Print all the available flights */
				for (Flight flight : depFlightList) {
					iFace.printFlightOption(depFlightList.indexOf(flight));
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

	private void filterFlights() {
		
		/* Sort the flights before we filter them */
		sortFlights();
		
		/* Filter by time (if the user wants) */
		FlightFilter filter = new FlightFilter(depFlightList);
		
		/* Check if the user typed in correct answers */
		boolean validAns = false;
		
		do {
			
			/* Ask the user if they want to filer by departure time */
			String filAns = iFace.askDepFilter();
			
			/* They want to filter */
			if (filAns.startsWith("Y")) {
				
				/* Get the time from the user */
				Time depTime = getUserInputTime();
				
				boolean BOrA = false; // B Or A
				
				do{

					/* Ask the user if they want the departing flight to leave
					 * before of after the given time */
					String b4OrAfter = iFace.b4OrAfter();	

					if (b4OrAfter.startsWith("B")){
						BOrA = true;
						
						filterFlights.clear(); // clean the list 1st.
						
						filterFlights.addAll(filter.filterDepTime(depTime, false, userInfo.getDepartureAirport()));
					
					}
					else if (b4OrAfter.startsWith("A")){
						BOrA = true;
						filterFlights.clear(); // clean the list 1st.
						filterFlights.addAll(filter.filterDepTime(depTime, true, userInfo.getDepartureAirport()));
					}
					else{
						ErrorHandler.invalidInput();
					}

				} while(!BOrA);
				
				validAns = true;
				
			}
			/* They don't want to filter, go on to the
			 * show detail state */
			else if (filAns.startsWith("N")) {
				state = State.DetailFlights;
			} 
			/* Input is invalid */
			else {
				ErrorHandler.notYesOrNo();
			}

		} while (!validAns);
		validAns = false;
		ArrayList<Flight> arrFilterList = new ArrayList<Flight>();
		FlightFilter filterArr = new FlightFilter(filterFlights); 
		//choose arrival filter time
		do{
			/* Ask the user if they want to filer by arrival time */
			String filAns = iFace.askArrFilter();
			
			/* They want to filter */
			if (filAns.startsWith("Y")) {
				
				/* Get the time from the user */
				Time arrTime = getUserInputTime();
				
				boolean BOrA = false; // B Or A
				
				do{

					/* Ask the user if they want the arriving flight to arrive
					 * before of after the given time */
					String b4OrAfter = iFace.b4OrAfter();	

					if (b4OrAfter.startsWith("B")){
						BOrA = true;
						arrFilterList.clear();
						arrFilterList.addAll(filterArr.filterArrTime(arrTime, false, userInfo.getArrivalAirport()));
					
					}
					else if (b4OrAfter.startsWith("A")){
						BOrA = true;
						arrFilterList.clear();
						arrFilterList.addAll(filter.filterArrTime(arrTime, true, userInfo.getArrivalAirport()));
					}
					else{
						ErrorHandler.invalidInput();
					}

				} while(!BOrA);
				
				validAns = true;
				filterFlights.clear();
				filterFlights.addAll(arrFilterList);
			}
			/* They don't want to filter, go on to the
			 * show detail state */
			else if (filAns.startsWith("N")) {
				state = State.DetailFlights;
			} 
			/* Input is invalid */
			else {
				ErrorHandler.notYesOrNo();
			}

		}while(!validAns);
		
		state = State.DetailFlights;
	}

	private Time getUserInputTime() {
		
		boolean validTime = false;
		
		Time filterTime = null; // the return value
		
		/* Ask the user what time they want to depart */
		iFace.askDepTime();
		
		/* Get the hour */
		String hourStr = iFace.getHours();
		String minsStr = iFace.getMinutes();
		
		/* Try to parse Hour */
		do {
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

	private void sortFlights() {
	
		/* Clean the list each time we want to filter */
		filterFlights.clear();
		
		/* Ask if they want ascending or descending order */
		String ordAns = null;
		
		/* Add the flights to the filter */
		FlightFilter filter = new FlightFilter(depFlightList);
		
		/* Ask the user if they want to filter the flights */
		String ans = iFace.doFilter();
		boolean ansValid = false;
		
		do {
			
			/* Ask the user what they want to sort by */
			if (ans.startsWith("Y")) {
				

					/* Integer that has the sort option */
					int sortOpt = Integer.parseInt(iFace.sortBy());

					if (sortOpt < 1 || sortOpt > 6) {
						ErrorHandler.invalidSort();
					} 
					/* It's a valid sorting option, so let's sort
					 * accordingly */
					else {
						
						switch (sortOpt){

						/* Departure time */
						case 1:

							/* Ask if they want ascending or descending order */
							ordAns = iFace.sortOrder();

							/* Ascending */
							if(ordAns.startsWith("A")){
								filterFlights.addAll(filter.sortDepartTime(true));
								ansValid = true;
							}
							/* Descending */
							else if (ordAns.startsWith("D")){
								filterFlights.addAll(filter.sortDepartTime(false));
								ansValid = true;
							}
							else{
								ErrorHandler.invalidSortOrder();
							}
							break;

							/* Arrival time */
						case 2:

							/* Ask if they want ascending or descending order */
							ordAns = iFace.sortOrder();

							/* Ascending */
							if(ordAns.startsWith("A")){
								filterFlights.addAll(filter.sortArriveTime(true));
								ansValid = true;
							}
							/* Descending */
							else if (ordAns.startsWith("D")){
								filterFlights.addAll(filter.sortArriveTime(false)); 
								ansValid = true;
							}
							else{
								ErrorHandler.invalidSortOrder();
							}
							break;

							/* Total Time */
						case 3:

							/* Ask if they want ascending or descending order */
							ordAns = iFace.sortOrder();

							/* Ascending */
							if(ordAns.startsWith("A")){
								filterFlights.addAll(filter.sortTime(true)); 
								ansValid = true;
							}
							/* Descending */
							else if (ordAns.startsWith("D")){
								filterFlights.addAll(filter.sortTime(false)); 
								ansValid = true;
							}
							else{
								ErrorHandler.invalidSortOrder();
							}
							break;

							/* Number of Connections */
						case 4:

							/* Ask if they want ascending or descending order */
							ordAns = iFace.sortOrder();

							/* Ascending */
							if(ordAns.startsWith("A")){
								filterFlights.addAll(filter.sortConnect(true)); 
								ansValid = true;
							}
							/* Descending */
							else if (ordAns.startsWith("D")){
								filterFlights.addAll(filter.sortConnect(false));
								ansValid = true;
							}
							else{
								ErrorHandler.invalidSortOrder();
							}
							break;

							/* Lay over Time */
						case 5:

							/* Ask if they want ascending or descending order */
							ordAns = iFace.sortOrder();

							/* Ascending */
							if(ordAns.startsWith("A")){
								filterFlights.addAll(filter.sortLayover(true)); 
								ansValid = true;
							}
							/* Descending */
							else if (ordAns.startsWith("D")){
								filterFlights.addAll(filter.sortLayover(false));
								ansValid = true;
							}
							else{
								ErrorHandler.invalidSortOrder();
							}
							break;

							/* Cost */
						case 6:	

							/* Ask if they want ascending or descending order */
							ordAns = iFace.sortOrder();

							/* Ascending */
							if(ordAns.startsWith("A")){
								filterFlights.addAll(filter.sortPrice(true, userInfo.getIsFirstClass()));
								ansValid = true;
							}
							/* Descending */
							else if (ordAns.startsWith("D")){
								filterFlights.addAll(filter.sortPrice(false, userInfo.getIsFirstClass()));
								ansValid = true;
							}
							else{
								ErrorHandler.invalidSortOrder();
							}
							break;
						
						default:
							
							// will never get here.
							
							break;
							
						} // end of switch
					
					} // end of if						
				
			}
			else if (ans.startsWith("N")) {
				
				ansValid = true;
				
				/* Go to the Detail flight state */
				state = State.DetailFlights;
				
			}
			/* Tell the user to input yes or no */
			else{
				ErrorHandler.notYesOrNo();
			}
		 
		} while(!ansValid); // repeat until we get a valid ans.
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
					
					fltOpt = depFlightList.get(fltNum - 1);
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
	
	/* Returns true if the user provides a valid answer */
	private boolean validUserAns(String userAns, String option1, String option2){
		
		/* If the user pick an appropriate option, then return true */
		return ( userAns.startsWith(option1) || userAns.startsWith(option2) );
			
	}
	
}
