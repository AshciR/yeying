/** Engineer: Daoheng guo
 * Date: March 23. 2015
 * Description: Class used to store the user's information.
 */
package flight_system;

public class UserInfo {
	private String departureAirport;
	private String arrivalAirport;
	private Date departureDate;
	private boolean isFirstclass;
	
	public UserInfo(String departureAirport, String arrivalAirport,
			Date departureDate, boolean isFirstclass) {
		this.departureAirport = departureAirport;
		this.arrivalAirport = arrivalAirport;
		this.departureDate =departureDate;
		this.isFirstclass = isFirstclass;
	}

	public String getDepartureAirport() {
		return departureAirport;
	}

	public String getArrivalAirport() {
		return arrivalAirport;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public boolean isFirstclass() {
		return isFirstclass;
	}

	@Override
	public String toString() {
		return "UserInfo [departureAirport=" + departureAirport
				+ ", arrivalAirport=" + arrivalAirport + ", departureDate="
				+ departureDate + ", isFirstclass=" + isFirstclass + "]";
	}
	
	
}
