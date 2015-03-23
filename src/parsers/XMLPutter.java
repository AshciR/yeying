package parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class XMLPutter {
	private String teamName = "TeamYeYing"; // Team Name
	private int numXML; // Number of XMLs 
	private String urlAddress = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";
	
	private static XMLPutter firstInstance = null;
	
	/* The private constructor */
	private XMLPutter(){};
	
	/* Method to get the only instance of the class */
	public static XMLPutter getInstance(){
		if(firstInstance == null){
			firstInstance = new XMLPutter();
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
	
	/*Lock the Database*/
	public boolean lockDB(){
		URL url;
		HttpURLConnection connection;
		
		try{
			url = new URL(urlAddress);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			DataOutputStream writer=new DataOutputStream(connection.getOutputStream());
			writer.writeBytes( "team="+teamName+"&action=lockDB");
			writer.flush();
			writer.close();
			
			int responseCode=connection.getResponseCode();
			System.out.println("\nSending 'POST' to lock database");
			System.out.println("\nResponse Code:"+ responseCode);
			
			if((responseCode>=200)&&(responseCode<=299)){
				System.out.println("lock the Database successfully!");
				BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response=new StringBuffer();
				
				while((line=in.readLine())!=null){
					response.append(line);
				}
				in.close();
				
				System.out.println(response.toString());
				return true;
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
			return false;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return false;
	}
	
	/*Unlock the Database*/
	public boolean unlockDB(){
		URL url;
		HttpURLConnection connection;
		
		try{
			//url = new URL(urlAddress+"?team="+teamName+"&action=unlockDB");
			url = new URL(urlAddress);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			DataOutputStream writer=new DataOutputStream(connection.getOutputStream());
			writer.writeBytes( "team="+teamName+"&action=unlockDB");
			writer.flush();
			writer.close();
			
			int responseCode=connection.getResponseCode();
			System.out.println("\nSending 'POST' to unlock database");
			System.out.println("\nResponse Code:"+ responseCode);
			
			if((responseCode>=200)&&(responseCode<=299)){
				System.out.println("unlock the Database successfully!");
				BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response=new StringBuffer();
				
				while((line=in.readLine())!=null){
					response.append(line);
				}
				in.close();
				
				System.out.println(response.toString());
				return true;
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
			return false;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return false;
	}

	/* Make a ticket with the fight number */
	public String makeTicket(int number, boolean firstClass){
		
		/* Flight String looks like this. */
		/*		 
		<Flights>
		   <Flight number=Ò1781Ó seating=ÒFirstClassÓ />
		   <Flight number=Ò1782Ó seating=ÒFirstClassÓ />
		</Flights>
		*/
		
		String seat; // holds either First Class or Coach String
		
		/* Set the seat String */
		if (firstClass) {
			seat = "FirstClass";
		}
		else{
			seat = "Coach";
		}
		
		/* The ticket XML */
		String ticket = "<Flights><Flight number="+"\""+ number +"\""+" seating="+"\""+seat+"\""+" /></Flights>";
				 
		 return ticket;
	}
	
	/*Reset the Database*/
	public String resetDB(){
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		
		try{
			url = new URL(urlAddress + "?team="+teamName+"&action=resetDB");
				
			/* Open Connection and send GET request */
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			/* The response code given by the server*/
			int responseCode = connection.getResponseCode(); 
			System.out.println("\nThe Response Code is: " + responseCode);
			
			/* If The connection was successful */
			if((responseCode >= 200) && (responseCode <= 299)){
				
				System.out.println("Reset the Database"); // Shows successful connection
				
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
	
	
	/* Buying a ticket */
	public boolean buyTicket(String ticket){
		URL url;
		HttpURLConnection connection;
		
		try{
			url = new URL(urlAddress);
			
			/* Test to see the URL that gets produced */
			System.out.println(url);
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes("team="+teamName+"&action=buyTickets&flightData="+ticket);
			writer.flush();
			writer.close();
			
			int responseCode=connection.getResponseCode();
			System.out.println("\nResponse Code: "+ responseCode);
			
			if((responseCode>=200)&&(responseCode<=299)){
				System.out.println("The ticket was bought.");
				BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response=new StringBuffer();
				
				while((line=in.readLine())!=null){
					response.append(line);
				}
				in.close();
				
				System.out.println(response.toString());
				
				return true;
			}
			/* Else the response was not valid */
			else if (responseCode == 304){
				System.out.println("Unsuccessful: Did not update the Database.");
				return false;
			}
			else if (responseCode == 400){
				System.out.println("Missing or Invalid action");
				return false;
			}
			else if (responseCode == 412){
				System.out.println("Unsuccessful: Our team did not have the lock.");
				return false;
			}
			else{
				System.out.println("Unknown connection error");
				return false;
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
			return false;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		
	}
	
	
		
}
