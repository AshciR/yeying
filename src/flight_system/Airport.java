/** Engineer: Richard Walker
 * Date: March 6. 2015
 * Description: Class used to represent an airport
 * 				based on the information provided by the
 * 				Database
 */

package flight_system;

public class Airport {
	
	/* The Fields */
	private String code;
	private String name;
	private Location location;
	
	/**
	 * @param code the airport's 3 letter code
	 * @param name the airport's name
	 * @param location the airport's location
	 */
	public Airport(String code, String name, Location location) {
		this.code = code;
		this.name = name;
		this.location = location;
	}
	
	/* The getter methods */
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}

	@Override
	public String toString() {
		return "Airport [code=" + code + ", name=" + name + ", location="
				+ location + "]";
	}


	
	
}
