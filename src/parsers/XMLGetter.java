import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class XMLGetter {
	private String teamName = "TeamYeYing"; // Team Name
	private int numXML; // Number of XMLs 
	private String urlAddress = "http://cs509.cs.wpi.edu:8181/CS509Server/ReservationSystem";
	
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
			System.out.println(url+"\n");
			
			connection = (HttpURLConnection) url.openConnection();
			//connection.setRequestProperty("User-Agent", ticketAgency);
			connection.setRequestMethod("GET");
			
			int responseCode = connection.getResponseCode(); // The response code given by the server
			System.out.println("The Response Code is:" + responseCode);
			
			/* The connection was successful */
			if((responseCode >= 200)&&(responseCode <= 299)){
				
				System.out.println("Connection Sucessful!");
				
				InputStream inputStream = connection.getInputStream();
				String encoding = connection.getContentEncoding();
				encoding = (encoding == null ? "URF-8" : encoding);
				/* This code just copies the String from the Server */
				reader = new BufferedReader(new InputStreamReader(inputStream));
				while ((line = reader.readLine()) != null){
					result.append(line);
				}
				reader.close();
				
				this.numXML++; // Increment XML Count
				
			}
			/* The team name was invalid */
			else if (responseCode == 403){
				System.out.println("Wrong team name!");
			}
			else if (responseCode == 400){
				System.out.println("missing or invalid action");
			}
			//not successful connection
			else{
				System.out.println("unknown connection error");
			}
		}	
		
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
		return "This is an XMLGetter that has the team name: " + teamName + " and is has made" + numXML + " XMLs";
	}


}

