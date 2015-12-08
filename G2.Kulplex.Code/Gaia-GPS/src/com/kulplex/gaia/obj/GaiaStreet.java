/**
 * GaiaStreet
 * 
 * This object contains a single street, containing all the segments of it
 * 
 * @author Alberto Vaccari
 * 
 */

package com.kulplex.gaia.obj;

import java.util.ArrayList;


public class GaiaStreet{
	
	String name;
	String type;
	ArrayList<GaiaPoint> street;
	
	
	public GaiaStreet(String name, String type)
	{
		if(name != null)
			this.name = name;
		else
			this.name = "no name";
		
		if(type != null)
			this.type = type;
		else
			this.type = "";
		
		street = new ArrayList<GaiaPoint>();
		
	}

	public String getName()
	{
		
		return name;
	}
	
	public String getType()
	{
		
		return type;
	}
	
	public ArrayList<GaiaPoint> getStreet()
	{
		
		return street;
	}
	
	public boolean isType(String name)
	{
		
		return type.equals(name);
	}
	
	public int getSize()
	{
		
		return street.size();
	}
	
	public void addNode(GaiaPoint gp)
	{
		
		street.add(gp);
	}
	
	public GaiaPoint getPoint(int index){
		
		return street.get(index);
	}

}
