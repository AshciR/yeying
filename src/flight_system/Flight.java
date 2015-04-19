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
	{
		int dday = flightList.get(0).getDepartureDate().getDay();
		int aday = flightList.get(flightList.size() - 1).getArrivalDate().getDay();
		int days = aday - dday;
		
		Time beginTime = flightList.get(0).getDepartureTime();
		Time endTime = flightList.get(flightList.size() - 1).getArrivalTime();
		int beginHour = beginTime.getHours();
		int endHour = endTime.getHours();
		int Hours = endHour - beginHour + days * 24;
			
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

	public double getTotalCost(UserInfo userInfo)
	{
		double totalCost = 0;
		int i;
		for(i = 0; i < this.flightList.size(); i++)
		{
			if(userInfo.getIsFirstClass())
			{
			totalCost = totalCost + this.flightList.get(i).getFirstClassPrice();
			}
			else totalCost = totalCost + this.flightList.get(i).getCoachClassPrice();
		}
		return totalCost;
	}
		
	public int getNumOfConnection()
	{
		int numOfConnection = this.flightList.size() - 1;
		return numOfConnection;
	}
	
	public Time gettotalLayoverTime()
	{
		int totoalLayoverMinutes = (this.getTotalTime().getHours() * 60) + this.getTotalTime().getMinutes();
		for (int i = 0; i < this.flightList.size(); i++)
		{
			totoalLayoverMinutes = totoalLayoverMinutes - this.flightList.get(i).getFlightDuration();
			System.out.println("The " + (i + 1) + "th flightLeg's duration is: " + this.flightList.get(i).getFlightDuration());
		}
		int hours = totoalLayoverMinutes / 60;
		int minutes = totoalLayoverMinutes % 60;
		Time totalLayoverTime = new Time(hours, minutes);
		
		return totalLayoverTime;
	}


	public Time getLayoverTime()
	{
		System.out.println("Please enter the serie number of the transit airport you want to check: ");
		Scanner in = new Scanner(System.in);
		int num = in.nextInt();

		if(num == 0)
		{
			Time zero = new Time(0, 0);
			return zero;
		}
		
		if(num > 0 && num < flightList.size())
		{
			Date transferADate = flightList.get(num - 1).getArrivalDate();
			Date transferDDate = flightList.get(num).getDepartureDate();
			Time AtransferTime = flightList.get(num - 1).getArrivalTime();
			Time transferTimeD = flightList.get(num).getDepartureTime();
		
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
		
		else 
		{
			System.out.println("Please enter the correct number of transform station");
			Time zero = new Time(0, 0);
			return zero;
		}
		
	}

	public String toString(UserInfo userInfo)
	{
		return "The total time is: " + this.getTotalTime() + "./n"+
			   "The total cost is: " + this.getTotalCost(userInfo) + "./n"+
			   "The number of connection are: " + this.getNumOfConnection() + "./n"+
			   "The total layover time is : " + this.gettotalLayoverTime() + "./n"+
			   "The layover time is : " + this.getLayoverTime() + "./n";
	}
}
