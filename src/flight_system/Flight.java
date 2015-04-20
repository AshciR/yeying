//Name: Zhong Ren
package flight_system;
import java.util.ArrayList;
import java.util.Scanner;


public class Flight
{
	private ArrayList<FlightLeg> flightList;
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
		int totoalLayoverMinutes = (this.getTotalTime().getTimeInMinutes());
		
		//print the duration of each flight leg into minutes
		for (int i = 0; i < this.flightList.size(); i++)
		{
			totoalLayoverMinutes = totoalLayoverMinutes - this.flightList.get(i).getFlightDuration();
			System.out.println("The " + (i + 1) + "th flightLeg's duration is: " + this.flightList.get(i).getFlightDuration());
		}
		
		//get the hours and minutes of the total layover time
		int hours = totoalLayoverMinutes / 60;
		int minutes = totoalLayoverMinutes % 60;
		Time totalLayoverTime = new Time(hours, minutes);
		
		return totalLayoverTime;
	}


	public Time getLayoverTime(int layoverIndex)
	{
		
		//give the return value of exception
		if(layoverIndex == 0)
		{
			Time zero = new Time(0, 0);
			return zero;
		}
		//calculate the layover time
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
		
			Time layoverTime = new Time(Hours, minutes);
			return layoverTime;
		}
		
		
	}

	public String toString()
	{
		return "The total time is: " + this.getTotalTime() + "./n"+
			   "The total cost is: " + this.getTotalCost(true) + "./n"+
			   "The number of connection are: " + this.getNumOfConnection() + "./n"+
			   "The total layover time is : " + this.getTotalLayoverTime() + "./n"+
			   "The layover time is for first connection : " + this.getLayoverTime(0) + "./n";
	}
}
