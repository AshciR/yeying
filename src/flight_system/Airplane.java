/** Engineer: Richard Walker
 * Date: March 6. 2015
 * Description: Class used to represent an airplane
 * 				based on the information provided by the
 * 				Database
 */

package flight_system;

public class Airplane {
	
	/* The Fields */
	private String model;
	private String manufacturer;
	private int firstClassSeats;
	private int coachSeats;
	
	/**
	 * @param model the model of the airplane i.e. "742"
	 * @param manufacturer the maker of the airplane i.e. "Airbus"
	 * @param firstClassSeats the number of first class seats
	 * @param coachSeats the number of coach seats
	 */
	public Airplane(String model, String manufacturer, int firstClassSeats,
			int coachSeats) {
		
		this.model = model;
		this.manufacturer = manufacturer;
		this.firstClassSeats = firstClassSeats;
		this.coachSeats = coachSeats;
	}

	/* The getter methods */
	public String getModel() {
		return model;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public int getFirstClassSeats() {
		return firstClassSeats;
	}

	public int getCoachSeats() {
		return coachSeats;
	}

	@Override
	public String toString() {
		return "Airplane [model=" + model + ", manufacturer=" + manufacturer
				+ ", firstClassSeats=" + firstClassSeats + ", coachSeats="
				+ coachSeats + "]";
	}	
}
