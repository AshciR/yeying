/** Engineer: Daoheng guo
 * Date: March 23. 2015
 * Description: Class used to store the user's information.
 */
package flight_system;

public class UserInfo {
	private Airport departureAirport;
	private Airport arrivalAirport;
	private Date departureDate;
	private Date returnDate;
	private boolean isFirstClass;
	private boolean isRoundTrip;

	public UserInfo(Airport departureAirport, Airport arrivalAirport,
			Date departureDate, Date returnDate, boolean isFirstClass, boolean isRoundTrip) {
		
		this.departureAirport = departureAirport;
		this.arrivalAirport = arrivalAirport;
		this.departureDate = departureDate;
		this.returnDate = returnDate;
		this.isFirstClass = isFirstClass;
		this.isRoundTrip = isRoundTrip;
	}
	
	/* Setter Methods */
	public void setDepartureAirport(Airport departureAirport) {
		this.departureAirport = departureAirport;
	}

	public void setArrivalAirport(Airport arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public void setIsFirstClass(boolean isFirstClass) {
		this.isFirstClass = isFirstClass;
	}

	public void setIsRoundTrip(boolean isRoundTrip){
		this.isRoundTrip = isRoundTrip;
	}

	/* Getter Methods */
	public Airport getDepartureAirport() {
		return departureAirport;
	}

	public Airport getArrivalAirport() {
		return arrivalAirport;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}
	
	public boolean getIsFirstClass() {
		return isFirstClass;
	}
	
	public boolean getIsRoundTrip() {
		return isRoundTrip;
	}
	
	public void printUserInfo(){
		System.out.println("\nDeparture Date: " + departureDate);
		System.out.println("Departure Airport: " + departureAirport.getCode());
		System.out.println("Arrival Airport: " + arrivalAirport.getCode());
		
		if(isRoundTrip){
			System.out.println("Return Date: " + returnDate);
			System.out.println("Departure Airport: " + arrivalAirport.getCode());
			System.out.println("Arrival Airport: " + departureAirport.getCode());
		}
			
		System.out.println("Class: " + (isFirstClass ? "First Class": "Coach"));
		System.out.println();
	}
	
	@Override
	public String toString() {

		String seatlevel;

		if(isFirstClass){
			seatlevel="first class";
		}
		else{
			seatlevel="coach";
		}

		return "This User will be leaving from "+departureAirport.getName()+", and arrives at " + arrivalAirport.getName() + " on "
		+ departureDate + ". The seat for this user is a " + seatlevel + " seat.";
	}


}
