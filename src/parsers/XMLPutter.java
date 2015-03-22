package parsers;
import java.io.BufferedReader;

import flight_system.*;

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
	private FlightLeg flightleg;
	
	private static XMLPutter firstInstance = null;
	
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
			url = new URL(urlAddress+"?team="+teamName+"&action=lockDB");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			DataOutputStream writer=new DataOutputStream(connection.getOutputStream());
			writer.writeBytes( "?team="+teamName+"&action=lockDB");
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
		return true;
	}
	
	/*Unlock the Database*/
	public boolean unlockDB(){
		URL url;
		HttpURLConnection connection;
		
		try{
			//url = new URL(urlAddress+"?team="+teamName+"&action=unlockDB");
			url = new URL(urlAddress+"?team="+teamName+"&action=unlockDB");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			DataOutputStream writer=new DataOutputStream(connection.getOutputStream());
			//writer.writeBytes( "?team="+teamName+"&action=unlockDB");
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
		return true;
	}

	public boolean buyTicket (){
		URL url;
		HttpURLConnection connection;
		
		try{
			url = new URL(urlAddress+"?team="+teamName+"&action=buyTickets&flightData=<Flights><Flight number="+"\""+1781+"\""+" seating="+"\""+"FirstClass"+"\""+" />"
					 +"<Flight number="+"\""+1782+"\""+" seating="+"\""+"FirstClass"+"\""+" /></Flights>");
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			DataOutputStream writer=new DataOutputStream(connection.getOutputStream());
			writer.writeBytes("?team="+teamName+"&action=buyTickets&flightData=<Flights><Flight number="+"\""+1781+"\""+" seating="+"\""+"FirstClass"+"\""+" />"
					 +"<Flight number="+"\""+1782+"\""+" seating="+"\""+"FirstClass"+"\""+" /></Flights>");
			writer.flush();
			writer.close();
			//flightleg.getAirplane().firstClassSeatsInc(); //number of first class seats increases after buy a ticket
			
			int responseCode=connection.getResponseCode();
			System.out.println("\nResponse Code:"+ responseCode);
			
			if((responseCode>=200)&&(responseCode<=299)){
				System.out.println("buy ticket successfully!");
				BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response=new StringBuffer();
				
				while((line=in.readLine())!=null){
					response.append(line);
				}
				in.close();
				
				System.out.println(response.toString());
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
		return true;
	}
	
	/*make a ticket with the fight number */
	public String makeTicket(int number){
		 FlightParser flight=new FlightParser();
		 flightleg=flight.getFlight(number);
		 //int firstclassNum=flightleg.getAirplane().getFirstClassSeats();
		 //int coachNum=flightleg.getAirplane().getCoachSeats();
		 //String para="&flightData=<Flights><Flight number="+"\""+number+"\""+" seating="+"\""+"CoachSeat"+"\""+" /></Flights>";
		
		 String para="&flightData=<Flights><Flight number="+"\""+number+"\""+" seating="+"\""+"FirstClass"+"\""+" />"
		 +"<Flight number="+"\""+number+"\""+" seating="+"\""+"FirstClass"+"\""+" /></Flights>";
		 return para;
	}
	
	public void resetDB(){
		
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("Testing XMLPutter Class");
		XMLPutter test = XMLPutter.getInstance(); // create the test object
//		test.lockDB();
		test.unlockDB();
//		
		//test.buyTicket();	
	}
}
