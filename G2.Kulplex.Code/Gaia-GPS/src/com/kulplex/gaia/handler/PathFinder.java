/**
 * PathFinder
 * 
 * This class handles the pathfinding
 * 
 * @author Hasan Yahya
 * 
 */

package com.kulplex.gaia.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.kulplex.gaia.GaiaApp;
import com.kulplex.gaia.CustomMapView;
import com.kulplex.gaia.obj.GaiaPath;
import com.kulplex.gaia.obj.GaiaPathPoint;
import com.kulplex.gaia.obj.Graph;

import android.content.Context;


/**
 * This class creates a Map with all nodes and edges, and creates a route using Dijkstra's Algorithm.
 * 
 * @author Hasan Yahya
 *
 */
public class PathFinder{

	GaiaApp gaia;
	Context myContext;

	DijkstraAlgorithm djk;
	Graph graph;
	
	/**
	 * Creates the node-map
	 * 
	 * @param gaia - Application Logic
	 * @param gpp - List of nodes
	 * @param gp - List of edges
	 * @param target - target node
	 * @param start - source node
	 * @return
	 */
	public Map<GaiaPathPoint, GaiaPathPoint> pathFind(GaiaApp gaia, ArrayList<GaiaPathPoint> gpp, ArrayList<GaiaPath> gp, GaiaPathPoint target, GaiaPathPoint start) {
		
		Map<GaiaPathPoint, GaiaPathPoint> pathMap = new HashMap<GaiaPathPoint, GaiaPathPoint>();
		
		System.out.println(gpp.size() + " gpp");
		System.out.println(gp.size() + " gp");

		this.gaia = gaia;
	
		//Creates a graph with the nodes and paths that have been loaded
		graph = new Graph(gpp,
				gp);
		
		gaia.setGraph(graph);

		djk = new DijkstraAlgorithm(graph);

		
				
		int indexFrom = findNode(getClosestPathPoint(target.getLatitude(), target.getLongitude(), gpp), gpp);
		int indexTo = findNode(getClosestPathPoint(start.getLatitude(), start.getLongitude(), gpp), gpp);
		
		
		System.out.println("indexFrom is " + indexFrom);
		System.out.println("indexFrom is " + gpp.get(indexFrom).getName());
		System.out.println("indexTo is " + indexTo);
		System.out.println("indexTo is " + gpp.get(indexTo).getName());
				
		//In case a node is not found
		if (indexFrom == -1) {
			System.out.println("First Node not found.");

		}

		if (indexTo == -1) {
			System.out.println("Second Node not found.");
					
		}

		//Gets the best path from the source to the destination
		pathMap = djk.run(gpp.get(indexFrom), gpp.get(indexTo));
		gaia.setPathMap(pathMap);
		gaia.setPathMapList(djk.getPathMapList());
		
		return pathMap;
	}
		
	
	/**
	 * creates the route
	 * 
	 * @param gpp - node list
	 * @param start - source node
	 * @return
	 */
	public ArrayList<GaiaPathPoint> locateTarget(GaiaApp gaia, ArrayList<GaiaPathPoint> gpp, GaiaPathPoint start, Map<GaiaPathPoint, GaiaPathPoint> gmP, ArrayList<GaiaPathPoint> gpList, ArrayList<GaiaPath> gPath){
		int indexTo = findNode(getClosestPathPoint(start.getLatitude(), start.getLongitude(), gpp), gpp);
		this.gaia = gaia;

		
		DijkstraAlgorithm djk = new DijkstraAlgorithm();

		ArrayList<GaiaPathPoint> path = djk.getPath(gpp.get(indexTo), gmP, gpList);

		ArrayList<GaiaPathPoint> tempqwe = new ArrayList<GaiaPathPoint>();
		ArrayList<GaiaPath> gasd = new ArrayList<GaiaPath>();
		tempqwe.clear();
		gasd.clear();
		if (path != null && path.size() > 0) {
			
			System.out.println("Path Size is " + path.size());
			//If a path exists the steps will be printed, with their length
			System.out.println("Steps required: " + (path.size() - 1));

			int distance;
			int totalDistance = 0;

			for (int i = 0; i < path.size(); i++) {
				GaiaPathPoint n;
				GaiaPathPoint n1;

				if (i == 0) {
					n = path.get(i);

					System.out.println("#" + i + " - Go to " + n.getName());
					tempqwe.add(n);
				} else {
					n1 = path.get(i - 1);
					n = path.get(i);
					distance = djk.getDistance(n1, n, gPath);
					totalDistance += distance;
					System.out.println("#" + i + " - Go to " + n.getName() + " for "
							+ distance + "m");
					
					tempqwe.add(n);

				}

			}
			
			System.out.println("Total Distance: " + totalDistance + "m");

		}
		else
			System.out.println("\nNot Found");
		
		setRouteList(tempqwe, gasd);

		
		CustomMapView.setDrivingMode(true);
		
		return path;

	}
	
	/**
	 * 
	 * cancels routing and goes back to default view
	 */
	public void cancelRoute(){
		
		CustomMapView.setDrivingMode(false);
		System.out.println("Route canceled");
	}
	
	/**
	 * Finds a node's position in the list
	 * 
	 * @param name
	 * @param gpp
	 * @return
	 */
	private int findNode(String name, ArrayList<GaiaPathPoint> gpp) {
		int index = -1;

		for (int i = 0; i < gpp.size(); i++)
			if (gpp.get(i).getName().equals(name))
				index = i;

		return index;
	}

	
	
	/**
	 * sets the route list
	 * 
	 * @param ar
	 */
	public void setRouteList(ArrayList<GaiaPathPoint> ar, ArrayList<GaiaPath> pr){
		

		String edge;
		GaiaPathPoint n1;
		GaiaPathPoint n2;
		
		for(int i = 1; i<ar.size(); i++){
			
			n1 = ar.get(i-1);
			n2 = ar.get(i);
			edge = n1.getName()+"TO"+n2.getName();
			
			pr.add(new GaiaPath(edge, n1, n2));

		}
		gaia.setRoute(pr);
		
	}
	
	/**
	 * Returns a node that is closest to the coordinate, in case that coordinate is not a node in the map.
	 * 
	 * @param curLat - latitude
	 * @param curLon - longitude
	 * @param ogp - node list
	 * @return
	 */
	private String getClosestPathPoint(int curLat, int curLon, ArrayList<GaiaPathPoint> ogp){
		
		int index = 0;
		int closestDist;
		ArrayList<Integer> dists= new ArrayList<Integer>();
		
		int latDif;
		int lonDif;
		int tempDist;
		
		for(int i = 0; i < ogp.size(); i++){
			
			latDif = curLat - ogp.get(i).getLatitude();
			lonDif = curLon - ogp.get(i).getLongitude();
			
			tempDist = (int) Math.sqrt(Math.pow(latDif, 2) + Math.pow(lonDif, 2));
			dists.add(tempDist);
			
		}
		
		closestDist = dists.get(0);
		
		for(int i = 0; i<dists.size(); i++){
			
			if(dists.get(i) < closestDist){
				
				closestDist = dists.get(i);
				
				index = i;
			}
						
		}
		
		
		return ogp.get(index).getName();
	}

}
