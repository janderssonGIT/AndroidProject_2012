/**
 * SearchListActivity
 * 
 * This Activity handles the searchlist and add users to the friendlist.
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchListActivity extends Activity {

	static public Handler mHandler;
	
	static public GaiaApp gaia;
	
	LinearLayout background;
	static public ListView lv;
	
	static public  ListViewAdapter adapter;
	static public  List<Map<String, Object>> list;
	
	static public Context mContext;
	
	static public TextView SearchBox;
	static public ArrayList<String> searchResult;

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist);

        gaia = ((GaiaApp)getApplicationContext());
       
        
        mHandler = new Handler();
        lv = (ListView) findViewById(R.id.list);
		
        mContext = this;
     
		
		background = (LinearLayout) findViewById(R.id.background);
        
		
		ImageView BackBtn = (ImageView) findViewById(R.id.BackBtn);

		BackBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				finish();
				Intent intent = new Intent(SearchListActivity.this,
						FriendListActivity.class);
				startActivity(intent);
			}
		});
	      
        SearchBox = (TextView) findViewById(R.id.SearchBox);
        
        
        Button Enter = (Button) findViewById(R.id.Enter);
		   
        Enter.setOnClickListener(new View.OnClickListener() {
             	
             public void onClick(View v) {
             	
             		SearchUsers();
             	
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
		
		
		for (int i = 0; i < searchResult.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", searchResult.get(i));
			map.put("add", R.drawable.add_poi);

			list.add(map);
		}
		
		if(searchResult.size() == 0)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", "Not Found");
			map.put("add", R.drawable.list_divider);
			
			list.add(map);
		}
		
		
		
		return list;
	}
    
    /**
     * Gets users from list.
     */
    
    private void SearchUsers()
    {
    
    	 LoadTask t = new LoadTask(this);
         t.execute();
          	
    }
    
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
        	
        	String search = SearchBox.getText().toString();
         	
         	if(search.trim() != "" && search != null)
         		searchResult = mySQLAdapter.getSearchList(search);
         	
         	System.out.println(searchResult.toString());
	           
	
				mHandler.post(new Runnable() {
					@Override
					public void run() {
				
						 list = buildList();
	
						 adapter = new ListViewAdapter(context, list);
	
						 lv.setAdapter(adapter);
	
					}
	
				});
		

            return null;
        }
    }

    /**
     * Add user to friendlist
     * 
     */
    private ListView.OnItemClickListener listViewListener = new ListView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {		
			
			    if(mySQLAdapter.addFriend(getBaseContext(), searchResult.get(position)))
			    	Toast.makeText(getBaseContext(), "Added Friend.", Toast.LENGTH_SHORT).show();
			
			    else
			    	Toast.makeText(getBaseContext(), "Friend already added.", Toast.LENGTH_SHORT).show();
				
			    	
                }
            };
            
            
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
				convertView = inflater.inflate(R.layout.searchlist_item, null);
				viewHolder = new ViewHolder();
				
				viewHolder.name = (TextView) convertView.findViewById(R.id.name);
				viewHolder.add = (ImageView) convertView
						.findViewById(R.id.add);
				
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.name.setText((String) map.get("name"));
			viewHolder.add.setImageResource((Integer) map.get("add"));
			

			return convertView;
		}
	}
	static class ViewHolder {
		private TextView name;
		private ImageView add;
	}
	

}