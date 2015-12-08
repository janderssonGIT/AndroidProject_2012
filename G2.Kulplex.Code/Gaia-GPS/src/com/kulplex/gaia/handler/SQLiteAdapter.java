/**
 * SQLiteAdapter
 * 
 * This class handles the querying of the maps.
 * 
 * @author Alberto Vaccari, Hasan Yahya
 * 
 */

package com.kulplex.gaia.handler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.kulplex.gaia.CustomMapView;
import com.kulplex.gaia.GaiaApp;
import com.kulplex.gaia.obj.GaiaPOI;
import com.kulplex.gaia.obj.GaiaPoint;
import com.kulplex.gaia.obj.GaiaPolygon;
import com.kulplex.gaia.obj.GaiaStreet;
import com.kulplex.gaia.obj.GaiaPath;
import com.kulplex.gaia.obj.GaiaPathPoint;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteAdapter extends SQLiteOpenHelper{
	 
    //The Android's default system path of your application database.
    private static String DB_PATH = "/mnt/sdcard/Kulplex/Gaia/";
 
    private String DB_NAME;
    
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;
 
    GaiaApp gaia;

    
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public SQLiteAdapter(Context context, String DB_NAME) {
 
    	super(context, DB_NAME, null, 1);
        myContext = context;
        this.DB_NAME = DB_NAME;
        gaia = CustomMapView.gaia;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		System.out.println("NO NEED COPYING");
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
    			
    			System.out.println("DONE COPYING");
 
    		} catch (IOException e) {
 
    			System.out.println("ERROR COPYING " + e.toString());
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    		System.out.println("EXISTS");
    	}catch(SQLiteException e){
 
    		System.out.println("NOT EXISTS");
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	
    }
    
    
    public void deleteDatabase(){
    	
    	myContext.deleteDatabase(DB_NAME);
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
 
	
	public ArrayList<GaiaPOI> getPoints(Context context, float lat, float lon, int range, float scale, Boolean[] POIstate, Integer[] POIicon)
	{
		
		int zoomlvl = getZoomLvl(scale);
		ArrayList<GaiaPOI> plist = new ArrayList<GaiaPOI>();
		String[] args = new String[]{"" + ((int)lon - range/zoomlvl), "" + ((int)lon + range/zoomlvl), "" + ((int)lat - range/zoomlvl), "" + ((int)lat + range/zoomlvl) };
		String searchPOI = "";
		boolean previousPOI = false;
		
		
		//Food & Drink
		if(POIstate[0])
			{
				System.out.println("Food & Drink");
				searchPOI = " AND(";
				searchPOI += 
						 "amenity = 'restaurant' OR " +
						 "amenity = 'fast_food' OR " +
						 "amenity = 'cafe' OR " +
						 "amenity = 'pub' OR " +
						 "amenity = 'bar' OR " +
						 "amenity = 'internet_cafe'";
				previousPOI = true;
			}
		
		//Entertainment
		if (POIstate[1])
		{
			System.out.println("Entertainment");
			if(previousPOI)
				searchPOI += " OR ";
			else
				searchPOI = " AND(";
				
				searchPOI += 
						"amenity = 'theatre' OR " +
						"amenity = 'nightclub' OR " +
						"amenity = 'bath_place' OR " +
						"amenity = 'strip_club' OR " +
						"amenity = 'stripclub' OR " +
						"amenity = 'swimming_pool' OR " +
						"amenity = 'cinema'";
				previousPOI = true;
		}
		
		//Places of worship
		if (POIstate[2])
		{
			System.out.println("Worship");
			if(previousPOI)
				searchPOI += " OR ";	
			else
				searchPOI = " AND(";

				searchPOI += 
						"amenity = 'place_of_worship' OR " +
						"amenity = 'chapel'";
				previousPOI = true;
		}
		
		//Healthcare
		if (POIstate[3])
		{
			System.out.println("Healthcare");
			if(previousPOI)
				searchPOI += " OR ";
			else
				searchPOI = " AND(";
							
				searchPOI += 
						"amenity = 'pharmacy' OR " +
						"amenity = 'shelter' OR " +
						"amenity = 'fire_station' OR " +
						"amenity = 'hospital' OR " +
						"amenity = 'police' OR " +
						"amenity = 'dentist' OR " +
						"amenity = 'doctors' OR " +
						"amenity = 'veterinary'";
				previousPOI = true;
		}			
			
		//Education
		if (POIstate[4])
		{
			System.out.println("Education");
			if(previousPOI)
				searchPOI += " OR ";
			else
				searchPOI = " AND(";
							
				searchPOI += 
						"amenity = 'school' OR " +
						"amenity = 'library' OR " +
						"amenity = 'kindergarten' OR " +
						"amenity = 'university' OR " +
						"amenity = 'preschool' OR " +
						"amenity = 'college' OR " +
						"amenity = 'community_centre' OR " +
						"amenity = 'arts_centre'";
				previousPOI = true;
		}		
			
		//Public Transport
		if (POIstate[5])
		{
			System.out.println("Transport");
			if(previousPOI)
				searchPOI += " OR ";
			else
				searchPOI = " AND(";
							
				searchPOI += 
						"amenity = 'bus_station' OR " +
						"amenity = 'ferry_terminal' OR " +
						"amenity = 'taxi' OR " +
						"amenity = 'airport' OR " +
						"amenity = 'preschool'";
				previousPOI = true;
		}		
			
		//Shop
		if (POIstate[6])
		{
			System.out.println("Shop");
			if(previousPOI)
				searchPOI += " OR ";
			else
				searchPOI = " AND(";
							
				searchPOI += 
						"amenity = 'atm' OR " +
						"amenity = 'boat_rental' OR " +
						"amenity = 'car_wash' OR " +
						"amenity = 'car_rental' OR " +
						"amenity = 'vending_machine'";
				previousPOI = true;
		}	
		
		//Administrative Buildings
		if (POIstate[7])
		{
			System.out.println("Buildings");
			if(previousPOI)
				searchPOI += " OR ";
			else
				searchPOI = " AND(";
							
				searchPOI += 
						"amenity = 'post_box' OR " +
						"amenity = 'post_office' OR " +
						"amenity = 'townhall' OR " +
						"amenity = 'public_building'";
				previousPOI = true;
		}	
		
		//Parking
		if (POIstate[8])
		{
			System.out.println("Parking");
			if(previousPOI)
				searchPOI += " OR ";
			else
				searchPOI = " AND(";
							
				searchPOI += 
						"amenity = 'parking' OR " +
						"amenity = 'bicycle_parking' OR " +
						"amenity = 'parking_entrance'";
				previousPOI = true;
		}	
		
		
		if(previousPOI)
			searchPOI += ")";
		
		
		Cursor c =  myDataBase.rawQuery("SELECT latitude, longitude, name, amenity FROM planet_osm_point WHERE longitude > ? AND longitude < ? AND latitude > ? AND latitude < ?" + searchPOI,
				args);
		
		if(c.moveToFirst())
			do{
			
				int _lat = c.getInt(0);
				int _lon = c.getInt(1);
				String name = c.getString(2);
				String type = c.getString(3);
				
				
				//0
				if(type.equals("restaurant") || type.equals("fast_food") || type.equals("cafe") || 
						type.equals("pub") || type.equals("bar") || type.equals("internet_cafe"))
							
							plist.add(new GaiaPOI(name, type,_lon, _lat, POIicon[0]));
	
				//1
				else if(type.equals("theatre") || type.equals("nightclub") || type.equals("bath_place") || 
						type.equals("strip_club") || type.equals("stripclub") || type.equals("swimming_pool") || type.equals("cinema"))
					
							plist.add(new GaiaPOI(name, type,_lon, _lat, POIicon[1]));
		
		
				//2
				else if(type.equals("place_of_worship") || type.equals("chapel"))
					
							plist.add(new GaiaPOI(name, type,_lon, _lat, POIicon[2]));

				//3
				else if(type.equals("pharmacy") || type.equals("shelter") || type.equals("fire_station") || 
						type.equals("hospital") || type.equals("police") || type.equals("dentist") || type.equals("doctors") || type.equals("veterinary"))
					
							plist.add(new GaiaPOI(name, type, _lon, _lat, POIicon[3]));
				
				
				//4
				else if(type.equals("school") || type.equals("library") || type.equals("kindergarten") || 
						type.equals("university") || type.equals("preschool") || type.equals("college") || type.equals("community_centre") || type.equals("arts_centre"))
					
							plist.add(new GaiaPOI(name, type, _lon, _lat, POIicon[4]));

				//5
				else if(type.equals("bus_station") || type.equals("ferry_terminal") || type.equals("taxi") || 
						type.equals("airport") || type.equals("preschool"))
					
							plist.add(new GaiaPOI(name, type, _lon, _lat, POIicon[5]));

				//6
				else if(type.equals("atm") || type.equals("boat_rental") || type.equals("car_wash") || 
						type.equals("car_rental") || type.equals("vending_machine"))
					
							plist.add(new GaiaPOI(name, type, _lon, _lat, POIicon[6]));

				
				//7
				else if(type.equals("post_box") || type.equals("post_office") || type.equals("townhall") || 
						type.equals("public_building"))
					
							plist.add(new GaiaPOI(name, type, _lon, _lat, POIicon[7]));
				
				//8
				else if(type.equals("parking") || type.equals("bicycle_parking") || type.equals("parking_entrance"))
					
							plist.add(new GaiaPOI(name, type, _lon, _lat, POIicon[8]));
			
			}while(c.moveToNext());
		
		c.close();
		
		return plist;
		
	}
	
	public ArrayList<GaiaStreet> getStreets(Context context, float lat, float lon, int range, float scale)
	{
		
		int zoomlvl = getZoomLvl(scale);

		Log.d("Scale", "Scale: "+scale);
		ArrayList<GaiaStreet> plist = new ArrayList<GaiaStreet>();
		String[] args = new String[]{"" + ((int)lon - range/zoomlvl), "" + ((int)lon + range/zoomlvl), "" + ((int)lat - range/zoomlvl), "" + ((int)lat + range/zoomlvl) };
		
		
		Cursor c;
		if(zoomlvl == 6)
		{
			c =  myDataBase.rawQuery("SELECT name, longitude, latitude, highway, l._id FROM planet_osm_line l INNER JOIN planet_osm_streets s ON (l._id = s.code) WHERE " +
					"longitude > ? AND longitude < ? AND latitude > ? AND latitude < ? ", args);
		}
		else if(zoomlvl == 5)
		{
			c =  myDataBase.rawQuery("SELECT name, longitude, latitude, highway, l._id FROM planet_osm_line l INNER JOIN planet_osm_streets s ON (l._id = s.code) WHERE " +
					"longitude > ? AND longitude < ? AND latitude > ? AND latitude < ?" , args);
		}
		else if(zoomlvl == 4)
		{
			c =  myDataBase.rawQuery("SELECT name, longitude, latitude, highway, l._id FROM planet_osm_line l INNER JOIN planet_osm_streets s ON (l._id = s.code) WHERE " +
					"longitude > ? AND longitude < ? AND latitude > ? AND latitude < ?" , args);
		}
		else
		{
			c =  myDataBase.rawQuery("SELECT name, longitude, latitude, highway, l._id FROM planet_osm_line l INNER JOIN planet_osm_streets s ON (l._id = s.code) WHERE " +
					"longitude > ? AND longitude < ? AND latitude > ? AND latitude < ?", args);
		}
		
		int code = 0;
		GaiaStreet gs = new GaiaStreet("","");  //void init
		
		if(c.moveToFirst())
			do{
	
				Float _lon = c.getFloat(1);
				Float _lat = c.getFloat(2);
				
				
	
				if(c.getInt(4) == code)
				{
					gs.addNode(new GaiaPoint(_lon, _lat));
				}
				else
				{
					if(code != 0)
						plist.add(gs);
					
					code = c.getInt(4);
					
					gs = new GaiaStreet(c.getString(0), c.getString(3));
					
					
					
					gs.addNode(new GaiaPoint(_lon, _lat));
				}
					

					
		}while(c.moveToNext());
		
		c.close();
		return plist;
		
		
	}
	
	public ArrayList<GaiaPolygon> getPolygons(Context context, float lat, float lon, int range, float scale)
	{
		int zoomlvl = 10;
		
		ArrayList<GaiaPolygon> plist = new ArrayList<GaiaPolygon>();
		String[] args = new String[]{"" + ((int)lon - range/zoomlvl), "" + ((int)lon + range/zoomlvl), "" + ((int)lat - range/zoomlvl), "" + ((int)lat + range/zoomlvl) };
		
		Cursor c =  myDataBase.rawQuery("SELECT name, longitude, latitude, l._id, s.part FROM planet_osm_polygon l INNER JOIN planet_osm_poly s ON (l._id = s.code) WHERE " +
				"longitude > ? AND longitude < ? AND latitude > ? AND latitude < ?", args);
		
		int code = 0;
		GaiaPolygon gp = new GaiaPolygon("");  //void init
		
		if(c.moveToFirst())
			do{
				
				Float _lon = c.getFloat(1);
				Float _lat = c.getFloat(2);
				
				if(c.getInt(3) == code)
				{
					gp.addNode(new GaiaPoint(_lon, _lat));
				}
				else
				{
				
					
					if(code != 0)
						plist.add(gp);
					
					code = c.getInt(3);
					
					gp = new GaiaPolygon(c.getString(0));
					gp.addNode(new GaiaPoint(_lon, _lat));
				
				}
				
			}while(c.moveToNext());
		
		c.close();
		
		
		
		return plist;
		
	}
	
	
	public int getZoomLvl(float scale)
	{
		int zoomlvl;
		
		
		if(scale >= 0.075f)
		{
			zoomlvl = 20;
		}
		else if(scale >= 0.06f && scale < 0.075f)
		{
			zoomlvl = 10;
		}
		else if(scale >= 0.04f && scale < 0.06f)
		{
			zoomlvl = 8;
		}
		else
			zoomlvl = 3;
		
		return zoomlvl;
	}
	
	public ArrayList<GaiaPathPoint> getPathPoints(Context context)
	{
		
		ArrayList<GaiaPathPoint> pPlist = new ArrayList<GaiaPathPoint>();
		
		Cursor c =  myDataBase.rawQuery(" SELECT _id, latitude, longitude FROM planet_osm_streets ", null);
		
		if(c.moveToFirst())
			
			do{
			
				String id = c.getString(0);
				String _lat = c.getString(1);
				String _lon = c.getString(2);
				
				GaiaPathPoint asdf = new GaiaPathPoint(_lat+","+_lon, _lat+","+_lon);
						
			
				if(!asdf.existsIn(pPlist)){
					
					pPlist.add(asdf);
					
				}
				
					
			}while(c.moveToNext());
		
		c.close();
		
		return pPlist;
		
	}
	
	public ArrayList<GaiaPath> getPaths(Context context)
	{
		
		ArrayList<GaiaPathPoint> pthPolist = new ArrayList<GaiaPathPoint>();
		ArrayList<String> coList = new ArrayList<String>();
		ArrayList<GaiaPath> gPList = new ArrayList<GaiaPath>();
		
		
		Cursor c =  myDataBase.rawQuery(" SELECT _id, code, latitude, longitude FROM planet_osm_streets ", null);
		
		int i = 0;
		if(c.moveToFirst())
			
			do{
			
				String code = c.getString(1);
				String _lat = c.getString(2);
				String _lon = c.getString(3);
				
				pthPolist.add(new GaiaPathPoint(_lat+","+_lon, _lat+","+_lon));
				coList.add(code);
				
				if(i>0 && coList.get(i-1).equals(coList.get(i)))
				{
					
					GaiaPathPoint n1 = pthPolist.get(i-1);
					
					GaiaPathPoint n2 = pthPolist.get(i);
					
					String edge = n1.getName()+"TO"+n2.getName();
				
					gPList.add(new GaiaPath(edge, n1, n2));
				
				}
				i++;
			}while(c.moveToNext());
	
		c.close();
	
		return gPList;
	
	}

	
		public ArrayList<String> getAddress(Context context)
		{
				
			ArrayList<String> streetNames = new ArrayList<String>();
				
			Cursor c =  myDataBase.rawQuery(" SELECT distinct name FROM planet_osm_line WHERE NAME IS NOT NULL ORDER BY NAME ", null);
				
			if(c.moveToFirst())
					
				do{
					
					String name = c.getString(0);
						
						streetNames.add(name);	
						
				}while(c.moveToNext());
				
			c.close();
				
			return streetNames;
				
		}
		
		
		public GaiaPathPoint getPosition(Context context, String type, String pName)
		{
			
			String query = "SELECT MIN(_id), latitude, longitude FROM planet_osm_streets WHERE code = (SELECT MIN(_id) FROM planet_osm_line WHERE name = \"" + pName +  "\")";
			
			if(type.equals("street")){
				query = "SELECT MIN(_id), latitude, longitude FROM planet_osm_streets WHERE code = (SELECT MIN(_id) FROM planet_osm_line WHERE name = \"" + pName +  "\")";
			}
			
			if(type.equals("landmark")){
				query = "SELECT MIN(_id), latitude, longitude FROM planet_osm_point WHERE name = \"" + pName +  "\")";
			}
			
			System.out.println(query);
			GaiaPathPoint stPos;
				
			Cursor c =  myDataBase.rawQuery(query, null);
				
			if(c.moveToFirst()){
					
				do{
					
					
					String lat = c.getString(1);
						
					String lon = c.getString(2);	
					
					stPos = new GaiaPathPoint(lat+","+lon, lat+","+lon);
					
				}while(c.moveToNext());
				
			c.close();
				
			return stPos;
			}
			
			else
				return null;
			
				
		}
		
				
		public ArrayList<String> getLocation(Context context)
		{
		
			ArrayList<String> stop_landmark = new ArrayList<String>();
		
			Cursor c =  myDataBase.rawQuery(" SELECT distinct name FROM planet_osm_point WHERE NAME IS NOT NULL ORDER BY NAME ", null);
		
			if(c.moveToFirst())
			
				do{
			
					String name = c.getString(0);
				
					stop_landmark.add(name);	
				
				}while(c.moveToNext());
		
			c.close();
		
			return stop_landmark;
		
		} 


	}