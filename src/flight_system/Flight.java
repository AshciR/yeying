/*Name: Zhong Ren */

package flight_system;

import java.util.ArrayList;

public class Flight
{
	private ArrayList<FlightLeg> flightList;
	
	/**
	 * Makes a Flight object without any Flight Legs
	 * @see FlightLeg 
	 */
	public Flight(){
		this.flightList = new ArrayList<FlightLeg>();
	}
	
	public Flight(FlightLeg flight1)
	{
		this.flightList = new ArrayList<FlightLeg>();
		this.flightList.add(flight1);
	}
	
	public Flight(FlightLeg flight1, FlightLeg flight2)
	{
		this.flightList = new ArrayList<FlightLeg>();
		this.flightList.add(flight1);
		this.flightList.add(flight2);
	}
	
	public Flight(FlightLeg flight1, FlightLeg flight2, FlightLeg flight3)
	{
		this.flightList = new ArrayList<FlightLeg>();
		this.flightList.add(flight1);
		this.flightList.add(flight2);
		this.flightList.add(flight3);
	}
	
	/**
	 * Adds a FlightLeg object to this flight list.
	 * @see FlightLeg 
	 */
	public void addFlightLeg(FlightLeg flightLeg){
		this.flightList.add(flightLeg);
	}
	
	public Time getTotalTime()
	{	//get the days between the beginning and the end of travel
		int dday = flightList.get(0).getDepartureDate().getDay();
		int aday = flightList.get(flightList.size() - 1).getArrivalDate().getDay();
		int days = aday - dday;
		
		//get the hours between the beginning and the end of travel
		Time beginTime = flightList.get(0).getDepartureTime();
		Time endTime = flightList.get(flightList.size() - 1).getArrivalTime();
		int beginHour = beginTime.getHours();
		int endHour = endTime.getHours();
		int Hours = endHour - beginHour + days * 24;
		
		//get the minutes between the beginning and the end of travel	
		int beginMinute = beginTime.getMinutes();
		int endMinute = endTime.getMinutes();
		int minutes = endMinute - beginMinute;
		if(minutes < 0)
		{
			Hours = Hours - 1;
			minutes = 60 + minutes;
		}
		
		
		Time totalTime = new Time(Hours, minutes);	
		return totalTime;
	}

	public double getTotalCost(boolean isFirstClass)
	{	
		double totalCost = 0;
		int i;
		for(i = 0; i < this.flightList.size(); i++)
		{	//judge if the ticket is first class or coach lass
			if(isFirstClass)
			{
				totalCost = totalCost + this.flightList.get(i).getFirstClassPrice();
			}
			else{
				totalCost = totalCost + this.flightList.get(i).getCoachClassPrice();
			}
		}
		return totalCost;
	}
		
	public int getNumOfConnection()
	{
		int numOfConnection = this.flightList.size() - 1;
		return numOfConnection;
	}
	
	public Time getTotalLayoverTime()
	{
		//transform the hours and minutes into minutes
		int totalLayoverMinutes = (this.getTotalTime().getTimeInMinutes());
		
		//print the duration of each flight leg into minutes
		for (int i = 0; i < this.flightList.size(); i++)
		{
			totalLayoverMinutes = totalLayoverMinutes - this.flightList.get(i).getFlightDuration();
			//System.out.println("The " + (i + 1) + " flightLeg's duration is: " + this.flightList.get(i).getFlightDuration());
		}
		
		//get the hours and minutes of the total lay over time
		int hours = totalLayoverMinutes / 60;
		int minutes = totalLayoverMinutes % 60;
		Time totalLayoverTime = new Time(hours, minutes);
		
		return totalLayoverTime;
	}


	public Time getLayoverTime(int layoverIndex)
	{
		
		/* If you ask for a lay over time 
		 * that's more than the size of the list,
		 * return 0 minutes */
		if(layoverIndex >= (flightList.size()-1))
		{
			return new Time(0, 0);
		}
		//calculate the lay over time
		else 
		{
			Date transferADate = flightList.get(layoverIndex).getArrivalDate();
			Date transferDDate = flightList.get(layoverIndex + 1).getDepartureDate();
			Time AtransferTime = flightList.get(layoverIndex).getArrivalTime();
			Time transferTimeD = flightList.get(layoverIndex + 1).getDepartureTime();
		
			int transferDays = transferDDate.getDay() - transferADate.getDay();
			int transferHours = transferTimeD.getHours() - AtransferTime.getHours();
		
			int Hours = transferDays * 24 + transferHours;
			int minutes = transferTimeD.getMinutes() - AtransferTime.getMinutes();
			if (minutes < 0)
			{
				Hours = Hours - 1;
				minutes = 60 + minutes;
			}
		
			return new Time(Hours, minutes);
			
		}
		
		
	}

	public String toString()
	{
		return "The total time is: " + getTotalTime() + "\n"+
			   "The total cost for First Class is: " + getTotalCost(true) + "\n"+
			   "The total cost for Coach Class is: " + getTotalCost(false) + "\n"+
			   "The number of connection are: " + getNumOfConnection() + "\n"+
			   "The total layover time is " + getTotalLayoverTime() + "\n"+
			   "The layover time for 1st connection is: " + getLayoverTime(0) + "\n"+
			   "The layover time for the 2nd connection is: " + getLayoverTime(1);
	}
}
