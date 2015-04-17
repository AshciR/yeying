package flight_system;

import java.util.ArrayList;
import java.util.Scanner;


public class Flight
{
	//Flight flight = new Flight();
	private ArrayList<FlightLeg> flightList;
	
	public Flight()
	{
		this.flightList = new ArrayList<FlightLeg>();
	}
	
	
	public Time getTotalTime(UserInfo userInfo)
	{
		Date beginDate = flightList.get(0).getDepartureDate();
		Date endDate = flightList.get(flightList.size() - 1).getArrivalDate();
		Time beginTime = flightList.get(0).getDepartureTime();
		Time endTime = flightList.get(flightList.size() - 1).getArrivalTime();
		
		int beginDay = beginDate.getDay();
		int endDay = endDate.getDay();
		int days = endDay - beginDay;
		
		int beginHour = beginTime.getHours();
		int endHour = endTime.getHours();
		int totalHours = endHour - beginHour + days * 24;
		
		int beginMinute = beginTime.getMinutes();
		int endMinute = endTime.getMinutes();
		int minutes = endMinute - beginMinute;
		if(minutes < 0)
		{
			totalHours = totalHours - 1;
			minutes = 60 + minutes;
		}
		
		Time totalTime = new Time(totalHours, minutes);
		return totalTime;
		//return totalTime.toString();
	}
	
	
	public double getTotalCost(UserInfo userInfo)
	{
		double totalCost = 0;
		for (FlightLeg flight : flightList)
		{
			if (userInfo.getIsFirstClass())
			{
				totalCost += flight.getFirstClassPrice();
			}
			else totalCost += flight.getCoachClassPrice();
		}
		return totalCost;
	}
	
	
	public int getNumOfConnection()
	{
		int numOfConnection;
		int numOfLocattions = this.flightList.size();
		if(numOfLocattions > 1)
		{
			numOfConnection = numOfLocattions - 2;
		}
		else numOfConnection = 0;
		return numOfConnection;
	}
	
	
	public Time gettotalLayoverTime(UserInfo userInfo)
	{
		int totoalFlyMinutes = 0;
		for (FlightLeg flight : flightList)
		{
			totoalFlyMinutes += flight.getFlightDuration();
		}
		int totalFlyHour = totoalFlyMinutes / 60;
		int flyMinute = totoalFlyMinutes - totalFlyHour * 60;
		int layoverHour = this.getTotalTime(userInfo).getHours() - totalFlyHour;
		int layoverMinute = this.getTotalTime(userInfo).getMinutes() - flyMinute;
		
		Time totalLayoverTime = new Time(layoverHour, layoverMinute);
		return totalLayoverTime;
	}
	
	
	public Time getLayoverTime(UserInfo userInfo)
	{
		System.out.println("Please enter the number of transform station you want to check: ");
		Scanner.in = new Scanner(System.in);
		int num = in.nextInt();
		
		/*if(flightList.size() < 3)
		{
			Time layoverTime = new Time(0, 0);
		}
		if(flightList.size() <4)
		{
			Time layoverTime = this.gettotalLayoverTime(userInfo);
		}
		else{*/
			Date transferADate1 = flightList.get(num - 1).getArrivalDate();
			Date transferDDate1 = flightList.get(num).getDepartureDate();
			int transferDays1 = transferADate1.getDay() - transferDDate1.getDay();
			
			Time transferATime1 = flightList.get(num - 1).getArrivalTime();
			Time transferDTime1 = flightList.get(num).getDepartureTime();
			int hours1 = transferATime1.getHours() - transferDTime1.getHours();
			int transferHours1 = transferDays1 * 24 + hours1;
			
			int minutes1 = transferATime1.getMinutes() - transferDTime1.getMinutes();
			if (minutes1 < 0)
			{
				transferHours1 = transferHours1 - 1;
				minutes1 = 60 + minutes1;
			}
			
			/*Date transferADate2 = flightList.get(1).getArrivalDate();
			Date transferDDate2 = flightList.get(2).getDepartureDate();
			int transferDays2 = transferADate2.getDay() - transferDDate2.getDay();
			
			Time transferATime2 = flightList.get(1).getArrivalTime();
			Time transferDTime2 = flightList.get(2).getDepartureTime();
			int hours2 = transferATime2.getHours() - transferDTime2.getHours();
			int transferHours2 = transferDays2 * 24 + hours2;
			
			int minutes2 = transferATime2.getMinutes() - transferDTime2.getMinutes();
			if (minutes2 < 0)
			{
				transferHours2 = transferHours2 - 1;
				minutes2 = 60 + minutes2;
			}
			}
			Time layoverTime2 = new Time(transferHours2, minutes2);
			String layoverTime = "The first layover time will be: " + layoverTime1 + "/nThe second layover time will be: " + layoverTime2;
			*/
			
			Time layoverTime1 = new Time(transferHours1, minutes1);
			return layoverTime1;
	}
	
	public String toString(UserInfo userInfo)
	{
		return "The total time is: " + this.getTotalTime(userInfo) + "./n"+
			   "The total cost is: " + this.getTotalCost(userInfo) + "./n"+
			   "The number of connection are: " + this.getNumOfConnection() + "./n"+
			   "The total layover time is : " + this.gettotalLayoverTime(userInfo) + "./n"+
			   "The layover time is : " + this.getLayoverTime(userInfo) + "./n";
	}
	
}
