/**
 * CustomMapView
 * 
 * This class handles the MapView, it also uses the Renderer class for the rendering
 * 
 * 
 * @author Alberto Vaccari
 * 
 */

package com.kulplex.gaia;


import java.util.ArrayList;

import com.kulplex.gaia.R;

import com.kulplex.gaia.handler.PathFinder;
import com.kulplex.gaia.handler.Renderer;
import com.kulplex.gaia.handler.SQLiteAdapter;
import com.kulplex.gaia.obj.GaiaPOI;
import com.kulplex.gaia.obj.GaiaPath;
import com.kulplex.gaia.obj.GaiaPathPoint;
import com.kulplex.gaia.obj.GaiaPoint;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CustomMapView extends SurfaceView implements
		SurfaceHolder.Callback, LocationListener {

	private RenderingThread _threadStreets;
	private static final String LOG_TAG = "KulplexGaia";

	Context myContext;
	SQLiteAdapter myDbHelper;

	Paint pt = new Paint();

	public static PathFinder pf;
	
	final int SCREEN_HEIGHT = 600;
	final int SCREEN_WIDTH = 480;

	static public float scale;

	int range = 200000;
	static public int rotation = 0;

	int sensibility = 100;

	int lat;
	int lon;

	float translateX = -lat * scale - SCREEN_HEIGHT / 2;
	float translateY = -lon * scale + SCREEN_WIDTH / 2;

	float lastOffsetX;
	float lastOffsetY;

	float poiLon;
	float poiLat;
	
	Bitmap bitmap;

	boolean followLocation = true;
	boolean isZooming = false;
	boolean showLocation = true;
	boolean followRotation = false;

	static boolean drivingMode = false;
	static boolean addPOIMode = false;
	
	boolean DEBUG = true;
	Handler mHandler;

	public static GaiaApp gaia;

	static String streetname = "";
	
	Spinner sIcon;
	

	public CustomMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);

		gaia = ((GaiaApp) context.getApplicationContext());
		
		scale = gaia.getCurScale();
		
		lat = (int) gaia.getCurPosition().getLatitude();
		lon = (int) gaia.getCurPosition().getLongitude();

		startupDB();
		myContext = context;
		mHandler = new Handler();
		_threadStreets = new RenderingThread(getHolder(), this);

		setFocusable(true);

		// Should get the current location from GPS, also sets interval and
		// precision.
		LocationManager lm = (LocationManager) getContext().getSystemService(
				Context.LOCATION_SERVICE);

		LocationManager myLocationManager = (LocationManager) getContext()
				.getSystemService(Context.LOCATION_SERVICE);

		// Sets criteria for choosing the best location provider
		Criteria provchooser = new Criteria();
		provchooser.setAccuracy(Criteria.ACCURACY_FINE);
		provchooser.setAltitudeRequired(false);
		provchooser.setBearingRequired(false);
		provchooser.setCostAllowed(true);
		provchooser.setPowerRequirement(Criteria.POWER_LOW);

		String provider = myLocationManager.getBestProvider(provchooser, true);

		if (provider != null) {

			lm.requestLocationUpdates(provider, 0, 0, this);
		}
		
		pt.setFlags(Paint.DITHER_FLAG);
		pt.setFilterBitmap(true);

		pf = new PathFinder();
		setCenterAt(lat, lon);
		this.invalidate();
		
	}

	@Override
	public void onDraw(Canvas canvas) {
		
		try {	
			canvas.drawColor(Color.argb(200, 240, 248, 255));
		
			pt.setAntiAlias(true);
			pt.setAlpha(255);
		
			canvas.rotate(-90, 0, 0);
			canvas.translate(translateX, translateY);

			canvas.scale(scale, scale);
			
			
			canvas.rotate(-rotation, lat, lon);
			
			// Drawing Points
				
			Renderer.drawStreet(gaia.getStreets(), scale, canvas, pt);
			Renderer.drawPolygon(gaia.getPolygons(), canvas, pt);
			
			
			if(drivingMode)
			{
				 Renderer.drawRoute(myContext, gaia.getRoute(), canvas, pt);
				 sayTurn(gaia.getRoute());
			}
			else
			{
				Renderer.drawPOI(myContext, gaia.getPOIs(), canvas, pt, scale, rotation);
				Renderer.drawCustomPOI(myContext, gaia.getCustomPOI(), canvas, pt, scale, rotation);
			}

			
			if (showLocation) {

				drawMarker(canvas);
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		
	
	}

	public static void runTarget(GaiaPoint gp){
		 
		 String curp = (int)gp.getLatitude()+","+(int)gp.getLongitude();
		 
		 GaiaPathPoint gpp = new GaiaPathPoint(curp,curp);


		 pf.locateTarget(gaia, gaia.getPathPoints(), gpp, gaia.getPathMap(), gaia.getPathMapList(), gaia.getPaths());

	 }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	
			if (event.getAction() == MotionEvent.ACTION_DOWN) {

				lastOffsetX = event.getX();
				lastOffsetY = event.getY();

				invalidate();

			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {

				if (!isZooming) {

					float newX = event.getX() - lastOffsetX;
					float newY = event.getY() - lastOffsetY;

					// increment last offset to last processed by drag event.
					lastOffsetX += newX;
					lastOffsetY += newY;

					// Canvas is rotated thus it must be fixed
					translateX -= newY;
					translateY += newX;

				} else {

					Toast.makeText(null, "ZoomingOut", Toast.LENGTH_SHORT)
							.show();

					scale -= 0.2;
					gaia.setCurScale(scale);
				}


				invalidate();

			} else if (event.getAction() == MotionEvent.ACTION_UP) {

				invalidate();
				
				if(addPOIMode)
				{
					System.out.println("Adding stuff and shit");
				
					if(event.getY() < SCREEN_HEIGHT / 2 && event.getX() < SCREEN_WIDTH / 2)
					{
						float newX = (SCREEN_WIDTH / 2) - (event.getX()/scale);
						float newY = (SCREEN_HEIGHT/ 2) - (event.getY()/scale);
						
						poiLon = getCenterLon() - newX;
						poiLat = getCenterLat() + newY;
				
					}
					
					else
					if(event.getY() > SCREEN_WIDTH / 2 && event.getX() > SCREEN_WIDTH / 2)
					{
						float newX = (SCREEN_HEIGHT / 2) - (event.getX()/scale);
						float newY = (SCREEN_WIDTH/ 2) - (event.getY()/scale);
						
						poiLon = getCenterLon() + newX;
						poiLat = getCenterLat() - newY;
				
					}
					
					// Get Dialog
					final Dialog dialog = new Dialog(myContext);

					dialog.setContentView(R.layout.add_favourites);
					dialog.setTitle("Add Custom Location");
					sIcon = (Spinner) dialog.findViewById(R.id.iconChooser);

					ArrayAdapter<CharSequence> adapter = ArrayAdapter
							.createFromResource(myContext,
									R.array.icon_arrays,
									android.R.layout.simple_spinner_item);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					sIcon.setAdapter(adapter);
					// Components of the dialog
					final EditText addpoitextbox = (EditText) dialog
							.findViewById(R.id.writename);

					sIcon = (Spinner) dialog.findViewById(R.id.iconChooser);

					Button ok_add_favourites = (Button) dialog
							.findViewById(R.id.addfavouritesOk);
					Button cancel_add_favourites = (Button) dialog
							.findViewById(R.id.addfavouritesCancel);

					// Close dialog
		ok_add_favourites.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {

							String poiname = addpoitextbox.getText().toString();

							System.out.println(poiname);
							if (poiname.equals("")) {
								Toast.makeText(myContext,
										"Please insert a valid name",
										Toast.LENGTH_SHORT).show();
							} else {

								
								
								addPOI(poiname, poiLon, poiLat, (int)sIcon.getSelectedItemId());
								invalidate();
								toggleAddPOIMode();
								
								dialog.dismiss();
								
							}
						}
					});

		cancel_add_favourites.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					dialog.show();

					
					
				}

			} else if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
				isZooming = true;
			}
			return true;
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	try{
			if (!_threadStreets.isRunning()) {
				_threadStreets.setRunning(true);
				_threadStreets.start();
			}
	}
	catch(Exception e)
	{}

}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// simply copied from sample application LunarLander:
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode
		boolean retry = true;
		_threadStreets.setRunning(false);

		while (retry) {
			try {
				_threadStreets.join();
				retry = false;
			} catch (InterruptedException e) {
				// we will try it again and again...
			}
		}

	}

	/**
	 * 
	 * Handles the Rendering of the map
	 * 
	 */
	class RenderingThread extends Thread {

		CustomMapView _cmv;
		Boolean _run = false;
		private SurfaceHolder _surfaceHolder;
		
		public RenderingThread(SurfaceHolder surfaceHolder, CustomMapView cmv) {
			super();
			_cmv = cmv;
			_surfaceHolder = surfaceHolder;
		}

		public void setRunning(boolean run) {

			_run = run;

		}

		public boolean isRunning() {

			return _run;

		}
		
		public SurfaceHolder getSurfaceHolder() {
			return _surfaceHolder;
		}

		@Override
		public void run() {
			while (_run) {
				try {
					
					Log.d("CustomMapView", "Retrieving...");
					new StreetRunnable(_cmv).run();
					Log.d("CustomMapView", "Retrieving");

					if(!followRotation)
						rotation = 0;
					else
						rotation = gaia.getOrientation();
					
					Log.d("CustomMapView", "Updating...");
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							_cmv.invalidate();
						}

					});

					Log.d("CustomMapView", "Updated");
					}
				 finally {
				}
			}
		}

	}

	/**
	 * Retrieves the data for the map
	 * 
	 */
	static public class StreetRunnable implements Runnable {

		CustomMapView _cmv;

		public StreetRunnable(CustomMapView cmv) {
			_cmv = cmv;
		}

		public void run() {

			Log.d(LOG_TAG, "Getting street");
			_cmv.getGaiaItems();
			if(drivingMode)
				runTarget(gaia.getCurPosition());

			Log.d(LOG_TAG, "Got Streets");

		}

	}

	@Override
	public void onLocationChanged(Location loc) {

		if (loc != null) {

			// Precision: 58.4776xxx

			lat = (int) (loc.getLatitude() * 1E6);
			lon = (int) (loc.getLongitude() * 1E6);
			
			if(followLocation)
			gaia.setCurPosition(lon, lat);

			translateX = -lat * scale - SCREEN_WIDTH / 2;
			translateY = -lon * scale + SCREEN_HEIGHT / 2;

			
			this.invalidate();
		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/**
	 * Renders the marker at the current position
	 * 
	 * @param canvas	
	 * 			where to draw the marker
	 */
	protected void drawMarker(Canvas canvas) {


		canvas.rotate(+rotation, lat, lon);
		canvas.rotate(+90, lat, lon);
		
		canvas.scale(1 / (scale), 1 / (scale), lat, lon);

		Bitmap marker = BitmapFactory.decodeResource(getResources(),
				R.drawable.rotation_arrow_blue);

		// Rendering POI image
		canvas.drawBitmap(marker, lat, lon, pt);

		canvas.scale(scale, scale, lat, lon);

		canvas.rotate(-90, lat, lon);
		canvas.rotate(-rotation, lat, lon);

	}

	
	/**
	 * Handles the zoom in and zoom out
	 * 
	 *  @param index
	 *  			+1: ZoomIn, -1: ZoomOut
	 */

	protected void setZoom(int index) {

		if (index == 1) {
			if (scale > 0.1f && scale < 0.9f) {
				translateX = (translateX + SCREEN_HEIGHT / 2) / scale;
				translateY = (translateY - SCREEN_WIDTH / 2) / scale;

				scale += 0.1f;
				gaia.setCurScale(scale);
				
				translateX = translateX * scale - SCREEN_HEIGHT / 2;
				translateY = translateY * scale + SCREEN_WIDTH / 2;

			}
			else if(scale < 0.1f && scale < 0.9f )
			{
				translateX = (translateX + SCREEN_HEIGHT / 2) / scale;
				translateY = (translateY - SCREEN_WIDTH / 2) / scale;

				scale += 0.01f;
				gaia.setCurScale(scale);
				
				translateX = translateX * scale - SCREEN_HEIGHT / 2;
				translateY = translateY * scale + SCREEN_WIDTH / 2;
				
			}
				
				
				else
				Toast.makeText(getContext(), "Max ZoomIn " + scale,
						Toast.LENGTH_SHORT).show();

		}

		else if (index == -1) {
			if (scale > 0.1f) {
				translateX = (translateX + SCREEN_HEIGHT / 2) / scale;
				translateY = (translateY - SCREEN_WIDTH / 2) / scale;

				scale -= 0.1f;
				gaia.setCurScale(scale);
				
				translateX = translateX * scale - SCREEN_HEIGHT / 2;
				translateY = translateY * scale + SCREEN_WIDTH / 2;

			} else if (scale > 0.01f) {

				translateX = (translateX + SCREEN_HEIGHT / 2) / scale;
				translateY = (translateY - SCREEN_WIDTH / 2) / scale;

				scale -= 0.01f;
				gaia.setCurScale(scale);
				
				translateX = translateX * scale - SCREEN_HEIGHT / 2;
				translateY = translateY * scale + SCREEN_WIDTH / 2;
			} else
				Toast.makeText(getContext(), "Max ZoomOut " + scale,
						Toast.LENGTH_SHORT).show();
		}

		this.invalidate();
	}

	/**
	 * Puts the marker at the center of the screen
	 * 
	 */
	protected void showLocation() {


		if (showLocation)
			setCenterAt(lat, lon);

		this.invalidate();
	}
	
	/**
	 * Enables/Disables the rotation of the map
	 * 
	 * @param b
	 * 			true or false
	 * 
	 */
	protected void followRotation(boolean b) {

		followRotation = b;

		this.invalidate();
	}

	/**
	 * Set the center of the map at the given lat, lon
	 * 
	 * @param lat
	 * 				Latitude
	 * 
	 * @param lon
	 * 				Longitude
	 * 			
	 */
	protected void setCenterAt(float lat, float lon) {

		translateX = -(lat * scale) - (SCREEN_HEIGHT / 2);
		translateY = -(lon * scale) + (SCREEN_WIDTH / 2);
		this.invalidate();

	}
	
	/**
	 * returns the latitude of the center
	 * 
	 * @return _lat
	 * 				the latitude of the center
	 * 			
	 */
	protected float getCenterLat() {

		float _lat;

		_lat = (-translateX - (SCREEN_HEIGHT / 2)) / scale;
		
		
		return _lat;

	}
	
	/**
	 * returns the longitude of the center
	 * 
	 * @return _lon
	 * 				the longitude of the center
	 * 			
	 */
	protected float getCenterLon() {

		float _lon;

		_lon = (-translateY + (SCREEN_WIDTH / 2)) / scale;

		return _lon;

	}

	/**
	 * Gets all the data for the map rendering, from the Database
	 * 			
	 */
	public void getGaiaItems() {
		gaia.setStreets(myDbHelper.getStreets(myContext, getCenterLat(),
				getCenterLon(), range, scale));
		
		streetname = getStreetName(sensibility);
		
		GaiaActivity.mHandler.post(new Runnable() {
			@Override
			public void run() {
				GaiaActivity.tvStreetName.setText(streetname);
			}

		});
		
		gaia.setPOIs(myDbHelper.getPoints(myContext, getCenterLat(), getCenterLon(), range, scale, gaia.POIstate, gaia.POIicon));
		System.out.println(gaia.getPOIs().size());
		
		gaia.setPolygons(myDbHelper.getPolygons(myContext, getCenterLat(), getCenterLon(), range, scale));
		
		gaia.setPathPoints(myDbHelper.getPathPoints(myContext));
		
		gaia.setPaths(myDbHelper.getPaths(myContext));
		
		//Log.d("Paths", "Got Paths");
	}

	/**
	 * 
	 * set the given map rotation degree
	 * 
	 * @param rot
	 * 				the degree of the map rotation
	 * 			
	 */
	public void setRotation(int rot) {
		rotation = rot;
	}

	/**
	 * 
	 * initialises the database given the selected map
	 * 			
	 */
	public void startupDB() {

		try {
			
			
			
			
			if (GaiaActivity.map.equals("Västra Götaland")) {
				lat = 57709441;
				lon = 11924848;

				gaia.setCurPosition(lon, lat);
				
				myDbHelper = new SQLiteAdapter(myContext, "osmvgdb.kpx");
			}
			
			if (GaiaActivity.map.equals("Göteborg")) {				
				
				lat = 57709441;
				lon = 11941968;
				
				gaia.setCurPosition(lon, lat);
				
				myDbHelper = new SQLiteAdapter(myContext, "osmlndb.kpx");
				
			}
			if (GaiaActivity.map.equals("Piovene Rocchette")) {
				lat = 45761466;
				lon = 11431262;
				
				gaia.setCurPosition(lon, lat);
				
				myDbHelper = new SQLiteAdapter(myContext, "osmprdb.kpx");
			}
			if (GaiaActivity.map.equals("Marano Vicentino")) {
				lat = 45692912;
				lon = 11428332;

				gaia.setCurPosition(lon, lat);
				
				myDbHelper = new SQLiteAdapter(myContext, "osmmvdb.kpx");

			}

			myDbHelper.createDataBase();
			myDbHelper.openDataBase();
			gaia.setStreetNames(myDbHelper.getAddress(myContext));
			gaia.setStop_Landmark(myDbHelper.getLocation(myContext));

		} catch (Exception ioe) {

			throw new Error("Unable to create database");

		}

	}

	/**
	 * 
	 * gets the current street name, trying recursively to see which is the nearest street 
	 * 
	 * @param sensibility
	 * 						the sensibility for searching for the nearest street
	 * 			
	 * @return getClosestStreet
	 * 							the name of the closest street found
	 */
	public String getStreetName(int sensibility) {
		int sens = sensibility;

		boolean found = false;
		ArrayList<String> streetNames = new ArrayList<String>();
		ArrayList<Double> streetDistance = new ArrayList<Double>();

		for (int j = 0; j < gaia.getStreets().size(); j++) {
			for (int i = 1; i < gaia.getStreets().get(j).getSize(); i++) {
				GaiaPoint p1 = new GaiaPoint(gaia.getStreets().get(j)
						.getPoint(i - 1).getLongitude(), gaia.getStreets()
						.get(j).getPoint(i - 1).getLatitude());

				GaiaPoint p2 = new GaiaPoint(gaia.getStreets().get(j)
						.getPoint(i).getLongitude(), gaia.getStreets().get(j)
						.getPoint(i).getLatitude());

				float _lat = ((((p2.getLatitude() - p1.getLatitude()) / (p2
						.getLongitude() - p1.getLongitude()))) * (lon - p1
						.getLongitude()))
						+ p1.getLatitude();

				if (_lat + sens >= lat && _lat - sens <= lat) {

					if (!gaia.getStreets().get(j).getName().equals("")
							&& !gaia.getStreets().get(j).getName()
									.equals("no name")) {
						streetDistance.add(Math.sqrt(Math.pow((p2.getLatitude() - p1.getLatitude()),2)
										+ Math.pow((p2.getLongitude() - p1.getLongitude()), 2)));
						streetNames.add(gaia.getStreets().get(j).getName());
						found = true;
					}

				}
			}
		}

	
		if (sens < 5000 && !found)
			return getStreetName(sens + 50);
		else {

			return getClosestStreet(streetDistance, streetNames);

		}
	}

	/**
	 * 
	 * returns the name of the closest street
	 * 
	 * @param dists
	 * 				arraylist of all the distances found in range
	 * @param streets
	 * 				arraylist of all the street names
	 * 						
	 * 			
	 * @return String
	 * 			the name of the closest street found or empty if not found
	 */
	private String getClosestStreet(ArrayList<Double> dists,
			ArrayList<String> streets) {
		if (dists.size() > 0) {
			int index = 0;
			Double closest = dists.get(index);

			for (int i = 1; i < dists.size(); i++) {

				if (closest > dists.get(i)) {
					closest = dists.get(i);
					index = i;
				}
			}

			return streets.get(index);
		} else
			return "";
	}
	
	/**
	 * 
	 * enables or disables the driving mode
	 * 
	 * @param b
	 * 			true or false
	 */
	static public void setDrivingMode(boolean b)
	{
		drivingMode = b;
	}
	
	/**
	 * 
	 * toggles the add POI feature
	 * 
	 * 
	 */
	static public void toggleAddPOIMode()
	{
		if(addPOIMode)
			addPOIMode = false;
		else
			addPOIMode = true;
		
		
		System.out.println(addPOIMode);
		

        
	}
	
	/**
	 * 
	 * enables or disables the driving mode
	 * 
	 * @param name
	 * 				the name of the POI
	 * @param lon
	 * 				longitude
	 * @param lat
	 * 				latitude
	 * @param id
	 * 				its ID
	 */
	private void addPOI(String name, float lon, float lat, int id)
	{
	
		gaia.addPOI(new GaiaPOI(name, new GaiaPoint(lon, lat), id));
	}
	
	/**
	 * Get nodes, turn into degrees for driving mode instructions. 
	 * 
	 */
	
	public void sayTurn(ArrayList<GaiaPath> theList){

		int latx = theList.get(0).getFirstNode().getLatitude();
		int laty = theList.get(0).getSecondNode().getLatitude();
		int latz = theList.get(1).getSecondNode().getLatitude();


		int lonx = theList.get(0).getFirstNode().getLongitude();
		int lony = theList.get(0).getSecondNode().getLongitude();
		int lonz = theList.get(1).getSecondNode().getLongitude();


		double degree1 = Math.atan((lony - lonx) / (laty - latx)) * 57.2957795;
		double degree2 = Math.atan((lonz - lony) / (latz - laty)) * 57.2957795;
		double degree3 = degree2-degree1;
        int degree = (int) degree3;
        
        System.out.println(degree); 
        
        if (degree > 60 && degree < 120){

		sayTurnLeft();

		}

		if(degree < -60 && degree > -120){

		sayTurnRight();
         
		}
		
		if(degree < 60 && degree > 30) {
			sayGoUpLeft();
		}
		
		if (degree > -60 && degree < -30) {
			sayGoUpRight();
		}
		
		if(degree > 120 && degree > 150) {
			sayGoDownLeft();
		}
		
		if (degree < -120 && degree > -150) {
			sayGoDownRight();
		}
		
		if (degree > 150 && degree < -150) {
			sayGoDown();
		}
		
		
		else

		sayKeepGoing();

		}

		private void sayTurnLeft(){
			
			GaiaActivity.changeArrow(R.drawable.left_arrow);
			
		}

		private void sayTurnRight(){
			
			GaiaActivity.changeArrow(R.drawable.right_arrow);
			
		}
		
		private void sayGoUpLeft(){
			
			GaiaActivity.changeArrow(R.drawable.lean_left_arrow);

        }
		
		private void sayGoUpRight(){
			
			GaiaActivity.changeArrow(R.drawable.lean_right_arrow);

		}
		
		private void sayGoDownLeft(){
			
			GaiaActivity.changeArrow(R.drawable.uturn_left_arrow);

        }
		
		private void sayGoDownRight(){ 
			
			GaiaActivity.changeArrow(R.drawable.uturn_right_arrow);

		}
		
		private void sayGoDown(){

			GaiaActivity.changeArrow(R.drawable.down_arrow);

		}

		private void sayKeepGoing(){

			GaiaActivity.changeArrow(R.drawable.up_arrow);

		}
}

