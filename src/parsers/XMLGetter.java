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


public class XMLGetter {
	private String teamName = "TeamYeYing"; // Team Name
	private int numXML; // Number of XMLs 
	private String urlAddress = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";
	
	/* Constructor */
	public XMLGetter() {
		
	}
	
	/* Getter Functions*/
	public String getTeamName() {
		return teamName;
	}

	public int getNumXML() {
		return numXML;
	}
	
	/* Returns the XML for the Airports */
	public  String getAirportsXML (){
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
			System.out.println("\nThe Response Code is: " + responseCode);
			
			/* If The connection was successful */
			if((responseCode >= 200) && (responseCode <= 299)){
				
				System.out.println("Connection Sucessful!"); // Shows successful connection
				
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

	
	@Override
	public String toString() {
		return "This is an XMLGetter that has the team name: " + teamName + " and it has gotten " + numXML + " XML file(s)";
	}


}
