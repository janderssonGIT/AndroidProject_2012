/**
 * GaiaApp
 * 
 * This class handles the state of the application and its variables
 * 
 * 
 * @author Alberto Vaccari
 * 
 */package com.kulplex.gaia;

 
 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

//import org.acra.*;
//import org.acra.annotation.*;

import com.kulplex.gaia.obj.GaiaPath;
import com.kulplex.gaia.obj.GaiaPathPoint;
import com.kulplex.gaia.obj.GaiaPOI;
import com.kulplex.gaia.obj.GaiaPoint;
import com.kulplex.gaia.obj.GaiaPolygon;
import com.kulplex.gaia.obj.GaiaStreet;
import com.kulplex.gaia.obj.Graph;


@ReportsCrashes(formKey = "dDFyLUpGQ3lOLWhDa2wzcXVXWTFCLXc6MQ") 
public class GaiaApp extends Application {
	
	String version = "Pro"; //or Free
	
	String chosenTheme = "Black Theme";
	String chosenMap = "Gothenburg";
	
	int idTheme = 0;
	int idMap = 0;
	String mapFile = "osmgbdb.kpx";
	
	GaiaPoint curPos;
	
	int orientation = 0;
	float curScale = 0.8f; 
	
	
	boolean DEBUG = true;
	boolean firstRun = true;
	public Boolean[] POIstate = new Boolean[]{false, false, false, false, false, true, true, true, true};
    //Food and Drink, Entertainment, Places of Worship, Healthcare, Education, Public Transport, Shop, Administrative Buildings, Parking
	public Integer[] POIicon = new Integer[]{R.drawable.poi_food, R.drawable.poi_entertainment, R.drawable.poi_worship, R.drawable.poi_health, 
					R.drawable.poi_edu, R.drawable.poi_pubtransp, R.drawable.poi_shop, R.drawable.poi_admin, R.drawable.poi_parking};

	ArrayList<GaiaPOI> GPOIs;
	ArrayList<GaiaStreet> Gstreets;
	ArrayList<GaiaPolygon> Gpolys;
	ArrayList<GaiaPathPoint> GPpoints;
	ArrayList<GaiaPath> Gpaths;
	ArrayList<GaiaPath> Groute;
	ArrayList<String> streetNames;
	ArrayList<GaiaPOI> poiCustomList;
	ArrayList<String> stop_landmark;
	Set<String> poiCustomListSet;
	Map<GaiaPathPoint, GaiaPathPoint> pathMap;
	ArrayList<GaiaPathPoint> pathMapList;
	Graph graph;
	
	GaiaPathPoint plannedPlaceLoc;
	
	String plannedPlaceType;
	String plannedPlaceName;

	@Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        super.onCreate();
        
		
        
