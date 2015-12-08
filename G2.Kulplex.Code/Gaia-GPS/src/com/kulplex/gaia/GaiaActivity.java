/**
 * GaiaActivity
 * 
 * This class handles the main menu of the application, as well as the compass rotation.
 * 
 * 
 * @author Alberto Vaccari, Petra Bezci, Jiayu Hu
 * 
 */

package com.kulplex.gaia;

import java.util.List;
import com.kulplex.gaia.handler.PathFinder;
import com.kulplex.gaia.handler.mySQLAdapter;
import com.kulplex.gaia.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SlidingDrawer.OnDrawerOpenListener;


public class GaiaActivity extends Activity implements OnDrawerOpenListener, OnDrawerCloseListener, SensorEventListener {
	/** Called when the activity is first created. */

	
	public static GaiaApp gaia;
	
	SlidingDrawer sd;
	SlidingDrawer mainDrawer;
	SlidingDrawer sideDrawer;
	PathFinder pf;
	
	static CustomMapView cmv;

	//Dashboard
	ImageView exitbtn;
	ImageView feedbackbtn;
	ImageView aboutbtn;
	ImageView infobtn;
	ImageView tutorialbtn;
	
	//Side Drawer
	ImageView zoomInbtn;
	ImageView zoomOutbtn;
	ImageView Rotationbtn;
	ImageView addPOIbtn;
	
	public static ImageView arrow;
	
	//Main Menu
	ImageView plannerBtn;
	ImageView favBtn;
	ImageView socialBtn;
	ImageView settingsBtn;
	ImageView accountBtn;
	
	AbsoluteLayout backgroundSide;
	FrameLayout backgroundMain;

	static TextView tvStreetName;
	static Handler mHandler;
	
	public static Context myContext;
	
	public static String map;
	private boolean mRegisteredSensor;
	// set SensorManager
	private SensorManager mSensorManager;
	
		
		@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gaia = ((GaiaApp)getApplicationContext());
		
		pf = new PathFinder();

		mRegisteredSensor = false;
		// get SensorManager instance
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		myContext = this;
		
		mHandler = new Handler();
		
		map = gaia.getChosenMap();
		
