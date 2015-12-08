/**
 * LoadingActivity
 * 
 * This class handles the loading of the route
 * 
 * 
 * @author Hasan Yahya
 * 
 */

package com.kulplex.gaia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.kulplex.gaia.handler.PathFinder;
import com.kulplex.gaia.obj.GaiaPath;
import com.kulplex.gaia.obj.GaiaPathPoint;
import com.kulplex.gaia.obj.GaiaPoint;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Loading extends Activity {
	
	GaiaApp gaia;
	RelativeLayout background;
	PathFinder pf;
	
	int i = 1;
	ArrayList<GaiaPathPoint> gpp;
	ArrayList<GaiaPath> gp;
	GaiaPathPoint startNode;
	GaiaPathPoint targetNode;
	boolean routeStatus = false;
	Map<GaiaPathPoint, GaiaPathPoint> pathMap;
	ArrayList<GaiaPathPoint> pathList;

	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.loadroutelayout);
	        
			gaia = ((GaiaApp)getApplicationContext());			
			background = (RelativeLayout) findViewById(R.id.background);		
	        themeHandler();	 
	        
	    	pf = new PathFinder();
	    	
	    	new LoadViewTask().execute();
	 }
	 
	 private class LoadViewTask extends AsyncTask<Void, Integer, Void>{

		 @Override
			protected void onPreExecute() {

				System.out.println("getting ready");
			}
		 
		@Override
		protected Void doInBackground(Void... params) {
			synchronized (this) {
				
				publishProgress(0);

				initItems();

				publishProgress(1);

				runRoute();
				
				publishProgress(2);

				runTarget(gaia.getCurPosition());

				publishProgress(3);
				
			}
			return null;
		}
		 
		@Override
		protected void onProgressUpdate(Integer... values) {

			switch (i) {
			case 0:
				System.out.println("Initializing items..." + i + "/5");
				break;
			case 1:
				System.out.println("Initializing items..." + i + "/5");
				break;
			case 2:
				System.out.println("Calculating Route..." + i + "/5");
				break;
			case 3:
				System.out.println("Finding target in the map..." + i + "/5");
				break;
			default:
				System.out.println("Starting the direction support..." + i + "/5");
				break;

			}

			i++;
		}
		 
		@Override
		protected void onPostExecute(Void result) {
			
			Intent intent = new Intent(Loading.this, GaiaActivity.class);
			startActivity(intent);
			finish();
		}
		 
	 }
	 
	 public void initItems(){
		 
		 pathMap = new HashMap<GaiaPathPoint, GaiaPathPoint>();
		 pathList = new ArrayList<GaiaPathPoint>();
		 String asdf = (int)gaia.getCurPosition().getLatitude()+","+(int)gaia.getCurPosition().getLongitude();
		 startNode = new GaiaPathPoint(asdf, asdf);
		 targetNode = gaia.getPlannedPlaceLoc();
		 
	 }
	 
	 public void runRoute(){
		 
		
		 pathMap = pf.pathFind(gaia, gaia.getPathPoints(), gaia.getPaths(), targetNode, startNode );
	 }
	 
	 public void runTarget(GaiaPoint gp){
		 
		 String curp = (int)gp.getLatitude()+","+(int)gp.getLongitude();
		 
		 GaiaPathPoint gpp = new GaiaPathPoint(curp,curp);

		 pathList = pf.locateTarget(gaia, gaia.getPathPoints(), gpp, gaia.getPathMap(), gaia.getPathMapList(), gaia.getPaths());

	 }
	 
	 	
	        
	 	public void themeHandler(){	
	
	    		if(gaia.getChosenTheme().equals("Black Theme"))
	    		{
	    			
	    			background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_black));
	    			
	    		}
	    				
	    		if(gaia.getChosenTheme().equals("Blue Theme"))
	    		{
	    		
	    			background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_blue));
	    			
	    		}
	    		else
	    			if(gaia.getChosenTheme().equals("Orange Theme"))
	    			{
	    				
	    				background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_orange));
	    			}
	    		else
	    			if(gaia.getChosenTheme().equals("Green Theme"))
	    			{
	    				
	    				background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_green));
	    			}
	    		else
	    			if(gaia.getChosenTheme().equals("Purple Theme"))
	    			{
	    				
	    				background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_purple));
	    			}
	    		
	    		
	    	} 

}
