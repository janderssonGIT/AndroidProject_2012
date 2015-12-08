/**
 * GaiaPath
 * 
 * This object contains the Path for pathfinder
 * 
 * @author Hasan Yahya
 * 
 */


package com.kulplex.gaia.obj;

import java.util.ArrayList;


public class GaiaPath {

	String edge;
	int weight = 1;
	
	GaiaPathPoint n1;
	GaiaPathPoint n2;
	
	ArrayList<GaiaPathPoint> neighboursList;
	
	public GaiaPath(String edge, GaiaPathPoint node1, GaiaPathPoint node2)
	{
		this.edge = edge;
		this.weight = calcWeight(node1, node2);
		n1 = node1;
		n2 = node2;
		
		neighboursList = new ArrayList<GaiaPathPoint>();
	}
	

	@Override
	public String toString() {
		return "Edge: " + edge + ", weight: " + weight + ", node1: " + n1.getId() + " " + n1.getName() + ", node2: " + n2.getId() + " " + n2.getName();
		
	}
	
	public int getWeight()
	{
		return weight;
	}
	
	public String getFirstNodeId()
	{
		return n1.getId();
	}
	
	public String getSecondNodeId()
	{
		return n2.getId();
	}
	
	public GaiaPathPoint getFirstNode()
	{
		return n1;
	}
	
	public GaiaPathPoint getSecondNode()
	{
		return n2;
	}
	
	private int calcWeight(GaiaPathPoint n1, GaiaPathPoint n2){
		
		String[] coor1 = n1.getName().split(",");
		double lat1 = (double)Integer.parseInt(coor1[0]);
		double lon1 = (double)Integer.parseInt(coor1[1]);
		
		String[] coor2 = n2.getName().split(",");
		double lat2 = (double)Integer.parseInt(coor2[0]);
		double lon2 = (double)Integer.parseInt(coor2[1]);
		
		double toRad = (180 / Math.PI);

		   
			double latDif = (lat2 - lat1) * toRad;
			double lonDif = (lon2 - lon1) * toRad;
		    double a = Math.pow(Math.sin(latDif / 2.0), 2)
		            + Math.cos(lat1 * toRad)
		            * Math.cos(lat2 * toRad)
		            * Math.pow(Math.sin(lonDif / 2.0), 2);
		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		    double d = 6367 * c / 100;

		return (int)d;
		
	}
	
	
	
	
}
