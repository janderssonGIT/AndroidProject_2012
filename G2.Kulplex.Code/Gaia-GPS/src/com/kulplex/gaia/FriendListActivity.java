/**
 * FriendListActivity
 * 
 * This Activity handles the friendlist and users status.
 * 
 * 
 * @author Henry Dang, Alberto Vaccari
 * 
 */

package com.kulplex.gaia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.kulplex.gaia.handler.mySQLAdapter;
import com.kulplex.gaia.R;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class FriendListActivity extends Activity {

   

	static public ArrayList<String[]> friends;
	static public Handler mHandler;
	
	static public GaiaApp gaia;
	
	FrameLayout background;
	static public ListView lv;
	
	static public  ListViewAdapter adapter;
	static public  List<Map<String, Object>> list;
	
	static public Context mContext;
	
	static Boolean getFriends = false;
	
    Spinner setStatus;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendlistlayout);

        gaia = ((GaiaApp)getApplicationContext());
       
        getFriends = true;
        
        mHandler = new Handler();
        lv = (ListView) findViewById(R.id.list);
		
        mContext = this;
        
		loadFriends();
        		
		background = (FrameLayout) findViewById(R.id.background);
        
        setStatus = (Spinner) findViewById(R.id.status);
            
        setStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        		
        		mySQLAdapter.setStatus(setStatus.getSelectedItem().toString());
        		        		
        	}
        	public void onNothingSelected(AdapterView<?> parent) {
        		
        	}
        	
        	
		});      
        
		ImageView BackBtn = (ImageView) findViewById(R.id.BackBtn);

		BackBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				getFriends = false;
				finish();
				Intent intent = new Intent(FriendListActivity.this,
						SocialActivity.class);
				startActivity(intent);
			}
		});
		
		 Button Search = (Button) findViewById(R.id.Search);
		   
         Search.setOnClickListener(new View.OnClickListener() {
              	
              public void onClick(View v) {
              	

					Intent intent = new Intent(FriendListActivity.this,
							SearchListActivity.class);
					startActivity(intent);
					finish();
					
				}
				}); 
		

        themeHandler();

        lv.setOnItemClickListener(listViewListener);
        
    }
 
	/**
	 * 
	 * Composes the list before being shown
	 * 
	 */
    
    private static List<Map<String, Object>> buildList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		
		for (int i = 0; i < friends.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", friends.get(i)[1]);
			
			if(friends.get(i)[2].equals("Online"))
				map.put("status", R.drawable.online_icon);
			else
				if(friends.get(i)[2].equals("Offline"))
					map.put("status", R.drawable.offline_icon);
				else
					if(friends.get(i)[2].equals("Busy"))
						map.put("status", R.drawable.busy_icon);
					else
						if(friends.get(i)[2].equals("Away"))
							map.put("status", R.drawable.away_icon);
			
			map.put("message", R.drawable.message);
			map.put("call", R.drawable.call);
			list.add(map);
		}
		return list;
	}
    
    /**
     * Gets friends from list.
     */
    private void loadFriends()
    {
    	
    	FriendListActivity.friends = new ArrayList<String[]>();
    	 LoadTask t = new LoadTask(this);
         t.execute();
         
     
    	
    }
    
    /**
     * Adding buttons to list view
     * 
     */

    private ListView.OnItemClickListener listViewListener = new ListView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {		
			
			final int pos = position;
			
			final String selectedItemName = (String)((HashMap)parent.getItemAtPosition(pos)).get("name");
			
			
			final Dialog dialog = new Dialog(FriendListActivity.this);
            dialog.setContentView(R.layout.friendoption_dialog);
            String appName = getResources().getString(R.string.app_name);
            dialog.setCancelable(true);
            

            //set up button
            ImageView buttonCancel = (ImageView) dialog.findViewById(R.id.ButtonCancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
			
            ImageView buttonMessage = (ImageView) dialog.findViewById(R.id.ButtonMessage);
            buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    

	    			getFriends = false;
	    			finish();
	    			Intent intent = new Intent(FriendListActivity.this,
	    					ChatActivity.class);
	    			
	    			intent.putExtra("FriendName", selectedItemName);
	    			intent.putExtra("FriendId", friends.get(pos)[0]);
	    			
	    			startActivity(intent);
                }
            });
			
			
            ImageView buttonCall = (ImageView) dialog.findViewById(R.id.ButtonCall);
            buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {

    			getFriends = false;
    			finish();
    			Intent intent = new Intent(FriendListActivity.this,
    					CallActivity.class);
    			
    			intent.putExtra("FriendName", selectedItemName);
    			intent.putExtra("FriendId", friends.get(pos)[0]);
    			
    			startActivity(intent);
    			
                }
            });
            dialog.show();
		}
	};
	
	/**
	 * Handles fetching from the server
	 */
	
    private static class LoadTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {

        }
        
       public Context context;
        
        public LoadTask(Context context) {
            this.context = context;
         }

        protected Void doInBackground(Void... params) {
        	
        	while(getFriends)
        	{
	        	FriendListActivity.friends = mySQLAdapter.getFriendList();
	           
	
				mHandler.post(new Runnable() {
					@Override
					public void run() {
				
						 list = buildList();
	
						 adapter = new ListViewAdapter(context, list);
	
						 lv.setAdapter(adapter);
	
					}
	
				});
			}

            return null;
        }
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
  
 public static class ListViewAdapter extends BaseAdapter {
		private Context context;
		private List<Map<String, Object>> list;
		private LayoutInflater inflater;
		
	
		public ListViewAdapter(Context context, List<Map<String, Object>> list) {
			super();
			this.list = list;
			this.context = context;
			this.inflater = LayoutInflater.from(context);
		}

		public List<Map<String, Object>> getList() {
			return list;
		}


		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder viewHolder = null;
			Map<String, Object> map = list.get(position);
			
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.friendlist_item, null);
				viewHolder = new ViewHolder();
				
				viewHolder.name = (TextView) convertView.findViewById(R.id.name);
				viewHolder.status = (ImageView) convertView
						.findViewById(R.id.status);
				viewHolder.message = (ImageView) convertView.findViewById(R.id.message);
				
				viewHolder.call = (ImageView) convertView.findViewById(R.id.call);
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.name.setText((String) map.get("name"));
			viewHolder.message.setImageResource((Integer) map.get("message"));
			viewHolder.call.setImageResource((Integer) map.get("call"));
			viewHolder.status.setImageResource((Integer) map.get("status"));
			

			return convertView;
		}
	}
	static class ViewHolder {
		private TextView name;
		private ImageView status;
		private ImageView call;
		private ImageView message;
	}
	

}