package flight_system;

/** 
 * Class used to represent time in hours and minutes.
 * <p>
 * The class can either represent the time as GMT time or Local Time.
 * 
 * @author Daoheng Guo
 * @author Jianan Ou
 * @author Richard Walker
 *  
 */

public class Time {
	private int hours;
	private int minutes;
	
	
	public Time(int hours, int minutes) {
		this.hours = hours;
		this.minutes = minutes;
	}
	
	/* Returns a Time Object with the local time that was given */
	public static Time getLocalTime(Time time, Location location) {
		int localHours = (time.getHours() + (int) location.getTimeZoneOffset() / 3600) % 24;
		return new Time(localHours, time.getMinutes());
	}
	
	/**
	 * Returns a given time object as the 12-hour time.
	 * <p>
	 * Note: The time object that is returned doesn't specify whether
	 * the time is AM or PM. It is up to the client to gather that information
	 * from the original Time object.
	 * <p>
	 * @param time the time object to be converted from 24-hour format
	 * 		  to a 12-hour format. 
	 * @return the given time in a 12-hour format.  
	 */
	public static Time get12HourTime(Time time){
		
		return new Time(time.getHoursIn12(), time.getMinutes());
		
	}
	
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}
	/* this function returns the time in minutes */
	public int getTimeInMinutes(){
		return hours*60+minutes;
	}
	
	/* this function returns the hours in 12 hour format */
	public int getHoursIn12(){
		if(hours>12)
		{
			return hours-12;
		}
		else{
			return hours;
		}
	}
	/* this function returns true if it is AM */
	public boolean isAM(){
		return (hours < 12);
	}
	
	
	
	@Override
	public String toString() {
		/* Formats the time in HH:MM */
		return "The Time is: "+ String.format("%02d", hours) + ":" + String.format("%02d", minutes);
	}
}
