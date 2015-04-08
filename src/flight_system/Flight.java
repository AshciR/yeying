package flight_system;

import java.util.ArrayList;

public class Flight {
	//hold a list of FlightLegs
	private ArrayList<FlightLeg> flightList = new ArrayList<FlightLeg>();
	public int calculateTotalTime(FlightLeg leg){
		int Totaltime = 0;
		Totaltime = Totaltime + leg.getFlightDuration();
		return leg.getFlightDuration();
	}

}
