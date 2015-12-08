/**
 * Renderer
 * 
 * This class handles the rendering of the map
 * 
 * 
 * @author Alberto Vaccari, Hasan Yahya
 * 
 */

package com.kulplex.gaia.handler;

import java.util.ArrayList;

import com.kulplex.gaia.CustomMapView;
import com.kulplex.gaia.R;
import com.kulplex.gaia.obj.GaiaPath;
import com.kulplex.gaia.obj.GaiaPOI;
import com.kulplex.gaia.obj.GaiaStreet;
import com.kulplex.gaia.obj.GaiaPolygon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Renderer {
	
	
	public static void drawStreet(ArrayList<GaiaStreet> streets, float scale, Canvas canvas, Paint pt) {
		
		if(scale > 0.04)
			pt.setStrokeWidth(100);
		else
			pt.setStrokeWidth(0);
		
		pt.setAntiAlias(true);
		pt.setStyle(Paint.Style.STROKE);  
		pt.setStrokeJoin(Paint.Join.ROUND);  
		pt.setStrokeCap(Paint.Cap.ROUND);  
		
			for (int j = 0; j < streets.size(); j++) {
				
				for(int i = 0; i < streets.get(j).getSize() - 1; i++){
					
					if(streets.get(j).isType("primary"))
						pt.setColor(Color.YELLOW);
					if(streets.get(j).isType("secondary"))
						pt.setColor(Color.BLUE);
					else
						pt.setColor(Color.LTGRAY);
					
					
					canvas.drawLine(streets.get(j).getStreet().get(i).getLatitude(), streets.get(j).getStreet().get(i).getLongitude(),
							streets.get(j).getStreet().get(i+1).getLatitude(), streets.get(j).getStreet().get(i+1).getLongitude(), pt);
				
				}
				
			}
			
			pt.setColor(Color.RED);
				
		pt.setStrokeWidth(0);
	}
	
	public static void drawRoute(Context myContext, ArrayList<GaiaPath> gp, Canvas canvas, Paint pt){
		
		pt.setStrokeWidth(100);
		pt.setAntiAlias(true);
		pt.setStyle(Paint.Style.STROKE);  
		pt.setStrokeJoin(Paint.Join.ROUND);  
		pt.setStrokeCap(Paint.Cap.ROUND);  
		
		
			for(int i = 0; i < gp.size() - 1; i++){
				pt.setColor(Color.RED);
				
				canvas.drawLine(gp.get(i).getFirstNode().getLatitude(), gp.get(i).getFirstNode().getLongitude(),
						gp.get(i).getSecondNode().getLatitude(), gp.get(i).getSecondNode().getLongitude(), pt);
			
			}
			
			
			canvas.rotate(+CustomMapView.rotation, gp.get(gp.size() - 1).getSecondNode().getLatitude(), gp.get(gp.size() - 1).getSecondNode().getLongitude());
			canvas.rotate(+90, gp.get(gp.size() - 1).getSecondNode().getLatitude(), gp.get(gp.size() - 1).getSecondNode().getLongitude());
			
			canvas.scale(1 / (CustomMapView.scale), 1 / (CustomMapView.scale), gp.get(gp.size() - 1).getSecondNode().getLatitude(), gp.get(gp.size() - 1).getSecondNode().getLongitude());

			Bitmap marker = BitmapFactory.decodeResource(myContext.getResources(),
					R.drawable.end);

			// Rendering POI image
			canvas.drawBitmap(marker, gp.get(gp.size() - 1).getSecondNode().getLatitude(), gp.get(gp.size() - 1).getSecondNode().getLongitude(), pt);

			canvas.scale(CustomMapView.scale, CustomMapView.scale, gp.get(gp.size() - 1).getSecondNode().getLatitude(), gp.get(gp.size() - 1).getSecondNode().getLongitude());

			canvas.rotate(-90, gp.get(gp.size() - 1).getSecondNode().getLatitude(), gp.get(gp.size() - 1).getSecondNode().getLongitude());
			canvas.rotate(-CustomMapView.rotation, gp.get(gp.size() - 1).getSecondNode().getLatitude(), gp.get(gp.size() - 1).getSecondNode().getLongitude());

	}

	
	/*public static void drawPoint(ArrayList<GaiaPoint> points, Canvas canvas, Paint pt) {
		pt.setColor(Color.RED);

		
		for (int i = 0; i < points.size(); i++) {

			
				canvas.drawCircle( points.get(i).getLatitude(),points.get(i).getLongitude(), 80, pt);
				
				
		}
		
	}
	
	*/
	
	  public static void drawPOI(Context context, ArrayList<GaiaPOI> poiList, Canvas canvas, Paint pt, float scale, int rotation) {
	  
	  pt.setTextSize(15); 
	  pt.setColor(Color.BLACK);
	  
	  
	  for(int i = 0; i < poiList.size(); i++) { 
		  float _lon =	  poiList.get(i).getLongitude();
		  float _lat =	  poiList.get(i).getLatitude();
	  
		  canvas.rotate(+rotation, _lat, _lon);
		  canvas.rotate(+90, _lat, _lon); 
		  canvas.scale(1 / (scale), 1 / (scale), _lat, _lon); // Drawing POI text
		  
		  canvas.drawText(poiList.get(i).getName(), _lat, _lon, pt);
		  
		  
		  // Rendering POI image
		  
		  Bitmap icon = BitmapFactory.decodeResource(context.getResources(), poiList.get(i).getIcon());
		  canvas.drawBitmap(icon, _lat, _lon, pt);
		  
		  
		  canvas.scale(scale, scale, _lat, _lon);
		  
		  canvas.rotate(-90, _lat, _lon); 
		  canvas.rotate(-rotation, _lat, _lon);
	  }
	  
	 }
	
	  public static void drawCustomPOI(Context context, ArrayList<GaiaPOI> poiCustomList, Canvas canvas, Paint pt, float scale, int rotation) {
		  
	  pt.setTextSize(15); 
	  pt.setColor(Color.BLACK);
	  
	  
	  for(int i = 0; i < poiCustomList.size(); i++) { 
		  float _lon =	  poiCustomList.get(i).getLongitude();
		  float _lat =	  poiCustomList.get(i).getLatitude();
	  
		  canvas.rotate(+rotation, _lat, _lon);
		  canvas.rotate(+90, _lat, _lon); 
		  canvas.scale(1 / (scale), 1 / (scale), _lat, _lon); // Drawing POI text
		  
		  canvas.drawText(poiCustomList.get(i).getName(), _lat, _lon, pt);
		  
		  
		  // Rendering POI image
		  
		  Bitmap icon = BitmapFactory.decodeResource(context.getResources(), poiCustomList.get(i).getIcon());
		  canvas.drawBitmap(icon, _lat, _lon, pt);
		  
		  
		  canvas.scale(scale, scale, _lat, _lon);
		  
		  canvas.rotate(-90, _lat, _lon); 
		  canvas.rotate(-rotation, _lat, _lon);
	  }
	  
	 }
	
	
	
	
	public static void drawPolygon(ArrayList<GaiaPolygon> poly, Canvas canvas, Paint pt) {
		pt.setColor(Color.BLUE);
		pt.setStyle(Paint.Style.FILL_AND_STROKE);
		pt.setAlpha(50);
		
		Path path;
		
		for (int j = 0; j < poly.size(); j++) {
			
			path = new Path();
			path.setFillType(Path.FillType.EVEN_ODD);
			path.moveTo(poly.get(j).getPolygon().get(0).getLatitude(), poly.get(j).getPolygon().get(0).getLongitude());
		       
			for(int i = 0; i < poly.get(j).getSize() - 1; i++){
				
				/*canvas.drawLine(poly.get(j).getPolygon().get(i).getLatitude(), poly.get(j).getPolygon().get(i).getLongitude(),
						poly.get(j).getPolygon().get(i+1).getLatitude(), poly.get(j).getPolygon().get(i+1).getLongitude(), pt);
			*/
				
				
		         path.lineTo(poly.get(j).getPolygon().get(i+1).getLatitude(), poly.get(j).getPolygon().get(i+1).getLongitude());

		        
			}
			
			path.lineTo(poly.get(j).getPolygon().get(0).getLatitude(), poly.get(j).getPolygon().get(0).getLongitude());

			path.close();
	        canvas.drawPath(path, pt);
			
		}
		
		pt.setAlpha(255);
		
		
		
	}

}
