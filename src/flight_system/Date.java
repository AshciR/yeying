//Name: Zhong Ren
//Date: March 18. 2015
//Description: Creates a Date Object for CS 509 Project

package flight_system;


public class Date {
	
	// The fields 
	private Month month;
	private int day;
	private int year;
	
	// The Constructor
	public Date(Month month, int day, int year){
		this.month = month;
		this.day = day;
		this.year = year;
	}
	
	// Getter Methods
	public Month getMonth(){
		return month;
	}

	public int getDay(){
		return day;
	}

	public int getYear(){
		return year;
	}

	@Override
	public String toString(){
		return "The date is: " + month + "/" + day + "/" + year;
	}
}