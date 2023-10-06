package uk.ac.mmu.advprog.hackathon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Handles database access from within your web service
 * @author You, Mainly!
 */
public class DB implements AutoCloseable {
	
	//allows us to easily change the database used
	private static final String JDBC_CONNECTION_STRING = "jdbc:sqlite:./data/NaPTAN.db";
	
	//allows us to re-use the connection between queries if desired
	private Connection connection = null;
	
	/**
	 * Creates an instance of the DB object and connects to the database
	 */
	public DB() {
		try {
			connection = DriverManager.getConnection(JDBC_CONNECTION_STRING);
		}
		catch (SQLException sqle) {
			error(sqle);
		}
	}
	
	/**
	 * Returns the number of entries in the database, by counting rows
	 * @return The number of entries in the database, or -1 if empty
	 */
	public int getNumberOfEntries() {
		int result = -1;
		try {
			Statement s = connection.createStatement();
			ResultSet results = s.executeQuery("SELECT COUNT(*) AS count FROM Stops");
			while(results.next()) { //will only execute once, because SELECT COUNT(*) returns just 1 number
				result = results.getInt(results.findColumn("count"));
			}
		}
		catch (SQLException sqle) {
			error(sqle);
			
		}
		return result;
	}
	/**
	 * 
	 * @param LocalityName Is the name of the area (locality)
	 * @return Returns the number of stops in that locality
	 */
	public int getNumberOfStopsInLocality(String LocalityName) {
		int result = -1;

		try {

			
			//run query
			PreparedStatement s = connection.prepareStatement("SELECT COUNT(*) AS Number FROM Stops WHERE LocalityName = ?");
			s.setString(1, LocalityName);
			ResultSet results = s.executeQuery();
			while(results.next()) { //will only execute once, because SELECT COUNT(*) returns just 1 number
				result = results.getInt(results.findColumn("Number"));
			}
		}
		catch (SQLException sqle) {
			error(sqle);
			
		}
		return result;
	}
	/**
	 * 
	 * @param LocalityName Is the name of the area (locality)
	 * @param type Different types of stops in the area for example ferry bus stop metrolink etc
	 * @return a JSON array of json objects which have all the data
	 */
	
	public JSONArray GetStopType(String LocalityName, String type) {
		JSONArray JSONresults = new JSONArray();

		try {

			//run query
			PreparedStatement s = connection.prepareStatement("SELECT * FROM Stops WHERE LocalityName = ? AND StopType = ?");
			s.setString(1, LocalityName);
			s.setString(2, type);

			ResultSet results = s.executeQuery();
			while(results.next()) { //will only execute once, because SELECT COUNT(*) returns just 1 number
				
				
				
				
				JSONObject location = new JSONObject();
				//location.put("indicator", results.getString("Indicator"));
				if (results.getString("Indicator") != null) {
					location.put("indicator", results.getString("Indicator"));
				}else {
					location.put("indicator", "");
				}
				
				//location.put("bearing", results.getString("Bearing"));
				if (results.getString("Bearing") != null) {
					location.put("bearing", results.getString("Bearing"));
				}else {
					location.put("bearing", "");
				}
				
				
			//	location.put("street", results.getString("Street"));
				if (results.getString("Street") != null) {
					location.put("street", results.getString("Street"));
				}else {
					location.put("street", "");
				}
				
			//	location.put("landmark", results.getString("Landmark"));
				if (results.getString("Landmark") != null) {
					location.put("landmark", results.getString("Landmark"));
				}else {
					location.put("landmark", "");
				}
					
			
				
				JSONObject result = new JSONObject();
				//result.put("name", results.getString("CommonName"));
				if (results.getString("CommonName") != null) {
					result.put("name", results.getString("CommonName"));
				}else {
					result.put("name", "");
				}
				
				
			//	result.put("locality", results.getString("LocalityName"));
				if (results.getString("LocalityName") != null) {
					result.put("locality", results.getString("LocalityName"));
				}else {
					result.put("locality", "");
				}
				
				
				//result.put("type", results.getString("StopType"));
				if (results.getString("StopType") != null) {
					result.put("type", results.getString("StopType"));
				}else {
					result.put("type", "");
				}
				
				
				result.put("location", location);

				//result.toString();
				JSONresults.put(result);
				
				
				//result = results.getInt(results.findColumn("LocalityName"));
				
			}
		}
		catch (SQLException sqle) {
			error(sqle);
			
		}
		return JSONresults;
	}
	/**
	 * 
	 * @param Latitude 
	 * @param Longitude
	 * @param Type
	 * @return
	 */
	
	public String GetLocation(double Latitude, double Longitude, String Type) {
		String result = "";

		try {

			
			//run query
			PreparedStatement s = connection.prepareStatement("SELECT * FROM Stops WHERE StopType = 'MET' AND Latitude IS NOT NULL AND LONGITUDE IS NOT NULL ORDER BY( ((53.472 - Latitude) * (53.472 - Latitude)) + (0.595 * ((-2.244 - Longitude) * (-2.244 - Longitude))) ASC LIMIT 5");
			s.setDouble(1, Latitude);		
			s.setDouble(2, Longitude);
			s.setString(3, Type);
			ResultSet results = s.executeQuery();
			while(results.next()) { //will only execute once, because SELECT COUNT(*) returns just 1 number
				//result = results.getInt(results.findColumn("Number"));
			}
		}
		catch (SQLException sqle) {
			error(sqle);
			
		}
		return result;
	}
	
	/**
	 * Closes the connection to the database, required by AutoCloseable interface.
	 */
	@Override
	public void close() {
		try {
			if ( !connection.isClosed() ) {
				connection.close();
			}
		}
		catch(SQLException sqle) {
			error(sqle);
		}
	}

	/**
	 * Prints out the details of the SQL error that has occurred, and exits the programme
	 * @param sqle Exception representing the error that occurred
	 */
	private void error(SQLException sqle) {
		System.err.println("Problem Opening Database! " + sqle.getClass().getName());
		sqle.printStackTrace();
		System.exit(1);
	}
}
