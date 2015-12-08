/**
 * WeatherNextCondition
 * 
 * Holds the information between the <forecast_conditions>-tag of what the
 * Google Weather API returned. 
 * 
 * The code is based on the Android weather forecast, provided on http://www.anddev.org/advanced-tutorials-f21/android-weather-forecast-google-weather-api-description-t337-45.html
 * And adapted for our Weather feature
 * 
 * @author Henry Dang
 * 
 */

package com.kulplex.gaia;

public class WeatherNextCondition {

	private String dayofWeek = null;
	private Integer tempMin = null;
	private Integer tempMax = null;
	private String iconURL = null;
	private String condition = null;
	
	public WeatherNextCondition() {

	}

	public String getDayofWeek() {
		return dayofWeek;
	}

	public void setDayofWeek(String dayofWeek) {
		this.dayofWeek = dayofWeek;
	}

	public Integer getTempMinCelsius() {
		return tempMin;
	}

	public void setTempMinCelsius(Integer tempMin) {
		this.tempMin = tempMin;
	}

	public Integer getTempMaxCelsius() {
		return tempMax;
	}

	public void setTempMaxCelsius(Integer tempMax) {
		this.tempMax = tempMax;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
