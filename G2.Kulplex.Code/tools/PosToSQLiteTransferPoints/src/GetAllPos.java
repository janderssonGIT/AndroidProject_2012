import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetAllPos {

	static protected Connection connection;
	static protected Statement s;
	static protected ResultSet rs;
	
	static protected String driver;
	static protected String url;
	static protected String username;
	static protected String password;
	static protected String qr;
	
	static protected int rowCount;
	static protected int columnCount;
	static protected String[][] all;
	static protected String[] allMerged;
	static protected String[] geom;
	static protected String[] geomStr;
	static protected int[][] latitudes;
	static protected int[][] longitudes;
	static protected String[] latMerged;
	static protected String[] lonMerged;
	static protected String tempQuery = " SELECT askml(\"way\") FROM planet_osm_point";
	
	
	
	
	
	/**
	 * Counts rows. 
	 * From JavaMagic - Jiayu
	 */
	private static int getRowCount(String query) {

		try {

			s.executeQuery("SELECT COUNT(*) FROM(" + query + ") as u");

			rs = s.getResultSet();

			while (rs.next()) {

				rowCount = rs.getInt(1);
			}

		} catch (Exception e) {
			
			System.out.println("Error in \"getRowCount.\"");
					
		}

		return rowCount;

	}
	
	
	/**
	 * 
	 * Gets the geometry column as kml
	 * 
	 */
	private static String[] getCoor(String query){
		
		try {

			int i = 0;
						
			geom = new String[rowCount];
			
			geomStr = new String[rowCount];
			
			latitudes = new int[rowCount][];
			
			longitudes = new int[rowCount][];
			
			latMerged = new String[rowCount];
			
			lonMerged = new String[rowCount];
					
			s.executeQuery("SELECT askml(\"way\"), * FROM planet_osm_point");

			rs = s.getResultSet();
			
			columnCount = rs.getMetaData().getColumnCount()-1;
			
			all = new String[rowCount][columnCount-1];
			
			allMerged = new String[rowCount];
			
			while (rs.next()) {
				
				for(int j = 0; j < columnCount-1; j++){
					
					all[i][j] = rs.getString(j+2);
					
					if(rs.getString(j+2) != null && rs.getString(j+2).charAt(0) != '"' ){
						
						all[i][j] = "\"" + rs.getString(j+2) + "\"";
						
					}
					
				}

				geom[i] = rs.getString(1);
				
				latMerged[i] = "";
				
				lonMerged[i] = "";
				
				geomStr[i] = geom[i].substring(20, geom[i].length()-22);

				latitudes[i] = splitStringToLat(geomStr[i]);

				longitudes[i] = splitStringToLon(geomStr[i]);

				allMerged[i] = mergeStrings(all[i]);

				for(int j = 0; j<latitudes[i].length; j++){
					
					latMerged[i]+="," + latitudes[i][j];
					
					lonMerged[i]+="," + longitudes[i][j];
					
					}
				System.out.println((i+1) + "/" + rowCount + " Receiving");
				i++;
			
			}

		} catch (Exception e) {
			
			System.out.println("Error in \"getGeom.\"");
					
		}
		
		return geom;
		
	}
	
	
	/**
	 * 
	 * 
	 * 
	 */
	private static int[] splitStringToLat(String mult){
		
		String[] splitEach = mult.split(" ");
		
		String[][] ind = new String[splitEach.length][];
		
		int[] lats = new int[splitEach.length];
		
		for(int i = 0; i < splitEach.length; i++){
			
			ind[i] = splitEach[i].split(",");
			
			lats[i] = (int) (new Float(ind[i][1])*1E6);
			
		}	
		
		return lats;
	}
	
	/**
	 * 
	 * 
	 * 
	 */
	private static int[] splitStringToLon(String mult){
		
		String[] splitEach = mult.split(" ");
		
		String[][] ind = new String[splitEach.length][];
		
		int[] longs = new int[splitEach.length];
		
		for(int i = 0; i < splitEach.length; i++){
			
			ind[i] = splitEach[i].split(",");
			
			longs[i] = (int) (new Float(ind[i][0])*1E6);
			
		}	
		
		return longs;
	}
	
	private static String mergeStrings(String[] a){
		
		String merged = "";
		
		for(int i = 0; i<a.length-1; i++){
			
			merged += a[i] + ", ";
			
		}
		
		merged+= a[a.length-1];
		
		return merged;
	}
	
	
	private static void Connect() {

		try {

			driver = "org.postgresql.Driver"; // Load the JDBC driver

			url = "jdbc:postgresql://localhost:5432/osmlndb"; // Connection
														// information
			username = "postgres";
			password = "1235";

			Class.forName(driver).newInstance(); // Connect to the database
			connection = DriverManager.getConnection(url, username,
					password);
			s = connection.createStatement();

		} catch (Exception ex) {
			System.out.println("No Connection");
		}
	}
	
	
	private static void CheckConnection() {

		try {

			rs = s.executeQuery(tempQuery);
			
			
			
			while(rs.next()){
				
				qr = rs.getString(1);
				
			}

		} catch (Exception e) {

			Connect();
		}
	}
	private static void Disconnect() {

		try {

			s.close();
			rs.close();
			connection.close();

		} catch (Exception e) {
		}
	}
	
	public static void main(String[] args) throws SQLException{
		
		Connect();
		CheckConnection();
		getRowCount(tempQuery);
		getCoor(tempQuery);
		Disconnect();
		
		AddToLite.Connect();
		AddToLite.updateL(allMerged, latitudes, longitudes);
		AddToLite.Disconnect();
	}
	
}
