/**
 * WeatherUtils
 * 
 * Useful Utility in working with temperatures. (conversions)
 * 
 * The code is based on the Android weather forecast, provided on http://www.anddev.org/advanced-tutorials-f21/android-weather-forecast-google-weather-api-description-t337-45.html
 * And adapted for our Weather feature
 * 
 * @author Henry Dang
 * 
 */

package com.kulplex.gaia;

public class WeatherUtils {

	public static int CtF(int tCelsius) {
		return (int) ((9.0f / 5.0f) * tCelsius + 32);
	}
	
	public static int FtC(int tFahrenheit) {
		return (int) ((5.0f / 9.0f) * (tFahrenheit - 32));
	}
}
