/**
 * GaiaFeedbackActivity
 * 
 * This Activity handles feedback from the users.
 * 
 * 
 * @author Henry Dang, Alberto Vaccari
 * 
 */

package com.kulplex.gaia;

import com.kulplex.gaia.handler.GmailSender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GaiaFeedbackActivity extends Activity {

	Spinner sType;
	TextView body;
	TextView sender;
	CheckBox response;
	LinearLayout background;
	GaiaApp gaia;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);
        
        
        gaia = ((GaiaApp)getApplicationContext());
        
        background = (LinearLayout) findViewById(R.id.background);
        
        themeHandler();
        
        
        body = (TextView) findViewById(R.id.body);
        sender = (TextView) findViewById(R.id.sender);
        response = (CheckBox) findViewById(R.id.chkResponse);
        
        sType = (Spinner) findViewById(R.id.sType);
        ArrayAdapter<?> adapterTheme = ArrayAdapter.createFromResource(
                this, R.array.feedbacktypelist, android.R.layout.simple_spinner_item);
        adapterTheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sType.setAdapter(adapterTheme);
        sType.setSelection(0);
        

        final Button send = (Button) this.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

            	
            	boolean resp = response.isChecked();
            	
            	
            	String txtBody = "" + body.getText();
            	String txtSender = "" + sender.getText();
            	String txtType = "" + sType.getSelectedItem().toString();
            	
        	if(!txtBody.equals("") && !txtSender.equals(""))
        	{
                try {   
                    GmailSender sender = new GmailSender("kulplex@gmail.com", "gaia2012");
                    sender.sendMail("Feedback report: " + txtType,   
                            txtBody + "\nNeed Response? " + resp,   
                            txtSender,   
                            "kulplex@gmail.com");   
                } catch (Exception e) {   
                    Log.e("SendMail", e.getMessage(), e); 
                }
                
                Intent intent = new Intent(GaiaFeedbackActivity.this,
						SettingsActivity.class);
				startActivity(intent);
				finish(); 
        	}
        	else
        		Toast.makeText(getBaseContext(), "Fill all the required fields", Toast.LENGTH_SHORT).show();

            }
        });
        
        Button cancel = (Button)findViewById(R.id.feedbackCancel);
		 
		 
    			 cancel.setOnClickListener(new View.OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {

    					Intent intent = new Intent(GaiaFeedbackActivity.this,
    							GaiaActivity.class);
    					startActivity(intent);
    					finish(); 
    					
    				}
    			});

    }
    
/**
 * This method handles the theme
 */
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
