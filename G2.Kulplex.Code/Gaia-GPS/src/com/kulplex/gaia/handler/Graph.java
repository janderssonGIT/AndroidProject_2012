package com.kulplex.gaia.handler;
import java.util.ArrayList;

import com.kulplex.gaia.obj.GaiaPathPoint;
import com.kulplex.gaia.obj.GaiaPath;

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