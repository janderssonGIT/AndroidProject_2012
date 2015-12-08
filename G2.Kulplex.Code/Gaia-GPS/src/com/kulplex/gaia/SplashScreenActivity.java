/**
 * SplashScreenActivity
 * 
 * This class handles the initial SplashScreen and its loading
 * 
 * 
 * @author Alberto Vaccari
 * 
 */


package com.kulplex.gaia;

import com.kulplex.gaia.handler.SQLiteAdapter;
import com.kulplex.gaia.obj.GaiaPoint;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.content.SharedPreferences;

public class SplashScreenActivity extends Activity {
	SharedPreferences settings;
	TextView loadingtv;
	int i = 1;
	GaiaApp gaia;
	SQLiteAdapter myDbHelper;

	int lat;
	int lon;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		settings = getSharedPreferences(SettingsActivity.prefsFile, 0);
		super.onCreate(savedInstanceState);
		gaia = ((GaiaApp) getApplicationContext());
		// Keeps screen alive
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.splashlayout);

		new LoadViewTask().execute();

	}

	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		@Override
		protected void onPreExecute() {

			loadingtv = (TextView) findViewById(R.id.loadingStatus);
			loadingtv.setText("Loading resources...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			synchronized (this) {
				publishProgress(0);

				getPrefs();

				publishProgress(1);

				// Placeholder for other
				try {
					this.wait(1000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				publishProgress(2);

				startupDB();

				publishProgress(3);

				getPoints();

				publishProgress(4);

				getStreets();

				publishProgress(5);

				getPolygons();

				// Launcing activity

			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			switch (i) {
			case 0:
				loadingtv.setText("Launching processes..." + i + "/6");
				break;
			case 1:
				loadingtv.setText("Loading settings..." + i + "/6");
				break;
			case 2:
				loadingtv.setText("Loading resources..." + i + "/6");
				break;
			case 3:
				loadingtv.setText("Getting points..." + i + "/6");
				break;
			case 4:
				loadingtv.setText("Getting streets..." + i + "/6");
				break;
			case 5:
				loadingtv.setText("Getting polygons..." + i + "/6");
				break;
			default:
				loadingtv.setText("Starting application..." + i + "/6");
				break;

			}

			i++;
		}

		@Override
		protected void onPostExecute(Void result) {
			myDbHelper.close();
			Intent intent = new Intent(SplashScreenActivity.this,
					GaiaActivity.class);

			startActivity(intent);
			finish();
		}
	}

	/**
	 * 
	 * gets saved settings
	 * 			
	 */
	private void getPrefs() {
		
		if(!settings.contains("selectedThemeName"))
			gaia.setFirstRun(true);
		else
			gaia.setFirstRun(false);
				
		gaia.chooseTheme(settings.getString("selectedThemeName", "Black Theme"));
		gaia.setIdTheme(settings.getInt("selectedThemeId", 0));
		gaia.chooseMap(settings.getString("selectedMapName", "Göteborg"));
		gaia.setIdMap(settings.getInt("selectedMapId", 1));

		gaia.setCustomPOISet(settings.getStringSet("FavSet", null));
		gaia.setCustomPOI(gaia.setToList(gaia.getCustomPOISet()));
		
	}
	
	/**
	 * 
	 * initialises the database given the selected map
	 * 			
	 */
	public void startupDB() {

		try {
			
			if (gaia.chosenMap.equals("Göteborg")) {
				lat = 57709441;
				lon = 11924848;
				myDbHelper = new SQLiteAdapter(this, "osmgbdb.kpx");
			}
			if (gaia.chosenMap.equals("Västra Götaland")) {
				lat = 57709441;
				lon = 11924848;
				myDbHelper = new SQLiteAdapter(this, "osmvgdb.kpx");
			}
			if (gaia.chosenMap.equals("Piovene Rocchette")) {
				lat = 45761466;
				lon = 11431262;

				myDbHelper = new SQLiteAdapter(this, "osmprdb.kpx");
			}
			if (gaia.chosenMap.equals("Marano Vicentino")) {
				lat = 45692912;
				lon = 11428332;
				myDbHelper = new SQLiteAdapter(this, "Osmmvdb.kpx");

			}

			gaia.setCurPosition(lon, lat);
			
			myDbHelper.createDataBase();
			myDbHelper.openDataBase();

		} catch (Exception ioe) {

			throw new Error("Unable to create database");

		}

	}

	/**
	 * 
	 * gets points
	 * 			
	 */
	private void getPoints() {
		gaia.setPOIs(myDbHelper.getPoints(this, lat, lon, 100000,
				0.8f, gaia.POIstate, gaia.POIicon));

	}

	/**
	 * 
	 * gets streets
	 * 			
	 */
	private void getStreets() {
		gaia.setStreets(myDbHelper.getStreets(this, lat, lon, 100000,
				0.8f));
		
		gaia.setStreetNames(myDbHelper.getAddress(this));

	}

	/**
	 * 
	 * gets polygons
	 * 			
	 */
	private void getPolygons() {
		gaia.setPolygons(myDbHelper.getPolygons(this, lat, lon,
				100000, 0.8f));

	}

}
