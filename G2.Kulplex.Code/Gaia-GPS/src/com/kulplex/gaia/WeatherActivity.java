/**
 * WeatherActivity
 * 
 * This Activity handles the weather.
 * 
 * The code is based on the Android weather forecast, provided on http://www.anddev.org/advanced-tutorials-f21/android-weather-forecast-google-weather-api-description-t337-45.html
 * And adapted for our Weather feature
 * 
 * @author Henry Dang 
 * 
 */

package com.kulplex.gaia;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.kulplex.gaia.handler.GoogleWeatherHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WeatherActivity extends Activity {

	private final String DEBUG_TAG = "WeatherForcaster";
	private CheckBox temp_scale = null;
	
	GaiaApp gaia;
	LinearLayout background;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.weatherforecast);

        gaia = ((GaiaApp)getApplicationContext());
        
        background = (LinearLayout) findViewById(R.id.background);
        
        themeHandler();
        
		this.temp_scale = (CheckBox) findViewById(R.id.temp_scale);

		Button submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				showWeather();
			}
		});
        
        ImageView back = (ImageView)findViewById(R.id.back);
		 
		 
		 back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(WeatherActivity.this,
						SocialActivity.class);
				startActivity(intent);
				finish(); 
				
			}
		});

			showWeather();
	}

	private void updateWeatherInfoView(int aResourceID,
			WeatherNextCondition aWFIS) throws MalformedURLException {
		
		/* Construct the Image-URL. */
		URL imgURL = new URL("http://www.google.com" + aWFIS.getIconURL());
		((WeatherInfoView) findViewById(aResourceID)).setRemoteImage(imgURL);

		int tempMin = aWFIS.getTempMinCelsius();
		int tempMax = aWFIS.getTempMaxCelsius();

		/* Convert from Celsius to Fahrenheit if necessary. */
		if (this.temp_scale.isChecked()) {
			((WeatherInfoView) findViewById(aResourceID))
					.setTempCelciusMinMax(tempMin, tempMax);
		} else {
			tempMin = WeatherUtils.CtF(tempMin);
			tempMax = WeatherUtils.CtF(tempMax);
			((WeatherInfoView) findViewById(aResourceID))
					.setTempFahrenheitMinMax(tempMin, tempMax);
		}
		
	}

	private void updateWeatherInfoView(int aResourceID,
			WeatherCurrentCondition aWCIS) throws MalformedURLException {
		
		/* Construct the Image-URL. */
		URL imgURL = new URL("http://www.google.com" + aWCIS.getIconURL());
		((WeatherInfoView) findViewById(aResourceID)).setRemoteImage(imgURL);

		/* Convert from Celsius to Fahrenheit if necessary. */
		if (this.temp_scale.isChecked()){
			((WeatherInfoView) findViewById(aResourceID))
					.setTempCelcius(aWCIS.getTempCelcius());
		}else{
			((WeatherInfoView) findViewById(aResourceID))
					.setTempFahrenheit(aWCIS.getTempFahrenheit());
		}
	}

	private void showWeather(){
		
		URL url;
		try {
			/* Get what user typed to the EditText. */
			String cityString = ((EditText) findViewById(R.id.city_country))
					.getText().toString();
			String queryString = "http://www.google.com/ig/api?weather="
					+ cityString;
			/* Replace blanks with HTML-Equivalent. */
			url = new URL(queryString.replace(" ", "%20"));

			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();

			/*
			 * Create a new ContentHandler and apply it to the
			 * XML-Reader
			 */
			GoogleWeatherHandler gwh = new GoogleWeatherHandler();
			xr.setContentHandler(gwh);

			/* Parse the xml-data our URL-call returned. */
			xr.parse(new InputSource(url.openStream()));

			/* Our Handler now provides the parsed weather-data to us. */
			WeatherSet ws = gwh.getWeatherSet();

			/* Update the SingleWeatherInfoView with the parsed data. */
			updateWeatherInfoView(R.id.weather_today, ws
					.getWeatherCurrentCondition());

			updateWeatherInfoView(R.id.weather_1, ws
					.getWeatherForecastConditions().get(0));
			updateWeatherInfoView(R.id.weather_2, ws
					.getWeatherForecastConditions().get(1));
			updateWeatherInfoView(R.id.weather_3, ws
					.getWeatherForecastConditions().get(2));
			updateWeatherInfoView(R.id.weather_4, ws
					.getWeatherForecastConditions().get(3));

		} catch (Exception e) {
			resetWeatherInfoViews();
			Log.e(DEBUG_TAG, "QueryError " + "In " + "Weather", e);
		}
	}
	private void resetWeatherInfoViews() {
		((WeatherInfoView)findViewById(R.id.weather_today)).reset();
		((WeatherInfoView)findViewById(R.id.weather_1)).reset();
		((WeatherInfoView)findViewById(R.id.weather_2)).reset();
		((WeatherInfoView)findViewById(R.id.weather_3)).reset();
		((WeatherInfoView)findViewById(R.id.weather_4)).reset();
	}

/**
 * This method handles the theme
 */
public void themeHandler(){
		
		
		if(gaia.getChosenTheme().equals("Black Theme"))
		{
			
			background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_black));
			
		}
				
		if(gaia.getChosenTheme().equals("Blue Theme"))
		{
		
			background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_blue));
			
		}
		else
			if(gaia.getChosenTheme().equals("Orange Theme"))
			{
				
				background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_orange));
			}
		else
			if(gaia.getChosenTheme().equals("Green Theme"))
			{
				
				background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_green));
			}
		else
			if(gaia.getChosenTheme().equals("Purple Theme"))
			{
				
				background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_purple));
			}
		
		
	}

}