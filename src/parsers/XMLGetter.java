/**
 * Originally written by: Jianan 
 * Modified by: Richard Walker
 * Original date: March 15. 2015
 * Modified date: March 15. 2015
 * 
 */

package parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import flight_system.Airport;
import flight_system.Date;
import flight_system.Location;
import flight_system.Month;


public class XMLGetter {
	private String teamName = "TeamYeYing"; // Team Name
	private int numXML; // Number of XMLs 
	private String urlAddress = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";
	
	private static XMLGetter firstInstance = null;
	
	/* The private constructor */
	private XMLGetter(){};
	
	/* Method to get the only instance of the class */
	public static XMLGetter getInstance(){
		if(firstInstance == null){
			firstInstance = new XMLGetter();
		}

		return firstInstance;
	}
		
	/* Getter Functions*/
	public String getTeamName() {
		return teamName;
	}

	public int getNumXML() {
		return numXML;
	}
	
	/* Returns the XML for the Airports */
	public String getAirportsXML (){
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		
		try{
			url = new URL(urlAddress + "?team="+teamName+"&action=list&list_type=airports");
				
			/* Open Connection and send GET request */
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			/* The response code given by the server*/
			int responseCode = connection.getResponseCode(); 
			//System.out.println("\nThe Response Code is: " + responseCode);
			
			/* If The connection was successful */
			if((responseCode >= 200) && (responseCode <= 299)){
				
				System.out.println("Getting Airport Info..."); // Shows successful connection
				
				/* Setup the input stream */
				InputStream inputStream = connection.getInputStream();
				String encoding = connection.getContentEncoding();
				encoding = (encoding == null ? "URF-8" : encoding);
				
				/* This code just copies the String from the Server */
				reader = new BufferedReader(new InputStreamReader(inputStream));
				while ((line = reader.readLine()) != null){
					result.append(line);
				}
				reader.close();
				
				/* Increment XML Count by one */
				this.numXML++; 
				
			}
			/* Else the response was not valid */
			else if (responseCode == 403){
				System.out.println("Invalid team name!");
			}
			else if (responseCode == 400){
				System.out.println("Missing or Invalid action");
			}
			else{
				System.out.println("Unknown connection error");
			}
		}	
			
			/* Needs to catch these exceptions */
			catch(IOException e){
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			
			return result.toString();
	}

	public  String getAirplaneXML (){
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		
		try{
			url = new URL(urlAddress + "?team="+ teamName + "&action=list&list_type=airplanes");
				
			/* Open Connection and send GET request */
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			/* The response code given by the server*/
			int responseCode = connection.getResponseCode(); 
			//System.out.println("\nThe Response Code is: " + responseCode);
			
			/* If The connection was successful */
			if((responseCode >= 200) && (responseCode <= 299)){
				
				System.out.println("Getting Airplane Info..."); // Shows successful connection
				
				/* Setup the input stream */
				InputStream inputStream = connection.getInputStream();
				String encoding = connection.getContentEncoding();
				encoding = (encoding == null ? "URF-8" : encoding);
				
				/* This code just copies the String from the Server */
				reader = new BufferedReader(new InputStreamReader(inputStream));
				while ((line = reader.readLine()) != null){
					result.append(line);
				}
				reader.close();
				
				/* Increment XML Count by one */
				this.numXML++; 
				
			}
			/* Else the response was not valid */
			else if (responseCode == 403){
				System.out.println("Invalid team name!");
			}
			else if (responseCode == 400){
				System.out.println("Missing or Invalid action");
			}
			else{
				System.out.println("Unknown connection error");
			}
		}	
			
			/* Needs to catch these exceptions */
			catch(IOException e){
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			
			return result.toString();
	}

	public  String getFlightsXML (boolean depart, Airport airport, Date date){
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		
		/* Variables used in the URL address */
		String code = airport.getCode();
		int year = date.getYear();
		int day = date.getDay();
		Month m = date.getMonth();
		int month = m.ordinal() + 1;
		String type = "departing";
		
		/* If depart is not true, then set type to arriving */
		if (!depart){
			type = "arriving";
		}
		
		try{
			url = new URL(urlAddress + "?team="+ teamName + "&action=list&list_type="+ type +"&airport=" + code + "&day=" + year + "_" + month + "_" + day);
				
			/* Open Connection and send GET request */
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			/* The response code given by the server*/
			int responseCode = connection.getResponseCode(); 
			//System.out.println("\nThe Response Code is: " + responseCode);
			
			/* If The connection was successful */
			if((responseCode >= 200) && (responseCode <= 299)){
				
				System.out.println("Getting Flight Info..."); // Shows successful connection
				
				/* Setup the input stream */
				InputStream inputStream = connection.getInputStream();
				String encoding = connection.getContentEncoding();
				encoding = (encoding == null ? "URF-8" : encoding);
				
				/* This code just copies the String from the Server */
				reader = new BufferedReader(new InputStreamReader(inputStream));
				while ((line = reader.readLine()) != null){
					result.append(line);
				}
				reader.close();
				
				/* Increment XML Count by one */
				this.numXML++; 
				
			}
			/* Else the response was not valid */
			else if (responseCode == 403){
				System.out.println("Invalid team name!");
			}
			else if (responseCode == 400){
				System.out.println("Missing or Invalid action");
			}
			else{
				System.out.println("Unknown connection error");
			}
		}	
			
			/* Needs to catch these exceptions */
			catch(IOException e){
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}

			return result.toString();
	}
	
	/* Resets the Database */
	public boolean resetDB(){
		URL url;
		HttpURLConnection connection;
		boolean wasReset = false; // Returns true if successful reset

		try{
			url = new URL(urlAddress + "?team=TeamYeYing&action=resetDB");

			/* Open Connection and send GET request */
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			/* The response code given by the server*/
			int responseCode = connection.getResponseCode(); 
			System.out.println("\nThe Response Code is: " + responseCode);

			/* If The connection was successful */
			if((responseCode >= 200) && (responseCode <= 299)){
				System.out.println("Resetting the Database."); // Shows successful connection
				wasReset = true;
			}
			/* Else the response was not valid */
			else if (responseCode == 403){
				System.out.println("Invalid team name!");
			}
			else if (responseCode == 400){
				System.out.println("Missing or Invalid action");
			}
			else{
				System.out.println("Unknown connection error");
			}
		}	

		/* Needs to catch these exceptions */
		catch(IOException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return wasReset;
	}
	/**
	 * This method can get the time zone info from google server
	 * @param location
	 * @return XML string which include time zone info
	 */
	public String getTimeZoneXML (Location location){
		URL url;
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		//This is the date of May 8th 2015 at 00:00 GMT
		int timeStamp = 1431043200;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		
		try{
			url = new URL("https://maps.googleapis.com/maps/api/timezone/xml?location="+latitude+","+longitude+"&timestamp="+timeStamp);
				
			/* Open Connection and send GET request */
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			/* The response code given by the server*/
			int responseCode = connection.getResponseCode(); 
			//System.out.println("\nThe Response Code is: " + responseCode);
			
			/* If The connection was successful */
			if((responseCode >= 200) && (responseCode <= 299)){
				
				System.out.println("Getting TimeZone Info..."); // Shows successful connection
				
				/* Setup the input stream */
				InputStream inputStream = connection.getInputStream();
				String encoding = connection.getContentEncoding();
				encoding = (encoding == null ? "URF-8" : encoding);
				
				/* This code just copies the String from the Server */
				reader = new BufferedReader(new InputStreamReader(inputStream));
				while ((line = reader.readLine()) != null){
					result.append(line);
				}
				reader.close();
				
				/* Increment XML Count by one */
				this.numXML++; 
				
			}
			/* Else the response was not valid */
			else{
				System.out.println("Some error occured");
			}
		}	
			
			/* Needs to catch these exceptions */
			catch(IOException e){
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			
			return result.toString();
	}

	public String toString() {
		return "This is an XMLGetter that has the team name: " + teamName + " and it has gotten " + numXML + " XML file(s)";
	}

}

