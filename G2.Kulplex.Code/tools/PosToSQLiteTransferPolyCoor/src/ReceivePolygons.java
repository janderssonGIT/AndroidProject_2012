import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReceivePolygons {

	static protected Connection connection;
	static protected Statement s;
	static protected ResultSet rs;
	
	static protected String driver;
	static protected String url;
	static protected String qr;
	static protected String dbName;
	static protected String username;
	static protected String password;

	static protected int rowCount;
	static protected int columnCount;
	static protected String[] geom;
	static protected String[] geomStr;
	static protected String[][] parts;
	static protected int[][][] latitudes;
	static protected int[][][] longitudes;
	static protected String tempQuery = " SELECT askml(\"way\") FROM planet_osm_polygon";
	
	
	
	
	
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
			System.out.println("SELECT COUNT(*) FROM(" + query + ") as u");
			System.out.println("Error in \"getRowCount.\""+ e.getMessage());
					
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
			
			parts = new String[rowCount][];
			
			latitudes = new int[rowCount][][];
			
			longitudes = new int[rowCount][][];
					
			s.executeQuery("SELECT askml(\"way\") FROM planet_osm_polygon");

			rs = s.getResultSet();
			
			columnCount = rs.getMetaData().getColumnCount()-1;
			
									
			while (rs.next()) {
				
				geom[i] = rs.getString(1);

				geomStr[i] = geom[i].substring(51, geom[i].length()-55);
				
				parts[i] = removeTxt(geomStr[i]);

				latitudes[i] = new int[parts[i].length][];
				
				longitudes[i] = new int[parts[i].length][];
				
				for(int j = 0; j< parts[i].length; j++){

				latitudes[i][j] = splitStringToLat(parts[i][j]);

				longitudes[i][j] = splitStringToLon(parts[i][j]);

				}
				
				System.out.println((i+1) + "/" + rowCount + " receiving");
				
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
	
	private static String[] removeTxt(String a){
		
		String repl = a.replaceAll("</coordinates></LinearRing></outerBoundaryIs><innerBoundaryIs><LinearRing><coordinates>", "#");
		String repl2 = repl.replaceAll("</coordinates></LinearRing></innerBoundaryIs><innerBoundaryIs><LinearRing><coordinates>", "#");
		
		String[] temp = repl2.split("#");
		
		return temp;
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
		
		
		AddPolygons.Connect();
		AddPolygons.updateL(latitudes, longitudes);
		AddPolygons.Disconnect();
		
		
	}
	
}
