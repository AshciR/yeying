package flight_system;

import java.util.ArrayList;

public class FlightFilter {
	private ArrayList<Flight> flightFilter;

	public FlightFilter(){
		this.flightFilter =  new ArrayList<Flight>();
	}
	
	public void addFlight(Flight flight){
		this.flightFilter.add(flight);
	}
	
	public Flight cheapestFlight(){
		Filght cheapestFlight=new Flight();
		for (int i=0;i<Flight.length();i++){
			if(Flight[i].totalCostFlight()<cheapestFlight.totalCostFlight())
				cheapestFlight=Flight[i];
		}
		return cheapestFlight;
	}
	
	public Flight shortestFlightTime(){
		Filght shortestFlightTime=new Flight();
		for (int i=0;i<Flight.length();i++){
			if(Flight[i].totalTimeFlight()>shortestFlightTime.totalTimeFlight())
				shortestFlightTime=Flight[i];
		}
		return shortestFlightTime;
	}

	public Flight minLayover(){
		Filght minLayover=new Flight();
		for (int i=0;i<Flight.length();i++){
			if(Flight[i].totalLayoverTime()<minLayover.totalLayoverTime())
				minLayover=Flight[i];
		}
		return minLayover;
	}
	
	public Flight minConnections(){
		Filght minConnections=new Flight();
		for (int i=0;i<Flight.length();i++){
			if(Flight[i].numOfConnection()<minConnections.numOfConnection())
				minConnections=Flight[i];
		}
		return minConnections;
	}
	
	public boolean sortcheap(){
		Flight temp=new Flight();
		for (int i=0;i<Flight.length()-1;i++)
			for (int j=i+1;j<Flight.length();j++)
			{
				if(Flight[i].totalCostFlight()>Flight[j].totalCostFlight())
				{
					temp=Flight[i];
					Flight[i]=Flight[j];
					Flight[j]=temp;
				}
			}
		boolean k=true;
		for (int i=0;i<Flight.length()-1;i++)
		{
			for (int j=i+1;j<Flight.length();j++)
			if(Flight[i].totalCostFlight()<Flight[j].totalCostFlight())
			{
				k=false;
			    break;
		    }
		}
		return k;
	}
	
	public boolean sortshortest(){
		Flight temp=new Flight();
		for (int i=0;i<Flight.length()-1;i++)
			for (int j=i+1;j<Flight.length();j++)
			{
				if(Flight[i].totalTimeFlight()>Flight[j].totalTimeFlight())
				{
					temp=Flight[i];
					Flight[i]=Flight[j];
					Flight[j]=temp;
				}
			}
		boolean k=true;
		for (int i=0;i<Flight.length()-1;i++)
		{
			for (int j=i+1;j<Flight.length();j++)
			if(Flight[i].totalTimeFlight()<Flight[j].totalTimeFlight())
			{
				k=false;
			    break;
		    }
		}
			return k;
	}
	
	public boolean sortLayover(){
		Flight temp=new Flight();
		for (int i=0;i<Flight.length()-1;i++)
			for (int j=i+1;j<Flight.length();j++)
			{
				if(Flight[i].totalLayoverTime()>Flight[j].totalLayoverTime())
				{
					temp=Flight[i];
					Flight[i]=Flight[j];
					Flight[j]=temp;
				}
			}
		boolean k=true;
		for (int i=0;i<Flight.length()-1;i++)
		{
			for (int j=i+1;j<Flight.length();j++)
			if(Flight[i].totalLayoverTime()<Flight[j].totalLayoverTime())
			{
				k=false;
			    break;
		    }
		}
			return k;
	}
	
	public boolean sortConnect(){
		Flight temp=new Flight();
		for (int i=0;i<Flight.length()-1;i++)
			for (int j=i+1;j<Flight.length();j++)
			{
				if(Flight[i].numOfConnection()>Flight[j].numOfConnection())
				{
					temp=Flight[i];
					Flight[i]=Flight[j];
					Flight[j]=temp;
				}
			}
		boolean k=true;
		for (int i=0;i<Flight.length()-1;i++)
		{
			for (int j=i+1;j<Flight.length();j++)
			if(Flight[i].numOfConnection()<Flight[j].numOfConnection())
			{
				k=false;
			    break;
		    }
		}
			return k;
	}
	
	@Override
	public String toString() {
		return "the cheapest flight is: "+cheapestFlight+".\n"+
				"the shortest flight time is: "+shortestFlightTime+".\n"+
				"the minimum Layover is: "+minLayover+".\n"+
				"the minimum connections are: "+minConnections+".\n"+
				"FlightFilter [sortcheap()=" + sortcheap() + ", sortshortest()="
				+ sortshortest() + ", sortLayover()=" + sortLayover()
				+ ", sortConnect()=" + sortConnect() + "]";
	}
	
}