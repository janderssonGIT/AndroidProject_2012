/**
 * DijkstraAlgorithm
 * 
 * This class handles the creation of the fastest route between two points
 * 
 * 
 * @author Hasan Yahya, Alberto Vaccari
 * 
 */

package com.kulplex.gaia.handler;



import android.util.Log;

import com.kulplex.gaia.GaiaApp;
import com.kulplex.gaia.obj.GaiaPathPoint;
import com.kulplex.gaia.obj.GaiaPath;
import com.kulplex.gaia.obj.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Dijkstra's Algorithm for path finding
 * 
 * 
 * @author Hasan Yahya
 *
 */
public class DijkstraAlgorithm {

	private ArrayList<GaiaPath> paths;
	private Set<GaiaPathPoint> checkedNodes;
	private Set<GaiaPathPoint> unCheckedNodes;
	private Map<GaiaPathPoint, GaiaPathPoint> pathMap;
	private ArrayList<GaiaPathPoint> pathMapList;
	private Map<GaiaPathPoint, Integer> dist;
	private GaiaPathPoint targ;

	public DijkstraAlgorithm()
	{
		
	}
	
	public DijkstraAlgorithm(Graph g)
	{
		this.paths = g.getPaths();
		
	}
	
/**
 * 
 * 
 * @param source - starting point for the graph
 * @param targ - finak point for the graph
 * @return the graph (nodes, edges)
 */
	public Map<GaiaPathPoint, GaiaPathPoint> run(GaiaPathPoint source, GaiaPathPoint targ)
	{
		
		this.targ = targ;

		checkedNodes = new HashSet<GaiaPathPoint>();

		unCheckedNodes = new HashSet<GaiaPathPoint>();

		dist = new HashMap<GaiaPathPoint, Integer>();

		pathMap = new HashMap<GaiaPathPoint, GaiaPathPoint>();
		
		pathMapList = new ArrayList<GaiaPathPoint>();

		dist.put(source, 0);

		unCheckedNodes.add(source);
		
		int i = 0;
		
		while(unCheckedNodes.size() > 0)
		{
 
			System.out.println(i);
			i++;
			
			GaiaPathPoint n = getMinDistance(unCheckedNodes);

			checkedNodes.add(n);

			unCheckedNodes.remove(n);

			findMinDistance(n);
			
		}
		System.out.println("checked nodes size is "+checkedNodes.size());
		System.out.println("unchecked nodes size is "+unCheckedNodes.size());

		return pathMap;
	}
	
	/**
	 * Finds the minimum distance from the node to its neighbours
	 * @param n
	 */
	private void findMinDistance(GaiaPathPoint n)
	{
		ArrayList<GaiaPathPoint> neighbourNodes = getNeighbours(n);
		
		//System.out.println(neighbourNodes.size());
		GaiaPathPoint n1;
		
		for(int i = 0; i < neighbourNodes.size(); i++)
		{
			n1 = neighbourNodes.get(i);
			if(getShortestDist(n1) > (getShortestDist(n) + getDistance(n, n1, paths)))
			{
				
				dist.put(n1, getShortestDist(n) + getDistance(n, n1, paths));
				pathMap.put(n1, n);
				pathMapList.add(n1);
				unCheckedNodes.add(n1);
			}
		}
		
	}

	/**
	 * Finds neighbours nodes given a specific node
	 * @param n
	 * @return
	 */
	public ArrayList<GaiaPathPoint> getNeighbours(GaiaPathPoint n)
	{
		ArrayList<GaiaPathPoint> neigh = new ArrayList<GaiaPathPoint>();
	
		for(GaiaPath p : paths)
		{

			
			if(p.getFirstNode().getName().equals(n.getName()) && !isChecked(p.getSecondNode())){
				
				neigh.add(p.getSecondNode());
			}
			
			if(	p.getSecondNode().getName().equals(n.getName()) && !isChecked(p.getFirstNode())){
			
				neigh.add(p.getFirstNode());
			}
		}


		return neigh;
	}
	
	/**
	 * Checks whether a node has been checked already or not
	 * @param n
	 * @return
	 */
	private boolean isChecked(GaiaPathPoint n)
	{
		return checkedNodes.contains(n);
	}
	
	/**
	 * Gets the minimum distance given a set of GaiaPathPoints
	 * @param ns
	 * @return
	 */
	private GaiaPathPoint getMinDistance(Set<GaiaPathPoint> ns)
	{
		GaiaPathPoint min = null;
		
		for(GaiaPathPoint n : ns)
		{
			if(min == null)
				min = n;
			else
				if(getShortestDist(n) < getShortestDist(min))
					min = n;
		}
		
		return min;
	}
	
	/**
	 * Gets the distance of an edge
	 * @param n
	 * @param n1
	 * @return
	 */
	public int getDistance(GaiaPathPoint n, GaiaPathPoint n1, ArrayList<GaiaPath> gpL)
	{
		for(GaiaPath p : gpL)
		{
			if((p.getFirstNode().getName().equals(n.getName()) && p.getSecondNode().getName().equals(n1.getName()))
					||
					(p.getSecondNode().getName().equals(n.getName()) && p.getFirstNode().getName().equals(n1.getName())))
				return p.getWeight();
				
		}
		
		throw new RuntimeException("Incorrect Node entered.");
	}
	
	/**
	 * gets the shortest distance from the source to the target
	 * @param n
	 * @return
	 */
	private int getShortestDist(GaiaPathPoint n)
	{
		
		Integer d = dist.get(n);
		
		if(d == null)
			return Integer.MAX_VALUE;
		else
			return d;
		
	}
	
	/**
	 * Applies the fact that if two nodes have the same coordinate, they should be the same nodes
	 * @param gpp
	 */
	public GaiaPathPoint validate(GaiaPathPoint gpp, ArrayList<GaiaPathPoint> gpList){
		
		GaiaPathPoint gn = null;
		
		for(int i = 0; i < gpList.size(); i++){
			
			if(gpp.getName().equals(gpList.get(i).getName())){
				
				gn = gpList.get(i);
				
			}
			
			
		}
		return gn;
		
	}
	
	public ArrayList<GaiaPathPoint> getPathMapList(){
		
		return pathMapList;
		
	}
	
	/**
	 * Returns a list of Nodes from the source and the destination
	 * @param destination
	 * @return
	 */
	public ArrayList<GaiaPathPoint> getPath(GaiaPathPoint destination, Map<GaiaPathPoint, GaiaPathPoint> gpM, ArrayList<GaiaPathPoint> gpList) {
		ArrayList<GaiaPathPoint> path = new ArrayList<GaiaPathPoint>();		
		Map<GaiaPathPoint, GaiaPathPoint> pMap = gpM;
		
		GaiaPathPoint step = validate(destination, gpList);
						if(step == null)
							System.out.println("target is null");
		// Check if a path exists
		if (pMap.get(step) == null) {

			return null; 
		}
		path.add(step);
		while (pMap.get(step) != null) {

			step = pMap.get(step);
			path.add(step);
		}
		
		return path;
	}
}
