/**
 * PlannerActivity.java
 * 
 * This Activity displays buttons that connects to each of the three planner options.
 * 
 * @author Jim Andersson
 * 
 */


package com.kulplex.gaia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class PlannerActivity extends Activity {
    /** Called when the activity is first created. */
	
	 GaiaApp gaia;
	 FrameLayout background;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plannerlayout);

		gaia = ((GaiaApp)getApplicationContext());
		
		background = (FrameLayout) findViewById(R.id.background);
        
        themeHandler();
      //Listener for the "Go to location" option.
        ImageView O1 = (ImageView) findViewById(R.id.opt1);
        O1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(PlannerActivity.this, PlannerOptOneActivity.class);
                startActivity(intent);
                finish(); 
            }

        });
      //Listener for the "Go to favourite" option.
        ImageView O2 = (ImageView) findViewById(R.id.opt2);
        O2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(PlannerActivity.this, PlannerOptTwoActivity.class);
                startActivity(intent);
                finish(); 
            }

        });
      //Listener for the "Go to coordinates" option.
        ImageView O3 = (ImageView) findViewById(R.id.opt3);
        O3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(PlannerActivity.this, PlannerOptThreeActivity.class);
                startActivity(intent);
                finish(); 
            }

        });
      //Listener for the back button.
        ImageView B1 = (ImageView) findViewById(R.id.back1);
        B1.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {

				Intent intent = new Intent(PlannerActivity.this,
						GaiaActivity.class);
				startActivity(intent);
				
				finish(); 
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

