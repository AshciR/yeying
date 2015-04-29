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
		GetUserInfo, ShowFlights, BuyFlights, FinishState, DetailFlights
	};

	private ArrayList<Airport> airportList;
	private ArrayList<Airplane> airplaneList;
	private ArrayList<Flight> flightList, filterFlights;
	private State state;
	private UserInfo userInfo;
	private IUserInterface iFace;
	private boolean showOriginalList = true;

	/* The constructor */
	public FlightSystem(IUserInterface iFace) {
		this.airportList = new ArrayList<Airport>();
		this.airplaneList = new ArrayList<Airplane>();
		this.flightList = new ArrayList<Flight>();
		this.filterFlights = new ArrayList<Flight>();
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
				showFlights(showOriginalList);

				/* Filter the flights */
				filterFlights();

				showFlights(showOriginalList);
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

	private void filterFlights() {

		/* Sort the flights before we filter them */
		sortFlights();

		/* Filter by time (if the user wants) */
		FlightFilter filter = new FlightFilter(filterFlights);

		/* Check if the user typed in correct answers */
		boolean validAns = false;

		do {

			/* Ask the user if they want to filer by departure time */
			String filAns = iFace.askDepFilter();

			/* They want to filter */
			if (filAns.startsWith("Y")) {

				/* Get the time from the user */
				Time depTime = getUserInputTime();
				showOriginalList = false;

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
				validAns = true;
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
				showOriginalList = false;

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
				//clean filterFlight list to put the arrFilterList in it
				filterFlights.clear();
				filterFlights.addAll(arrFilterList);
			}
			/* They don't want to filter, go on to the
			 * show detail state */
			else if (filAns.startsWith("N")) {
				state = State.DetailFlights;
				validAns = true;
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

		do{
		/* Ask the user what time they want to depart */
		iFace.askDepTime();

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

	private void sortFlights() {


		/* Ask if they want ascending or descending order */
		String ordAns = null;

		/* Add the flights to the filter */
		FlightFilter filter = new FlightFilter(flightList);

		/* Ask the user if they want to filter the flights */
		String ans = iFace.doSort();
		boolean ansValid = false;

		do {

			/* Ask the user what they want to sort by */
			if (ans.startsWith("Y")) {

				/* Clean the list each time we want to filter */
				filterFlights.clear();
				showOriginalList = false;


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
					
					if(showOriginalList == true){

					fltOpt = flightList.get(fltNum - 1);
					validFlight = true;
					}
					else{
							fltOpt = filterFlights.get(fltNum - 1);
							validFlight = true;

					}
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

	private void showFlights(boolean showOriginal) {

		//decide whether to show filtered flights or original flights
		if(showOriginal){
			/* Chooses which seating to sort by initially */
			if(userInfo.getIsFirstClass()){
				/* Sort flights by price (by default) */
				Collections.sort(flightList, Flight.FirstClassPriceComparator);
			}
			else{
				/* Sort flights by price (by default) */
				Collections.sort(flightList, Flight.CoachClassPriceComparator);
			}


			/* If there flights */
			if (flightList.size() != 0) {

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
		else{
			/* Chooses which seating to sort by initially */
			if(userInfo.getIsFirstClass()){
				/* Sort flights by price (by default) */
				Collections.sort(filterFlights, Flight.FirstClassPriceComparator);
			}
			else{
				/* Sort flights by price (by default) */
				Collections.sort(filterFlights, Flight.CoachClassPriceComparator);
			}


			/* If there flights */
			if (filterFlights.size() != 0) {

				/* Tell the user that there are # of available flights */
				iFace.numOfFlights(filterFlights.size());

				/* Print all the available flights */
				for (Flight flight : filterFlights) {
					iFace.printFlightOption(filterFlights.indexOf(flight));
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


	}

	private void getFlights() {

		/* Empty the list each time we're going to get new flight data */
		flightList.clear();

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
