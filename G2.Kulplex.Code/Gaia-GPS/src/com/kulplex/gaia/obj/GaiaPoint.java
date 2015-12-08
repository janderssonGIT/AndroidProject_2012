/**
 * GaiaPoint
 * 
 * This object contains a single point for the map
 * 
 * @author Alberto Vaccari
 * 
 */
package com.kulplex.gaia.obj;

public class GaiaPoint {
	
	float lon;
	float lat;
	

	
	public GaiaPoint(float lon, float lat)
	{
		this.lon = lon;
		this.lat = lat;
		
	}
	

	
	public float getLongitude()
	{
		
		return lon;
	}
	
	public float getLatitude()
	{
		
		return lat;
	}
	

}
