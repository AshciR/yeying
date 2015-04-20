/** Engineer: Daoheng guo
 * Date: March 23. 2015
 * Description: Class used to store the user's information.
 */
package flight_system;

public class UserInfo {
	private Airport departureAirport;
	private Airport arrivalAirport;
	private Date departureDate;
	private boolean isFirstClass;

	public UserInfo(Airport departureAirport, Airport arrivalAirport,
			Date departureDate, boolean isFirstClass) {
		this.departureAirport = departureAirport;
		this.arrivalAirport = arrivalAirport;
		this.departureDate =departureDate;
		this.isFirstClass = isFirstClass;
	}
	
	/* Setter Methods */
	public void setDepartureAirport(Airport departureAirport) {
		this.departureAirport = departureAirport;
	}

	public void setArrivalAirport(Airport arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public void setIsFirstClass(boolean isFirstclass) {
		this.isFirstClass = isFirstclass;
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

	public boolean getIsFirstClass() {
		return isFirstClass;
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
