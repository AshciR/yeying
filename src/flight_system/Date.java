package flight_system;

public class Date {
	private int month;
	private int day;
	private int year;

	public Date(int month, int day, int year){
		this.month = month;
		this.day = day;
		this.year = year;
	}

	public int getmonth(){
		return month;
	}

	public int getday(){
		return day;
	}

	public int getyear(){
		return year;
	}

	@Override
	public String toString(){
		return "The date is: " + month + "/" + day + "/" + year;
	}
}