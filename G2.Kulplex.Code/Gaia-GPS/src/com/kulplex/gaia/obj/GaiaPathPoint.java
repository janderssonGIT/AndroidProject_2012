/**
 * GaiaPathPoint
 * 
 * This object contains the Points of the Path for pathfinder
 * 
 * @author Hasan Yahya
 * 
 */

package com.kulplex.gaia.obj;

import java.util.ArrayList;

public class GaiaPathPoint {
	
	String name;
	String id;
	
	boolean visited = false;

	
	public GaiaPathPoint(String id, String nodename)
	{
		
		this.id = id;
		this.name = nodename;


	}

	
	public String getName(){
		
		return name;
	}
	
	public String getId(){
		
		return id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id.hashCode();
		return result;
	}
	
	public boolean existsIn(ArrayList<GaiaPathPoint> ar){
		
		for(int i = 0; i<ar.size(); i++){
			
			if(name.equals(ar.get(i).getName())){
				
				return true;
			}
						
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		//Checking whether the given object is the same exact NodeItem
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GaiaPathPoint n = (GaiaPathPoint) obj;
		if (id == null) {
			if (n.getId() != null)
				return false;
		} else if (!id.equals(n.getId()))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public int getLatitude(){
		
		String splitter[] = name.split(",");
		
		return Integer.parseInt(splitter[0]);
		
	}

	public int getLongitude(){
		
		String splitter[] = name.split(",");
		
		return Integer.parseInt(splitter[1]);
		
	}
	
}
