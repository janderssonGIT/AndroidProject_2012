/**
 * SocialActivity
 * 
 * This class handles the Social menu
 * 
 * 
 * @author Alberto Vaccari, Henry Dang
 * 
 */

package com.kulplex.gaia;

import com.kulplex.gaia.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SocialActivity extends Activity{

	FrameLayout background;
	GaiaApp gaia;
	
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.sociallayout);
	        
	        gaia = ((GaiaApp)getApplicationContext());
	        
	        background = (FrameLayout) findViewById(R.id.background);
	        
	        themeHandler();
	        
	        ImageView Backbtn = (ImageView)findViewById(R.id.button1);
			 
			 
			 Backbtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(SocialActivity.this,
							GaiaActivity.class);
					startActivity(intent);
					finish(); 
					
				}
			});
			 
			 ImageView weatherforecast = (ImageView) findViewById(R.id.WeatherForecast);
				   
               weatherforecast.setOnClickListener(new View.OnClickListener() {
                	
                public void onClick(View v) {
                	setContentView(R.layout.weatherforecast);

					Intent intent = new Intent(SocialActivity.this,
							WeatherActivity.class);
					startActivity(intent);
					finish();
					
				}
				}); 
	        
               
               ImageView FriendList = (ImageView) findViewById(R.id.FriendList);
				   
               FriendList.setOnClickListener(new View.OnClickListener() {
	                	
	                public void onClick(View v) {
	                	

						Intent intent = new Intent(SocialActivity.this,
								FriendListActivity.class);
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