import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class AddPolygons {

	static protected Connection connection2;
	static protected Statement s2;
	
	static protected String driver2;
	static protected String url2;
	static protected String dbName2;
	public static void Connect() {

		try {

			driver2 = "org.sqlite.JDBC"; // Load the JDBC driver

			url2 = "jdbc:sqlite:SQLiteOsmlndb.sqlite"; // Connection
														// information
		

			Class.forName(driver2).newInstance(); // Connect to the database
			connection2 = DriverManager.getConnection(url2);
			s2 = connection2.createStatement();

		} catch (Exception ex) {
			System.out.println("No Connection");
		}
	}
	
	
	
	public static void Disconnect() {

		try {

			s2.close();
			
			connection2.close();

		} catch (Exception e) {
		}
	}
	
	public static void updateL(String[] a) throws SQLException{
		
		
		for(int i = 0; i< ReceivePolygons.rowCount; i++){
					
				s2.executeUpdate("INSERT INTO planet_osm_polygon VALUES (" + (i+1) + ", " + a[i] + ")");
							
		System.out.println((i+1) + "/" + ReceivePolygons.rowCount + " Adding");
		}
		
	}
	
	
}
