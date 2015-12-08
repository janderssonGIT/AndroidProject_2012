/**
 * GaiaPolygon
 * 
 * This object contains a single polygon for the map
 * 
 * @autho Alberto Vaccari
 * 
 */

package com.kulplex.gaia.obj;

import java.util.ArrayList;

public class GaiaPolygon {
	
	String name;
	ArrayList<GaiaPoint> poly;
	
	public GaiaPolygon(String name)
	{
		poly = new ArrayList<GaiaPoint>();
		
		if(name != null)
			this.name = name;
		else
			this.name = "";
	}
	
	
	public void addNode(GaiaPoint p){
		
		poly.add(p);
		
	}
	
	public ArrayList<GaiaPoint> getPolygon()
	{
		
		return poly;
	}
	
	public int getSize()
	{
		
		return poly.size();
	}

}
