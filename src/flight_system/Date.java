//Name: Zhong Ren
//Date: March 18. 2015
//Description: Creates a Date Object for CS 509 Project

package flight_system;


public class Date implements Comparable<Date> {
	
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
	
	/**
	 * Compares a given date object to this date object. 
	 * <p>
	 * If this date is after the compared date, then the result will be a positive integer.
	 * If this date is before the compared date, then the result will be a negative integer. 
	 * If this date is the same as the compared date, then the result will be 0.
	 * <p>
	 * @param compareDate the other date object to be compared to this date object. 
	 * @return a +ive or -ive integer, or 0.  
	 */
	@Override
	public int compareTo(Date compareDate) {
		
		/* If this year is after the compared year */
		if( year > compareDate.getYear() ){
			return 1;
		}
		else if (year <  compareDate.getYear() ){
			return -1;
		}
		/* It's the same year, let's compare the months */
		else{
			
			/* If this month is after the compared month */
			if (month.ordinal() > compareDate.getMonth().ordinal()){
				return 1;
			}
			else if (month.ordinal() < compareDate.getMonth().ordinal()){
				return -1;
			}
			/* It's the same month, let's compare the days */
			else{
				
				/* If this day is after the compared day */
				if (day > compareDate.getDay()){
					return 1;
				}
				else if (day < compareDate.getDay()){
					return -1;
				}
				/* It's the same year, month, and day. 
				 * therefore it has to be the same date. */
				else{
					return 0;
				}
				
			}
			
		}

	}

	@Override
	public String toString(){
		return "The date is: " + month + "/" + day + "/" + year;
	}
}