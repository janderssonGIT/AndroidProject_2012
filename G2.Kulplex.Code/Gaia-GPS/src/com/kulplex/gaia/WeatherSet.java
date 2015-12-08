/**
 * WeatherSet
 * 
 * Combines one WeatherCurrentCondition with a List of
 * WeatherForecastConditions.
 * 
 * The code is based on the Android weather forecast, provided on http://www.anddev.org/advanced-tutorials-f21/android-weather-forecast-google-weather-api-description-t337-45.html
 * And adapted for our Weather feature
 * 
 * @author Henry Dang
 * 
 */

package com.kulplex.gaia;

import java.util.ArrayList;

public class WeatherSet {	

	private WeatherCurrentCondition myCurrentCondition = null;
	private ArrayList<WeatherNextCondition> myForecastConditions = 
		new ArrayList<WeatherNextCondition>(4);

	public WeatherCurrentCondition getWeatherCurrentCondition() {
		return myCurrentCondition;
	}

	public void setWeatherCurrentCondition(
			WeatherCurrentCondition myCurrentWeather) {
		this.myCurrentCondition = myCurrentWeather;
	}

	public ArrayList<WeatherNextCondition> getWeatherForecastConditions() {
		return this.myForecastConditions;
	}

	public WeatherNextCondition getLastWeatherForecastCondition() {
		return this.myForecastConditions
				.get(this.myForecastConditions.size() - 1);
	}
}
