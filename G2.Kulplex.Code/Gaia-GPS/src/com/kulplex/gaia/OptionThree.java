package com.kulplex.gaia;

import com.kulplex.gaia.obj.GaiaPathPoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class OptionThree extends Activity{

	EditText myEditText;
	GaiaApp gaia;
	FrameLayout background;
	String i1;
	String i2;
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opt3);
        
        gaia = ((GaiaApp)getApplicationContext());	
        background = (FrameLayout) findViewById(R.id.background);	        
        themeHandler();
        
        ImageView OTB = (ImageView) findViewById(R.id.optback3);
        OTB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	Intent intent = new Intent(OptionThree.this, PlannerActivity.class);
            	startActivity(intent);
                finish(); 
            }

        });
        
        final EditText inputlon = (EditText) findViewById(R.id.editText1);
        inputlon.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
				
        final EditText inputlat = (EditText) findViewById(R.id.editText2); 
        inputlat.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        
        
		ImageView L1 = (ImageView) findViewById(R.id.loadopt3);
        L1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(OptionThree.this, Loading.class);	                    
                
                i1 = inputlon.getText().toString();
                i2 = inputlat.getText().toString();
                
                if(!i1.equals("") && !i2.equals(""))
                {
                	try
                	{
                		float lon = Float.parseFloat(i1);
                		float lat = Float.parseFloat(i2);
                		
                		System.out.println(lon + " " + lat);
                		
                		String gppName = (int)lat +","+ (int) lon;
                		GaiaPathPoint gp = new GaiaPathPoint(gppName, gppName);
                		gaia.setPlannedPlaceLoc(gp);
                
                		startActivity(intent);
	                    finish();
                	}
                	catch(Exception e)
                	{
                		Toast.makeText(getBaseContext(), "Insert coordinates, not text.", Toast.LENGTH_SHORT).show();
                	}
	                    
                }
                else
                	Toast.makeText(getBaseContext(), "Enter info", Toast.LENGTH_SHORT).show();
            
        }
    });

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
