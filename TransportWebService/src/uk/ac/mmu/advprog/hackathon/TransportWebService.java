package uk.ac.mmu.advprog.hackathon;
import static spark.Spark.get;
import static spark.Spark.port;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handles the setting up and starting of the web service
 * You will be adding additional routes to this class, and it might get quite large
 * Feel free to distribute some of the work to additional child classes, like I did with DB
 * @author You, Mainly!
 */
public class TransportWebService {

	/**0
	 * 
	 * 
	 * Main program entry point, starts the web service
	 * @param args not used
	 */
	public static void main(String[] args) {		
		port(8088);
		
		//Simple route so you can check things are working...
		//Accessible via http://localhost:8088/test in your browser
		get("/test", new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try (DB db = new DB()) {
					return "Number of Entries: " + db.getNumberOfEntries();
				}
			}			
		});
		
				get("/stopcount", new Route() {
					@Override
					public Object handle(Request request, Response response) throws Exception {
						try (DB db = new DB()) {
							String LocalityName = request.queryParams("locality");
							if (LocalityName == null || LocalityName.isEmpty()) {
								return "invalid request";
							}
							return "Number of Entries: " + db.getNumberOfStopsInLocality(LocalityName);
						}
					}			
				});
				
				get("/stopsroute", new Route() {
					@Override
					public Object handle(Request request, Response response) throws Exception {
						try (DB db = new DB()) {
							String LocalityName = request.queryParams("locality");
							if (LocalityName == null || LocalityName.isEmpty()) {
								return "invalid request";
							}
							String type = request.queryParams("type");
							if (type == null || type.isEmpty()) {
								return "invalid request";
							}
							response.type("application/json");
							return  db.GetStopType(LocalityName, type) ;
						}
					}			
				});
		
				get("/nearestroute", new Route() {
					@Override
					public Object handle(Request request, Response response) throws Exception {
						try (DB db = new DB()) {
							String Latitude = request.queryParams("Latitude");
							if (Latitude == null || Latitude.isEmpty()) {
								return "invalid request";
							}
							String Longitude = request.queryParams("Longitude");
							if (Longitude == null || Longitude.isEmpty()) {
								return "invalid request";
							}
							String Type = request.queryParams("Type");
							if (Type == null || Type.isEmpty()) {
								return "invalid request";
							}
							
							response.type("application/json");
							return  db.GetLocation(Double.parseDouble(Latitude) , Double.parseDouble(Longitude), Type) ;
						}
					}			
				});
				
				/*get("/getLocation", new Route() {
					@Override
					public Object handle(Request request, Response response) throws Exception {
						String LocalityName = request.queryParams("locality");
						
						return LocalityName;
					}
				});
				*/
		System.out.println("Server up! Don't forget to kill the program when done!");
	}

}