        GPOIs = new ArrayList<GaiaPOI>();
    	Gstreets = new ArrayList<GaiaStreet>();
        Gpolys = new ArrayList<GaiaPolygon>();
        GPpoints = new ArrayList<GaiaPathPoint>();
        Gpaths = new ArrayList<GaiaPath>();
        Groute = new ArrayList<GaiaPath>();
        streetNames = new ArrayList<String>();
        poiCustomList = new ArrayList<GaiaPOI>();
        stop_landmark = new ArrayList<String>();
        poiCustomListSet = new HashSet<String>();
        pathMap = new HashMap<GaiaPathPoint, GaiaPathPoint>();
        pathMapList = new ArrayList<GaiaPathPoint>();
        graph = new Graph(GPpoints, Gpaths);

        
        curPos = new GaiaPoint(11924848, 57709441);
    }
	
	

	public void setGraph(Graph g)
	{
		
		graph = g;
	}
	
	public Graph getGraph()
	{
		
		
		return graph;
	}
	

	public boolean isDebug(){
		return DEBUG;
	}
	
	public Boolean isFullVersion(){
		
		if(version.equals("Pro"))
			return true;
		else 
			return false;
	}
	
	public boolean isFirstRun(){
		
		return firstRun;
		
	}
	
	public void setFirstRun(Boolean b){
		
		firstRun = b;
		
	}
	
	public void chooseTheme(String theme)
	{
		
		chosenTheme = theme;

	}
	
	public String getChosenTheme()
	{
		
		return chosenTheme;
	}
	
	public int getThemeId()
	{
		
		return idTheme;
	}

	public void setIdTheme(int id)
	{
		
		idTheme = id;
	}
	
	public void chooseMap(String map)
	{
		
		chosenMap = map;

	}
	
	public String getChosenMap()
	{
		
		return chosenMap;
	}
	
	public int getMapId()
	{
		
		return idMap;
	}

	public void setIdMap(int id)
	{
		
		idMap = id;
	}

	public void setMapFile(String map)
	{
		
		mapFile = map;
	}
	
	public String getMapFile()
	{
		
		return mapFile;
	}
	
	public void setPOIstate(Boolean[] bAr)
	{
		
		POIstate = bAr;
	}
	
	public Boolean[] getPOIstate()
	{
		
		return POIstate;
	}
	
	public void setPathMap(Map<GaiaPathPoint, GaiaPathPoint> pm)
	{
		
		pathMap = pm;
		
	}
	
	public Map<GaiaPathPoint, GaiaPathPoint> getPathMap()
	{
		
		return pathMap;	
		
	}
	
	public void setPathMapList(ArrayList<GaiaPathPoint> gpp)
	{
		
		pathMapList = gpp;
		
	}
	
	public ArrayList<GaiaPathPoint> getPathMapList()
	{
		
		return pathMapList;
		
	}
	
	public void setPOIs(ArrayList<GaiaPOI> gp)
	{
		GPOIs.clear();
		for(int i = 0; i < gp.size(); i++)
		{
			GPOIs.add(gp.get(i));
		}
		
	}
	
	public ArrayList<GaiaPOI> getPOIs()
	{
		
		return GPOIs;
	}
	
	public ArrayList<GaiaStreet> getStreets()
	{
		
		return Gstreets;
	}
	
	
	public void setStreets(ArrayList<GaiaStreet> gs)
	{
		Gstreets.clear();
		for(int i = 0; i < gs.size(); i++)
		{
			Gstreets.add(gs.get(i));
		}
		
	}
	
	public ArrayList<GaiaPolygon> getPolygons()
	{
		
		return Gpolys;
	}
	
	public void setPolygons(ArrayList<GaiaPolygon> gp)
	{
		Gpolys.clear();
		for(int i = 0; i < gp.size(); i++)
		{
			Gpolys.add(gp.get(i));
		}
		
	}
	public ArrayList<GaiaPathPoint> getPathPoints()
	{
		
		return GPpoints;
	}
	
	public void setPathPoints(ArrayList<GaiaPathPoint> gp)
	{
		GPpoints.clear();
		for(int i = 0; i < gp.size(); i++)
		{
			GPpoints.add(gp.get(i));
		}
		
	}
	
	public ArrayList<GaiaPath> getPaths()
	{
		
		return Gpaths;
	}
	
	public void setPaths(ArrayList<GaiaPath> gp)
	{
		Gpaths.clear();
		for(int i = 0; i < gp.size(); i++)
		{
			Gpaths.add(gp.get(i));
		}
	}
	
	public ArrayList<String> getStreetNamesAL()
	{
		
		return streetNames;
	}
	
	public String[] getStreetNamesArray()
	{
		String[] streets = new String[streetNames.size()];
		
		for(int i = 0; i < streets.length; i++)
		{
			streets[i]	= streetNames.get(i);
		}
		
		return streets;
		
	}
	
	public void setStreetNames(ArrayList<String> sn)
	{
		streetNames.clear();
		for(int i = 0; i < sn.size(); i++)
		{
			streetNames.add(sn.get(i));
		}
		
	}
	
	public String[] getStop_LandmarkArray()
	{
		String[] stopsLand = new String[stop_landmark.size()];
		
		for(int i = 0; i < stopsLand.length; i++)
		{
			stopsLand[i]= stop_landmark.get(i);
		}
		
		return stopsLand;
		
	}
	
	public void setStop_Landmark(ArrayList<String> sl)
	{
		stop_landmark.clear();
		for(int i = 0; i < sl.size(); i++)
		{
			stop_landmark.add(sl.get(i));
		}
		
	}
	
	public float getCurScale()
	{
		return curScale;
	}
	
	
	public void setCurScale(float s)
	{
		curScale = s;
	}
	
	public GaiaPoint getCurPosition()
	{
		return curPos;
	}
	
	public void setCurPosition(float lon, float lat)
	{
		curPos = new GaiaPoint(lon, lat);
	}
	
	public void setOrientation(int o)
	{
		
		orientation = o;
	}
	
	public int getOrientation()
	{
		
		return orientation;
	}
	
	public void setRoute(ArrayList<GaiaPath> gp)
	{
		Groute.clear();

		for(int i = 0; i < gp.size(); i++)
		{
			Groute.add(gp.get(i));
		}

		
	}
	
	
	public ArrayList<GaiaPath> getRoute()
	{
		
		return Groute;
	}
	

	public void addPOI(GaiaPOI pl)
	{
		
		poiCustomList.add(pl);
		
		setCustomPOISet(listToSet(getCustomPOI()));

	}
	
	public void removePOI(GaiaPOI pl)
	{
		
		poiCustomList.remove(pl);
		
		setCustomPOISet(listToSet(getCustomPOI()));

	}
	
	public ArrayList<GaiaPOI> getCustomPOI()
	{
		
		return poiCustomList;
	}
	
	public void setCustomPOI(ArrayList<GaiaPOI> gp)
	{
		poiCustomList.clear();
		for(int i = 0; i < gp.size(); i++)
		{
			poiCustomList.add(gp.get(i));
		}
				
	}
	
	public Set<String> getCustomPOISet()
	{
		
		
		return poiCustomListSet;
	}
	
	public void setCustomPOISet(Set<String> ss)
	{
				
		poiCustomListSet = ss;
		
	}

	public void setPlannedPlaceLoc(GaiaPathPoint gp)
	{
		
		plannedPlaceLoc = gp;
		
	}
	
	public GaiaPathPoint getPlannedPlaceLoc()
	{
		
		return plannedPlaceLoc;
				
	}
	
	public void setPlannedPlaceName(String iName)
	{
		
		plannedPlaceName = iName;
	}
	
	public String getPlannedPlaceName()
	{
		
		return plannedPlaceName;
	}
	
	public void setPlannedPlaceType(String iType)
	{
		
		plannedPlaceName = iType;
	}
	
	public String getPlannedPlaceType()
	{
		
		return plannedPlaceType;
	}
	
	public String poiToStr(GaiaPOI gp)
	{
		
		String ret = gp.getName() + "," + (int)gp.getLongitude() + "," + (int)gp.getLatitude() + "," + gp.getIc();		
	
		return ret;
	}

	public GaiaPOI strToPoi(String str)
	{

		String[] arr = str.split(",");
		
		GaiaPOI gp = new GaiaPOI(arr[0], new GaiaPoint(Integer.parseInt(arr[1]), Integer.parseInt(arr[2])) , Integer.parseInt(arr[3]));

		return gp;
	}

	public Set<String> listToSet(ArrayList<GaiaPOI> gp)
	{
	
		Set<String> temp = new HashSet<String>();
	
		if(gp == null)
			return temp;
	
		for(int i = 0; i < gp.size(); i++){
		
			temp.add(poiToStr(gp.get(i)));
		}
	
		return temp;
	}

	public ArrayList<GaiaPOI> setToList(Set<String> ss)
	{

		ArrayList<GaiaPOI> temp = new ArrayList<GaiaPOI>();	

		if(ss == null)
			return temp;

		String[] ar = new String[ss.size()];

		String[] arr = ss.toArray(ar);

		for(int i = 0; i<ss.size();i++){

			temp.add(strToPoi(arr[i]));
		}

		return temp;

	}
	
}