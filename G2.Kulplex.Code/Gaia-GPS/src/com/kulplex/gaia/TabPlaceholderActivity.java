package com.kulplex.gaia;

import com.kulplex.gaia.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
 
public class TabPlaceholderActivity extends Activity {
	
	GaiaApp gaia;
	LinearLayout background;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Keeps screen alive
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.placeholder_layout);
        
		gaia = ((GaiaApp)getApplicationContext());
		
		background = (LinearLayout) findViewById(R.id.background);
        
        themeHandler();
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