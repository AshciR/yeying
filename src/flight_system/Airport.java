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
	private float latitude;
	private float longitude;
	
	/**
	 * @param code the 3 digit code of the airport
	 * @param name the official name for the airport
	 * @param latitude the latitude of the airport xx.xxxxx
	 * @param longitude the longitude of the airport xx.xxxxx
	 */
	/* The constructor */
	public Airport(String code, String name, float latitude, float longitude) {
		this.code = code;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/* The getter methods */
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "Airport [code=" + code + ", name=" + name + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}
	
	
}
