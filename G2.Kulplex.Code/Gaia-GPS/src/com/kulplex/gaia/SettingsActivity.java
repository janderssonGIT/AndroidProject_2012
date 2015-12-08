/**
 * SettingsActivity
 * 
 * This class handles the settings
 * 
 * 
 * @author Alberto Vaccari
 * 
 */


package com.kulplex.gaia;

import com.kulplex.gaia.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class SettingsActivity extends Activity{
	
	public static final String prefsFile = "Prefs";
	Spinner sTheme;
	Spinner sMap;
	GaiaApp gaia;
	LinearLayout background;
	 
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        SharedPreferences sPrefs = getSharedPreferences(prefsFile, MODE_PRIVATE);
	        final SharedPreferences.Editor editor = sPrefs.edit();

	        setContentView(R.layout.settingslayout);
	        
	        gaia = ((GaiaApp)getApplicationContext());
	        
	        background = (LinearLayout) findViewById(R.id.background);
	        
	        themeHandler();
	        
	        sTheme = (Spinner) findViewById(R.id.themeChooser);
	        ArrayAdapter<?> adapterTheme = ArrayAdapter.createFromResource(
	                this, R.array.themes, android.R.layout.simple_spinner_item);
	        adapterTheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        sTheme.setAdapter(adapterTheme);
	        sTheme.setSelection(gaia.getThemeId());
	        
	        
	        sMap = (Spinner) findViewById(R.id.mapChooser);
	        ArrayAdapter<?> adapterMap = ArrayAdapter.createFromResource(
	                this, R.array.maps, android.R.layout.simple_spinner_item);
	        adapterMap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        sMap.setAdapter(adapterMap);
	        sMap.setSelection(gaia.getMapId());
	        
	        
	        
	        ImageView Cancelbtn = (ImageView)findViewById(R.id.settingsCancel);
			 
			 
			 Cancelbtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(SettingsActivity.this,
							GaiaActivity.class);
					startActivity(intent);
					finish(); 
					
				}
			});
			 
			 ImageView Confirmbtn = (ImageView)findViewById(R.id.settingsConfirm);
				 
				 
				 Confirmbtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {

						v.setSelected(true);					
						//Theme
						gaia.chooseTheme(sTheme.getSelectedItem().toString());
						gaia.setIdTheme((int)sTheme.getSelectedItemId());
						
						editor.putInt("selectedThemeId", (int)sTheme.getSelectedItemId());
						editor.putString("selectedThemeName", sTheme.getSelectedItem().toString());
						
						
						//Map
						gaia.chooseMap(sMap.getSelectedItem().toString());
						gaia.setIdMap((int)sMap.getSelectedItemId());
						
						editor.putInt("selectedMapId", (int)sMap.getSelectedItemId());
						editor.putString("selectedMapName", sMap.getSelectedItem().toString());
						
						editor.commit();
						
						System.out.println(gaia.getChosenTheme());


						Intent intent = new Intent(SettingsActivity.this,
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

