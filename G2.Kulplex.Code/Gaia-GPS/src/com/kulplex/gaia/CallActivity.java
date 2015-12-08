/**
 * CallActivity
 * 
 * This Activity handles VOIP between two users.
 * 
 * The code is based on the SipDemo, provided on http://developer.android.com/resources/samples/SipDemo/index.html
 * And adapted for our SIP server
 * 
 * @author Jiayu Hu, Alberto Vaccari
 * 
 */


package com.kulplex.gaia;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.net.sip.*;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;

import com.kulplex.gaia.R;
import com.kulplex.gaia.handler.mySQLAdapter;

/**
 * Handles all calling, receiving calls, and UI interaction
 */
public class CallActivity extends Activity {

	public String sipAddress = null;

	FrameLayout background;
	static public GaiaApp gaia;
	
	public SipManager manager = null;
	public SipProfile me = null;
	public static SipAudioCall call = null;
	public IncomingCallReceiver callReceiver;

	private static String friendId;
	private static String friendName;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calllayout);

        Bundle extras = getIntent().getExtras();
        
        gaia = ((GaiaApp)getApplicationContext());
        background = (FrameLayout) findViewById(R.id.background);
        
        friendId = extras.getString("FriendId");
        friendName = extras.getString("FriendName");
        
       // sipAddress = friendName.toLowerCase() + "@ekiga.net";
        
        

		if(!mySQLAdapter.name.equals("nesh"))
		{
			sipAddress = "nesh108@ekiga.net";
		}
		else
		{
			sipAddress = "kulplex@ekiga.net";
		}

        // Set up the intent filter.  This will be used to fire an
        // IncomingCallReceiver when someone calls the SIP address used by this
        // application.
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.SipDemo.INCOMING_CALL");
        callReceiver = new IncomingCallReceiver();
        this.registerReceiver(callReceiver, filter);

        // "Push to talk" can be a serious pain when the screen keeps turning off.
        // Let's prevent that.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        ImageView call = (ImageView) findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				try {
					
					
					
					initiateCall();
				} catch (Exception se) {
				}
			
			}
		});
        
        ImageView BackBtn = (ImageView) findViewById(R.id.BackBtn);

		BackBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				
				finish();
				Intent intent = new Intent(CallActivity.this,
						FriendListActivity.class);
				startActivity(intent);
			}
		});
        
		ImageView endcall = (ImageView) findViewById(R.id.end);
        endcall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				endCall();
				
			}
		});
        
        themeHandler();
        initializeManager();
        
    }

	@Override
	public void onStart() {
		super.onStart();
		// When we get back from the preference setting Activity, assume
		// settings have changed, and re-login with new auth info.
		initializeManager();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (call != null) {
			call.close();
		}

		closeLocalProfile();

		if (callReceiver != null) {
			this.unregisterReceiver(callReceiver);
		}
	}


	
	public void initializeManager() {
	
			if (manager == null) {
		
			manager = SipManager.newInstance(this);
		}

		initializeLocalProfile();

	}

	/**
	 * Logs you into your SIP provider, registering this device as the location
	 * to send SIP calls to for your SIP address.
	 */
	public void initializeLocalProfile() {
		if (manager == null) {
			return;
		}

		if (me != null) {
			closeLocalProfile();
		}

		String username;
		String domain;
		String password;
		
		if(!mySQLAdapter.name.equals("nesh"))
		{
			username = "kulplex";
			domain = "ekiga.net";
			password = "gaia2012@";
		}
		else
		{
			username = "nesh108";
			domain = "ekiga.net";
			password = "gaia2012\\\\\\\"";
		}
			
		try {
			SipProfile.Builder builder = new SipProfile.Builder(username,
					domain);
			builder.setPassword(password);
			me = builder.build();

			
			
			Intent i = new Intent();
			i.setAction("android.SipDemo.INCOMING_CALL");
			PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,
					Intent.FILL_IN_DATA);
			manager.open(me, pi, null);
			// This listener must be added AFTER manager.open is called,
			// Otherwise the methods aren't guaranteed to fire.


			manager.setRegistrationListener(me.getUriString(),
					new SipRegistrationListener() {
						public void onRegistering(String localProfileUri) {
							updateStatus("Registering with SIP Server...");
						}

						public void onRegistrationDone(String localProfileUri,
								long expiryTime) {
							updateStatus("Ready");
						}

						public void onRegistrationFailed(
								String localProfileUri, int errorCode,
								String errorMessage) {
							updateStatus("Registration failed.  Please check settings.");
						}
					});
		} catch (ParseException pe) {
			updateStatus("Connection Error.");
		} catch (SipException se) {
			updateStatus("Connection error.");
		}
	}

	/**
	 * Closes out your local profile, freeing associated objects into memory and
	 * unregistering your device from the server.
	 */
	public void closeLocalProfile() {
		if (manager == null) {
			return;
		}
		try {
			if (me != null) {
				manager.close(me.getUriString());
			}
		} catch (Exception ee) {
			Log.d("onDestroy",
					"Failed to close local profile.", ee);
		}
	}

	/**
	 * Make an outgoing call.
	 */
	public void initiateCall() {

		updateStatus(sipAddress);

		try {
			SipAudioCall.Listener listener = new SipAudioCall.Listener() {
				// Much of the client's interaction with the SIP Stack will
				// happen via listeners. Even making an outgoing call, don't
				// forget to set up a listener to set things up once the call is
				// established.
				@Override
				public void onCallEstablished(SipAudioCall call) {
					call.startAudio();
					call.setSpeakerMode(true);
					//call.toggleMute();
					updateStatus(call);
				}

				@Override
				public void onCallEnded(SipAudioCall call) {
					updateStatus("Ready.");
				}
			};

			call = manager.makeAudioCall(me.getUriString(), sipAddress,
					listener, 30);

		} catch (Exception e) {
			Log.i("InitiateCall",
					"Error when trying to close manager.", e);
			if (me != null) {
				try {
					manager.close(me.getUriString());
				} catch (Exception ee) {
					Log.i("InitiateCall",
							"Error when trying to close manager.", ee);
					ee.printStackTrace();
				}
			}
			if (call != null) {
				call.close();
			}
		}
	}

	public void endCall()
	{
		if (call != null) {
			try {
				call.endCall();
			} catch (SipException se) {
				Log.d("onOptionsItemSelected",
						"Error ending call.", se);
			}
			call.close();
		}
	}
	
	/**
	 * Updates the status box at the top of the UI with a messege of your
	 * choice.
	 * 
	 * @param status
	 *            The String to display in the status box.
	 */
	public void updateStatus(final String status) {
		// Be a good citizen. Make sure UI changes fire on the UI thread.
		this.runOnUiThread(new Runnable() {
			public void run() {
				TextView labelView = (TextView) findViewById(R.id.sipLabel);
				labelView.setText(status);
			}
		});
	}

	/**
	 * Updates the status box with the SIP address of the current call.
	 * 
	 * @param call
	 *            The current, active call.
	 */
	public void updateStatus(SipAudioCall call) {
		String useName = call.getPeerProfile().getDisplayName();
		if (useName == null) {
			useName = call.getPeerProfile().getUserName();
		}
		updateStatus(useName + "@" + call.getPeerProfile().getSipDomain());
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