		// Keeps screen alive
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		
		setContentView(R.layout.mainlayout);
		initComponents();
		themeHandler();	
		

	}
		
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mySQLAdapter.logout();
	}

	private void initComponents()
	{
		cmv = (CustomMapView) findViewById(R.id.map);

		plannerBtn = (ImageView) findViewById(R.id.planner);
		favBtn = (ImageView) findViewById(R.id.favourites);
		socialBtn = (ImageView) findViewById(R.id.social);
		settingsBtn = (ImageView) findViewById(R.id.settings);
		backgroundSide = (AbsoluteLayout) findViewById(R.id.content);
		backgroundMain = (FrameLayout) findViewById(R.id.content2);
		accountBtn = (ImageView) findViewById(R.id.account);

		arrow = (ImageView) findViewById(R.id.arrow);
		
		if(!gaia.isFullVersion())
			accountBtn.setVisibility(View.INVISIBLE);
	
	
		
		tvStreetName = (TextView) findViewById(R.id.streetname);
		
		
		exitbtn = (ImageView) findViewById(R.id.exit);

		exitbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				finish(); 
			}
		});

		feedbackbtn = (ImageView) findViewById(R.id.feedback);

		feedbackbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(GaiaActivity.this,
						GaiaFeedbackActivity.class);
				startActivity(intent);
				finish();
				
                }});
   

		aboutbtn = (ImageView) findViewById(R.id.about);

		aboutbtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
					final Dialog dialog = new Dialog(GaiaActivity.this);
	                dialog.setContentView(R.layout.aboutdialoglayout);
	                String appName = getResources().getString(R.string.app_name);
	                dialog.setTitle("About \"" + appName + "\"");
	                dialog.setCancelable(true);
	                
	 
	                //set up button
	                Button buttonAbout = (Button) dialog.findViewById(R.id.ButtonAbout);
	                buttonAbout.setOnClickListener(new View.OnClickListener() {
	                @Override
	                    public void onClick(View v) {
	                        dialog.dismiss();
	                    }
	                });
	                
	                ImageButton buttonFB = (ImageButton) dialog.findViewById(R.id.ButtonFB);
	                buttonFB.setOnClickListener(new View.OnClickListener() {

	        			@Override
	        			public void onClick(View v) {
	        				
	        				Intent postOnFacebookWallIntent = new Intent(getBaseContext(), ShareOnFacebook.class);
	        				postOnFacebookWallIntent.putExtra("facebookMessage", "is working on the awesome GaiaGPS.");
	        				startActivity(postOnFacebookWallIntent);
	        				
	        			}
	        		});
	     
	                //now that the dialog is set up, it's time to show it    
	                dialog.show();
	            }
		});

		infobtn = (ImageView) findViewById(R.id.info);

		infobtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				finish(); 
				
				System.out.println("Finished MainActivity");
				Intent intent = new Intent(GaiaActivity.this,
						AndroidTwitterActivity.class);
				startActivity(intent);
			}
		});


		mainDrawer = (SlidingDrawer) findViewById(R.id.mainDrawer);
		mainDrawer.setOnDrawerOpenListener(this);
		mainDrawer.setOnDrawerCloseListener(this);
		
		if(!CustomMapView.drivingMode)
			mainDrawer.open();
		else
			{			
				exitbtn.setVisibility(View.GONE);
				feedbackbtn.setVisibility(View.GONE);
				aboutbtn.setVisibility(View.GONE);
				infobtn.setVisibility(View.GONE);
				
				arrow.setVisibility(View.VISIBLE);
				tvStreetName.setVisibility(View.VISIBLE);
			}
		sideDrawer = (SlidingDrawer) findViewById(R.id.sideDrawer);
		

		plannerBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				finish(); 
				
				System.out.println("Finished MainActivity");
				Intent intent = new Intent(GaiaActivity.this,
						PlannerActivity.class);
				startActivity(intent);
				

			}
		});
		
		favBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				finish(); 
				
				System.out.println("Finished MainActivity");
				
				Intent intent = new Intent(GaiaActivity.this,
						MainFavouritesActivity.class);
				startActivity(intent);
				
			}
		});
		socialBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
	
				
				if(gaia.isFullVersion())
				{
					if(!mySQLAdapter.isConnected)
					{
						final Dialog dialog = new Dialog(GaiaActivity.this);
		                dialog.setContentView(R.layout.loginlayout);
		                dialog.setTitle("Login");
		                dialog.setCancelable(true);
		                
		                //set up button
		                Button cancelbtn = (Button) dialog.findViewById(R.id.cancelbtn);
		                cancelbtn.setOnClickListener(new View.OnClickListener() {
		                @Override
		                    public void onClick(View v) {
		                        dialog.dismiss();
		                    }
		                });
		                
		                Button register = (Button) dialog.findViewById(R.id.btnLinkToRegisterScreen);
		                register.setOnClickListener(new View.OnClickListener() {
		                @Override
		                    public void onClick(View v) {
		                	dialog.dismiss();
		                	final Dialog dialogR = new Dialog(GaiaActivity.this);
			                dialogR.setContentView(R.layout.registerlayout);
		                	dialogR.setTitle("Register");
		                	dialogR.setCancelable(true);
			                
		                	Button cancelbtnR = (Button) dialogR.findViewById(R.id.cancelRbtn);
		            	    cancelbtnR.setOnClickListener(new View.OnClickListener() {
		            	        @Override
		            		public void onClick(View v) {
	
		            		    dialogR.dismiss();
		            		    
		            		}
		            	    });
		            	    
		            	    final TextView userR = (TextView) dialogR.findViewById(R.id.RegisterUser);
		                	final TextView pwdR = (TextView) dialogR.findViewById(R.id.RegisterPassword);            	   
		                	
		                	Button registerBtn = (Button) dialogR.findViewById(R.id.btnRegister);
		                	registerBtn.setOnClickListener(new View.OnClickListener() {
		            	        @Override
		            		public void onClick(View v) {
	
		            	        	if(!userR.getText().toString().equals("") && !pwdR.getText().toString().equals(""))
		                			{
		                				if(mySQLAdapter.register(getBaseContext(),userR.getText().toString(), pwdR.getText().toString()))
		                					{
		                						Toast.makeText(GaiaActivity.this, "Account created. Please login.",
		                							Toast.LENGTH_LONG).show();
		                						 dialogR.dismiss();
		                					}
		                			}
		            	        	else
		            	        	{
		            	    			Toast.makeText(GaiaActivity.this, "Username is already taken.",
		            	    					Toast.LENGTH_SHORT).show();
		            	        	}
	
		            		}
		            	    });
		                	
		                	    dialogR.show();
		                					
		                    }
		                });
		                
		                final TextView user = (TextView) dialog.findViewById(R.id.loginUser);
		                final TextView pwd = (TextView) dialog.findViewById(R.id.loginPassword);
		                
		                
		                Button btnLogin = (Button) dialog.findViewById(R.id.btnLogin);
		                btnLogin.setOnClickListener(new View.OnClickListener() {
		                @Override
		                    public void onClick(View v) {
		                        
		                			if(!user.getText().toString().equals("") && !pwd.getText().toString().equals(""))
		                			{
		                				if(mySQLAdapter.login(getBaseContext(),user.getText().toString(), pwd.getText().toString(), true))
		                					{
		                						
		                						dialog.dismiss();
		                						 
		                						 
		                						 	finish();
		                							
		                							System.out.println("Finished MainActivity");
		                							
		                							Intent intent = new Intent(GaiaActivity.this,
		                									SocialActivity.class);
		                							startActivity(intent);
		                					}
		                				else
		                					Toast.makeText(getBaseContext(), "Incorrect Username/Password", Toast.LENGTH_SHORT).show();
		                				
		                			}
		                    }
		                });
		                //now that the dialog is set up, it's time to show it    
		                dialog.show();
					}
					else
					{
						
						finish();
						
						System.out.println("Finished MainActivity");
						
						Intent intent = new Intent(GaiaActivity.this,
								SocialActivity.class);
						startActivity(intent);
					}
					
				
				
				}
				else
					Toast.makeText(getBaseContext(),"Social not available in Free Version", Toast.LENGTH_SHORT).show();
				
				
				 
			}
		});
		settingsBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				finish();
				
				System.out.println("Finished MainActivity");
				
				Intent intent = new Intent(GaiaActivity.this,
						SettingsActivity.class);
				startActivity(intent);

				
			}
		});
		
		
		accountBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
		
				if(!mySQLAdapter.isConnected)
				{
					final Dialog dialog = new Dialog(GaiaActivity.this);
	                dialog.setContentView(R.layout.loginlayout);
	                dialog.setTitle("Login");
	                dialog.setCancelable(true);
	                
	                //set up button
	                Button cancelbtn = (Button) dialog.findViewById(R.id.cancelbtn);
	                cancelbtn.setOnClickListener(new View.OnClickListener() {
	                @Override
	                    public void onClick(View v) {
	                        dialog.dismiss();
	                    }
	                });
	                
	                Button register = (Button) dialog.findViewById(R.id.btnLinkToRegisterScreen);
	                register.setOnClickListener(new View.OnClickListener() {
	                @Override
	                    public void onClick(View v) {
	                	dialog.dismiss();
	                	final Dialog dialogR = new Dialog(GaiaActivity.this);
		                dialogR.setContentView(R.layout.registerlayout);
	                	dialogR.setTitle("Register");
	                	dialogR.setCancelable(true);
		                
	                	Button cancelbtnR = (Button) dialogR.findViewById(R.id.cancelRbtn);
	            	    cancelbtnR.setOnClickListener(new View.OnClickListener() {
	            	        @Override
	            		public void onClick(View v) {

	            		    dialogR.dismiss();

	            		}
	            	    });
	            	    
	            	    final TextView userR = (TextView) dialogR.findViewById(R.id.RegisterUser);
	                	final TextView pwdR = (TextView) dialogR.findViewById(R.id.RegisterPassword);            	   
	                	
	                	Button registerBtn = (Button) dialogR.findViewById(R.id.btnRegister);
	                	registerBtn.setOnClickListener(new View.OnClickListener() {
	            	        @Override
	            		public void onClick(View v) {

	            	        	if(!userR.getText().toString().equals("") && !pwdR.getText().toString().equals(""))
	                			{
	                				if(mySQLAdapter.register(getBaseContext(),userR.getText().toString(), pwdR.getText().toString()))
	                					{
	                						Toast.makeText(GaiaActivity.this, "Account created. Please login.",
	                							Toast.LENGTH_LONG).show();
	                						 dialogR.dismiss();
	                					}
	                			}
	            	        	else
	            	        	{
	            	    			Toast.makeText(GaiaActivity.this, "Username is already taken.",
	            	    					Toast.LENGTH_SHORT).show();
	            	        	}

	            		}
	            	    });
	                	
	                	    dialogR.show();
	                					
	                    }
	                });
	                
	                final TextView user = (TextView) dialog.findViewById(R.id.loginUser);
	                final TextView pwd = (TextView) dialog.findViewById(R.id.loginPassword);
	                
	                
	                Button btnLogin = (Button) dialog.findViewById(R.id.btnLogin);
	                btnLogin.setOnClickListener(new View.OnClickListener() {
	                @Override
	                    public void onClick(View v) {
	                        
	                			if(!user.getText().toString().equals("") && !pwd.getText().toString().equals(""))
	                			{
	                				if(mySQLAdapter.login(getBaseContext(),user.getText().toString(), pwd.getText().toString(), true))
	                					{
	                						 Toast.makeText(getBaseContext(), "Logged in", Toast.LENGTH_SHORT).show();
	                						 
	                							accountBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.logout));
	                							dialog.dismiss();
	                						 
	                					}
	                				else
	                					Toast.makeText(getBaseContext(), "Incorrect Username/Password", Toast.LENGTH_SHORT).show();
	                				
	                			}
	                    }
	                });
	                //now that the dialog is set up, it's time to show it    
	                dialog.show();
				}
				else
				{
				final Dialog dialog = new Dialog(GaiaActivity.this);
		        dialog.setContentView(R.layout.logoutlayout);
		        dialog.setTitle("Logout");
		        dialog.setCancelable(true);
		        
		        //Connected as username
		        TextView txt = (TextView) dialog.findViewById(R.id.connect);
		        txt.setText(txt.getText() + mySQLAdapter.name);
		        
		        //set up button
		        Button cancelbtn = (Button) dialog.findViewById(R.id.cancelbtn);
		        cancelbtn.setOnClickListener(new View.OnClickListener() {
		        @Override
		            public void onClick(View v) {
		                dialog.dismiss();
		            }
		        });
		        
		        
		        Button btnLogout = (Button) dialog.findViewById(R.id.logoutbtn);
		        btnLogout.setOnClickListener(new View.OnClickListener() {
		        @Override
		            public void onClick(View v) {
		                
		        		mySQLAdapter.logout();
		        		Toast.makeText(getBaseContext(), "Logged out", Toast.LENGTH_SHORT).show();

		        		accountBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.login));
		        		dialog.dismiss();
		        		
		            }
		        });
		        //now that the dialog is set up, it's time to show it    
		        dialog.show();
				}
	    

			}
			
		});
		
		
		zoomInbtn = (ImageView) findViewById(R.id.zoom_in);
		zoomOutbtn = (ImageView) findViewById(R.id.zoom_out);
		Rotationbtn = (ImageView) findViewById(R.id.actual_location);
		
		
		zoomInbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			
				cmv.setZoom(1);
	
			}
		});
		
		zoomOutbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				cmv.setZoom(-1);

			}
		});
		
		Rotationbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rotation_arrow_blue));
		Rotationbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
			cmv.followRotation(!cmv.followRotation);
			
			
			if(cmv.followRotation)
				Rotationbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rotation_arrow_orange));
			else
				Rotationbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rotation_arrow_blue));
			
			
			}
		});
		
		
