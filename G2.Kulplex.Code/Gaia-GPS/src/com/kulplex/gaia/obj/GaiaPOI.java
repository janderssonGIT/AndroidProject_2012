/**
 * GaiaPOI
 * 
 * This object contains the POI for the map
 * 
 * @author Alberto Vaccari
 * 
 */

package com.kulplex.gaia.obj;

import com.kulplex.gaia.R;


public class GaiaPOI {
	
	String name;
	String type;
	
	float lon;
	float lat;
	
	Integer icon;
	
	public GaiaPOI(String name, String type, float lon, float lat, Integer icon)
	{
		if(name != null)
			this.name = name;
		else
			this.name = "";
		
		if(type != null)
			this.type = type;
		else
			this.type = "";
		
		this.lon = lon;
		this.lat = lat;
		this.icon = icon;
	}
	
	public GaiaPOI(String name, GaiaPoint gp, Integer icon)
	{
		this.name = name;
		this.lon = gp.getLongitude();
		this.lat = gp.getLatitude();
		this.icon = icon;
	}
	
	public GaiaPOI(String name, GaiaPoint gp, int id)
	{
		this.name = name;
		this.lon = gp.getLongitude();
		this.lat = gp.getLatitude();
		
		switch(id)
		{
			case 0: this.icon = R.drawable.custompoi1; break;
			case 1: this.icon = R.drawable.custompoi2; break;
			case 2: this.icon = R.drawable.custompoi3; break;
			default: this.icon = R.drawable.custompoi; break;
		}
		
		
		
	}
	
	
	public String getName()
	{
		return name;
	}
	
	public String getType()
	{
		return type;
	}
	
	public float getLongitude()
	{
		return lon;
	}
	
	public float getLatitude()
	{
		return lat;
	}
	
	public Integer getIcon()
	{
		return icon;
	}
	
	public int getIc()
	{		
		if(icon == R.drawable.custompoi1)
			return 0;
		else if(icon == R.drawable.custompoi2)
			return 1;
		else if(icon == R.drawable.custompoi3)
			return 2;
		else 
			return 3;
		
	}
}
