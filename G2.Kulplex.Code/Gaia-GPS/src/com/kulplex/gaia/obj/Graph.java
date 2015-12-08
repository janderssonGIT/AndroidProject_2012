/**
 * Graph
 * 
 * This object handles the routing graph
 * 
 * 
 * @author Hasan Yahya
 * 
 */

package com.kulplex.gaia.obj;
import java.util.ArrayList;


public class Graph {
	private final ArrayList<GaiaPathPoint> nodes;
	private final ArrayList<GaiaPath> paths;

	public Graph(ArrayList<GaiaPathPoint> nodes, ArrayList<GaiaPath> paths) {
		this.nodes = nodes;
		this.paths = paths;
	}

	public ArrayList<GaiaPathPoint> getNodes() {
		return nodes;
	}

	public ArrayList<GaiaPath> getPaths() {
		return paths;
	}
	
	
	
}