tutorialbtn = (ImageView) findViewById(R.id.tutorial);
		
		tutorialbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				showTutorial();
			}
		});
	
		
		
		addPOIbtn = (ImageView) findViewById(R.id.addPOI);
		addPOIbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				CustomMapView.toggleAddPOIMode();
				
				
			
			}
		});
		
	}
	

	public void onDrawerOpened() {
		
		exitbtn.setVisibility(View.VISIBLE);
		feedbackbtn.setVisibility(View.VISIBLE);
		aboutbtn.setVisibility(View.VISIBLE);
		infobtn.setVisibility(View.VISIBLE);
		
		arrow.setVisibility(View.GONE);
		tvStreetName.setVisibility(View.GONE);


}

public void onDrawerClosed() {

	if (!mainDrawer.isOpened()) {

		exitbtn.setVisibility(View.GONE);
		feedbackbtn.setVisibility(View.GONE);
		aboutbtn.setVisibility(View.GONE);
		infobtn.setVisibility(View.GONE);
		
		if(CustomMapView.drivingMode)
			arrow.setVisibility(View.VISIBLE);
		
		tvStreetName.setVisibility(View.VISIBLE);
	}

}


	
	
	public void themeHandler(){
		
		
		if(gaia.getChosenTheme().equals("Black Theme"))
		{
			plannerBtn.setImageDrawable(getResources().getDrawable(R.drawable.black_travelp_b_150));
			favBtn.setImageDrawable(getResources().getDrawable(R.drawable.black_favorites_b_150));
			socialBtn.setImageDrawable(getResources().getDrawable(R.drawable.black_social_b_150));
			settingsBtn.setImageDrawable(getResources().getDrawable(R.drawable.black_settings_b_150));
			backgroundSide.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_black));
			backgroundMain.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_black));
			
			
		}
				
		if(gaia.getChosenTheme().equals("Blue Theme"))
		{
			plannerBtn.setImageDrawable(getResources().getDrawable(R.drawable.blue_travelp_b_150));
			favBtn.setImageDrawable(getResources().getDrawable(R.drawable.blue_favorites_b_150));
			socialBtn.setImageDrawable(getResources().getDrawable(R.drawable.blue_social_b_150));
			settingsBtn.setImageDrawable(getResources().getDrawable(R.drawable.blue_settings_b_150));
			backgroundSide.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_blue));
			backgroundMain.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_blue));
			
		}
		else
			if(gaia.getChosenTheme().equals("Orange Theme"))
			{
				plannerBtn.setImageDrawable(getResources().getDrawable(R.drawable.orange_travelp_b_150));
				favBtn.setImageDrawable(getResources().getDrawable(R.drawable.orange_favorites_b_150));
				socialBtn.setImageDrawable(getResources().getDrawable(R.drawable.orange_social_b_150));
				settingsBtn.setImageDrawable(getResources().getDrawable(R.drawable.orange_settings_b_150));
				backgroundSide.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_orange));
				backgroundMain.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_orange));
			}
		else
			if(gaia.getChosenTheme().equals("Green Theme"))
			{
				plannerBtn.setImageDrawable(getResources().getDrawable(R.drawable.green_travelp_b_150));
				favBtn.setImageDrawable(getResources().getDrawable(R.drawable.green_favorites_b_150));
				socialBtn.setImageDrawable(getResources().getDrawable(R.drawable.green_social_b_150));
				settingsBtn.setImageDrawable(getResources().getDrawable(R.drawable.green_settings_b_150));
				backgroundSide.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_green));
				backgroundMain.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_green));
			}
		else
			if(gaia.getChosenTheme().equals("Purple Theme"))
			{
				plannerBtn.setImageDrawable(getResources().getDrawable(R.drawable.purple_travelp_b_150));
				favBtn.setImageDrawable(getResources().getDrawable(R.drawable.purple_favorites_b_150));
				socialBtn.setImageDrawable(getResources().getDrawable(R.drawable.purple_social_b_150));
				settingsBtn.setImageDrawable(getResources().getDrawable(R.drawable.purple_settings_b_150));
				backgroundSide.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_purple));
				backgroundMain.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_purple));
			}
		
		if(mySQLAdapter.isConnected)
			accountBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.logout));
		else
			accountBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.login));
		
		
		if(gaia.isFirstRun())
		{
			gaia.setFirstRun(false);
			showTutorial();
			
		}
	}

	/**
	 * 
	 * This method handles the resume of compass,  based on JYCompass, provided by google
	 * 
	 */
	@Override
	protected void onResume() {
		super.onResume();

		List<Sensor> sensors = mSensorManager
				.getSensorList(Sensor.TYPE_ORIENTATION);

		if (sensors.size() > 0) {

			Sensor sensor = sensors.get(0);

			mRegisteredSensor = mSensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
	}

	@Override
	protected void onPause() {
		if (mRegisteredSensor) {

			mSensorManager.unregisterListener(this);
			mRegisteredSensor = false;
		}
		super.onPause();
	}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * This method handles the sensor for compass,  based on JYCompass, provided by google
	 * 
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

			float x = event.values[SensorManager.DATA_X];

			gaia.setOrientation((int) x);

		}
		
	} 
	
	/**
	 * 
	 * This method handles the Tutorial
	 * 
	 */
	public void showTutorial(){
		//Get Dialog - TUTORIAL START
		final Dialog dialog = new Dialog(GaiaActivity.this);
		dialog.setTitle("Tutorial : Welcome To Gaia Navigator!");
		dialog.setContentView(R.layout.tutorial_startup);

		
		ImageView yesbtn = (ImageView) dialog.findViewById(R.id.yesbtn);
		ImageView nobtn = (ImageView) dialog.findViewById(R.id.nobtn);
		
		//Continue Tutorial
		yesbtn.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				
				dialog.dismiss();
				final Dialog dialog2 = new Dialog(GaiaActivity.this);
				dialog2.setTitle("Tutorial 1 : The Main Menu");
        		dialog2.setContentView(R.layout.tutorial_1);
        		dialog2.show();
        		
        	
        		ImageView previousbtn = (ImageView) dialog2.findViewById(R.id.previousbtn);
        	
        		previousbtn.setOnClickListener(new View.OnClickListener(){
        			public void onClick(View v){
        				dialog2.dismiss();
        				dialog.show();
        			}
        			});
        		
        		ImageView nextbtn = (ImageView) dialog2.findViewById(R.id.nextbtn);
        		
        		
        		//TUTORIAL 2 
        		nextbtn.setOnClickListener(new View.OnClickListener(){
        			public void onClick(View v){
        				
        				dialog2.dismiss();
        				final Dialog dialog3 = new Dialog(GaiaActivity.this);
        				dialog3.setTitle("Tutorial 2 : Travel Planner");
        				dialog3.setContentView(R.layout.tutorial_2);
        				dialog3.show();
        				
        				ImageView previousbtn2 = (ImageView) dialog3.findViewById(R.id.previousbtn2);
                		
        				previousbtn2.setOnClickListener(new View.OnClickListener(){
                			public void onClick(View v){
                				dialog3.dismiss();
                				dialog2.show();
                			}
                			});
        				
        				
        				ImageView nextbtn2 = (ImageView) dialog3.findViewById(R.id.nextbtn2);
        				//TUTORIAL 3 : Sub-menu 1 - add custom POI
        				nextbtn2.setOnClickListener(new View.OnClickListener(){
        					public void onClick(View v){
        					
        						dialog3.dismiss();
        						final Dialog dialog4 = new Dialog(GaiaActivity.this);
        						dialog4.setTitle("Tutorial 3 : Favourites - Add custom POI");
        						dialog4.setContentView(R.layout.tutorial_3);
        						dialog4.show();
        						
        						ImageView previousbtn3 = (ImageView) dialog4.findViewById(R.id.previousbtn3);
        						
        						previousbtn3.setOnClickListener(new View.OnClickListener(){
                        			public void onClick(View v){
                        				dialog4.dismiss();
                        				dialog3.show();
                        			}
                        			});
        						
        						ImageView nextbtn3 = (ImageView) dialog4.findViewById(R.id.nextbtn3);
        						
        					//TUTORIAL 4: Sub-menu 2 - Select from POI
        						nextbtn3.setOnClickListener(new View.OnClickListener(){
        							public void onClick(View v){
        								
        								dialog4.dismiss();
        								final Dialog dialog5 = new Dialog(GaiaActivity.this);
        								dialog5.setTitle("Tutorial 3 : Favourites - Select from POI list");
        								dialog5.setContentView(R.layout.tutorial_4);
        								dialog5.show();
        								
        								ImageView previousbtn4 = (ImageView) dialog5.findViewById(R.id.previousbtn4);
        								
        								previousbtn4.setOnClickListener(new View.OnClickListener(){
                                			public void onClick(View v){
                                				dialog5.dismiss();
                                				dialog4.show();
                                			}
                                			});
        								
        								
        								ImageView nextbtn4 = (ImageView) dialog5.findViewById(R.id.nextbtn4);
        								
        					//TUTORIAL 4 : Log in
        							nextbtn4.setOnClickListener(new View.OnClickListener(){
        								public void onClick(View v){
        									
        									dialog5.dismiss();
        									final Dialog dialog6 = new Dialog(GaiaActivity.this);
        									dialog6.setTitle("Tutorial 4 : Log in");
        									dialog6.setContentView(R.layout.tutorial_5);
        									dialog6.show();
        									
        									ImageView previousbtn5 = (ImageView) dialog6.findViewById(R.id.previousbtn5);
        									
        									previousbtn5.setOnClickListener(new View.OnClickListener(){
        	                        			public void onClick(View v){
        	                        				dialog6.dismiss();
        	                        				dialog5.show();
        	                        			}
        	                        			});
        									
        									
            								ImageView nextbtn4 = (ImageView) dialog6.findViewById(R.id.nextbtn5);
            								
            				//TUTORIAL 5: Social
            								nextbtn4.setOnClickListener(new View.OnClickListener(){
            									public void onClick(View v){
            										
            										dialog6.dismiss();
            										final Dialog dialog7 = new Dialog(GaiaActivity.this);
            										dialog7.setTitle("Tutorial 5 : Social");
            										dialog7.setContentView(R.layout.tutorial_6);
            										dialog7.show();
            										
            										ImageView previousbtn6 = (ImageView) dialog7.findViewById(R.id.previousbtn6);
            										
            										previousbtn6.setOnClickListener(new View.OnClickListener(){
                	                        			public void onClick(View v){
                	                        				dialog7.dismiss();
                	                        				dialog6.show();
                	                        			}
                	                        			});
            										
            										ImageView nextbtn6 = (ImageView) dialog7.findViewById(R.id.nextbtn6);
            				//TUTORIAL 6 : Settings
            								nextbtn6.setOnClickListener(new View.OnClickListener(){
            									public void onClick(View v){
            										
            										dialog7.dismiss();
            										final Dialog dialog8 = new Dialog(GaiaActivity.this);
            										dialog8.setTitle("Tutorial 6 : Settings");
            										dialog8.setContentView(R.layout.tutorial_7);
            										dialog8.show();
            										
            									ImageView previousbtn7 = (ImageView) dialog8.findViewById(R.id.previousbtn7);
            									
            									previousbtn7.setOnClickListener(new View.OnClickListener(){
            	                        			public void onClick(View v){
            	                        				dialog8.dismiss();
            	                        				dialog7.show();
            	                        			}
            	                        			});
            									
            									ImageView nextbtn7 = (ImageView) dialog8.findViewById(R.id.nextbtn7);
            									
            				//TUTORIAL END MENU DESCRIPTION	
            									nextbtn7.setOnClickListener(new View.OnClickListener(){
            										public void onClick(View v){
            											dialog8.dismiss();
            											final Dialog dialog9 = new Dialog(GaiaActivity.this);
            											dialog9.setTitle("Tutorial 7 : Map Consol");
            											dialog9.setContentView(R.layout.tutorial_8);
            											dialog9.show();
            											
            											ImageView previousbtn8 = (ImageView) dialog9.findViewById(R.id.previousbtn8);
            											 previousbtn8.setOnClickListener(new View.OnClickListener(){
            											
															@Override
															public void onClick(
																	View v) {
																
																dialog9.dismiss();
           													 	dialog8.show();
																
															}
            											 });
            											 
            											 ImageView nextbtn8 = (ImageView) dialog9.findViewById(R.id.nextbtn8);
            											 nextbtn8.setOnClickListener(new View.OnClickListener(){
            											
															@Override
															public void onClick(
																	View v) {
																
																dialog9.dismiss();
           													 	
																
															}
            											 });
            											 
            										}
            									});
            									}
            								});		
            									}
            								});		
        									
        								}
        							});
        							}
        						});
        					}
        				});
        			}
        		});
        		
			}
		});
		
		
		//Close Dialog
		
		nobtn.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				dialog.dismiss();
			}
		});
		dialog.show();
	
	}
	
	public static void changeArrow(int id)
	{
		arrow.setImageDrawable(myContext.getResources().getDrawable(id));
		
	}

}