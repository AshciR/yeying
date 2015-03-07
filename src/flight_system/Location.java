/** Engineer: Richard Walker
 *  Date: March 7. 2015
 *  Description: Class responsible for holding the location of an object.
 *  			 Contains the latitude and longitude of a location, in 
 *  			 addition to a method to determine it's time zone.
 */

package flight_system;

public class Location {
	
	/* Fields */
	private double latitude;
	private double longitude;
	private String timeZone; // TODO -- Possibly a different type
	
	/**
	 * @param latitude the latitude of the location
	 * @param longitude the longitude of the location
	 */
	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/* Getters */
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public String getTimeZone() {
		return timeZone;
	}
	
	@SuppressWarnings("unused")
	private void determineTimeZone(){
		// TODO - IMPLEMENT LATER 
	}

	@Override
	public String toString() {
		return "This location is at latitude " + latitude + " and at longitude " + longitude;

	}
	
	
	
}
