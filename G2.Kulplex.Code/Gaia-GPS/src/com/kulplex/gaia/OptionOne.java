package com.kulplex.gaia;


import com.kulplex.gaia.handler.SQLiteAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

public class OptionOne extends Activity {
	
	AutoCompleteTextView myAutoCompleteTextView;
	 GaiaApp gaia;
	 SQLiteAdapter dbAd;
	 FrameLayout background;
	 String type = "streets";
	 String tvt;
	 
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.opt1);
	        
			gaia = ((GaiaApp)getApplicationContext());		
			dbAd = new SQLiteAdapter(getApplicationContext(), gaia.getMapFile());
			
			try{
				dbAd.createDataBase();
				dbAd.openDataBase();
			}catch(Exception e){
				System.out.println("DB problem in option one");
			}
			background = (FrameLayout) findViewById(R.id.background);	        
	        themeHandler();
	        
	        ImageView OOB = (ImageView) findViewById(R.id.optback1);
            OOB.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                                   
                	Intent intent = new Intent(OptionOne.this, PlannerActivity.class);
                	startActivity(intent);
                    finish(); 
                }

            });
                                   
	      myAutoCompleteTextView 
	         = (AutoCompleteTextView)findViewById(
	           R.id.autocompletetextview);
	        
	        myAutoCompleteTextView.setAdapter(
	        new ArrayAdapter<String>(this, 
	        android.R.layout.simple_dropdown_item_1line, gaia.getStreetNamesArray()));
	        
	        ImageView L1 = (ImageView) findViewById(R.id.loadopt1);
            L1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    
                	System.out.println(tvt);
                  	 
                    	                    
                    if (tvt.equals("")) {
                    	
                    Toast.makeText(OptionOne.this, "Please select a desired destination ", 
	    					Toast.LENGTH_SHORT).show();                                        
                    
                  } else
                  {
                    	
                    Toast.makeText(OptionOne.this, tvt + " was selected.",  
     	    				Toast.LENGTH_SHORT).show();	
                    Intent intent = new Intent(OptionOne.this, Loading.class);
                    
                    gaia.setPlannedPlaceLoc(dbAd.getPosition(getApplicationContext(), type, tvt));
                                       
                    startActivity(intent);
                    finish(); 
                }
                	
                	
                	
               }
            });         
	    }
	        		        
	        public void onRadioButtonClick(View v) { 
	        	
	        	
	            RadioGroup group1 = (RadioGroup) findViewById(R.id.radio_group1);	            
	            int id = group1.getCheckedRadioButtonId();	   	            
	            
	            if (id == R.id.radioButton1)  {
	            	
	            	if(type.equals("point"))
	            	{
            			myAutoCompleteTextView.clearListSelection();
            			myAutoCompleteTextView.setText("");
            		}
	            	
						myAutoCompleteTextView.setAdapter(
						new ArrayAdapter<String>(this, 
						android.R.layout.simple_dropdown_item_1line, gaia.getStreetNamesArray()));						
						type = "streets";
						tvt = myAutoCompleteTextView.getEditableText().toString();
						
						
						
	            } else if (id == R.id.radioButton2) {
	            	
	            	if(type.equals("streets"))
	            		{
	            			myAutoCompleteTextView.clearListSelection();
	            			myAutoCompleteTextView.setText("");
	            		}
	            	
	            	myAutoCompleteTextView.setAdapter(
						new ArrayAdapter<String>(this, 
						android.R.layout.simple_dropdown_item_1line, gaia.getStop_LandmarkArray()));						
						type = "point";
						tvt = myAutoCompleteTextView.getEditableText().toString();				
										
	  }         	                               	             	    	                    	        	        	       	        	       
	       	   	 	           	           	    	    
	          
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