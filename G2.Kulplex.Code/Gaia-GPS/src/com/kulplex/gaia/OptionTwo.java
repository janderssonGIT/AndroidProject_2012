package com.kulplex.gaia;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kulplex.gaia.obj.GaiaPOI;
import com.kulplex.gaia.obj.GaiaPathPoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class OptionTwo extends Activity implements
	OnItemSelectedListener {
		 
	 GaiaApp gaia;
	 LinearLayout background;
	 FrameLayout background2;
	 ListViewAdapter adapter;
	 private List<Map<String, Object>> list;
	 public ListView pplv;   //PPLV = PlannerPOIListView
 	 final Context context = this;

    
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.opt2);
        
		gaia = ((GaiaApp)getApplicationContext());		
		background = (LinearLayout) findViewById(R.id.background); 
		background2 = (FrameLayout) findViewById(R.id.frameLayout1);    
        pplv = (ListView) findViewById(R.id.lv);			
        themeHandler();		   		
        ImageView OTB = (ImageView) findViewById(R.id.optback2);
        OTB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	Intent intent = new Intent(OptionTwo.this, PlannerActivity.class);
            	startActivity(intent);
                finish(); 
            }

        });
        
        
        
        list = buildList();
		adapter = new ListViewAdapter(this, list);
		pplv.setAdapter(adapter);
		pplv.setOnItemClickListener(listViewListener);
    }
   
	private List<Map<String, Object>> buildList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ArrayList<String[]> poiList = loadPOI();
		
		for (int i = 0; i < poiList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", R.drawable.arrow);
			map.put("title", poiList.get(i)[0]);
			map.put("description", poiList.get(i)[1]);
			list.add(map);
		}
		return list;
	}
   
	private ArrayList<String[]> loadPOI()
	{
		ArrayList<GaiaPOI> poi = gaia.getCustomPOI();
		
		ArrayList<String[]> p = new ArrayList<String[]>();
		for(int i = 0; i < poi.size(); i ++)
		{
			p.add(new String[]{poi.get(i).getName(),poi.get(i).getLatitude()/ 1000000 + "," + poi.get(i).getLongitude()/ 1000000});
		}
		
		return p;
		
		
	}
    
    
   private ListView.OnItemClickListener listViewListener = new ListView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			final int pos = position;

			final String selectedItemName = (String)((HashMap)parent.getItemAtPosition(position)).get("title");
			
			for(int i = 0; i< gaia.getCustomPOI().size(); i++){

				if(selectedItemName.equals(gaia.getCustomPOI().get(i).getName())){
					
					String gppName = (int)gaia.getCustomPOI().get(i).getLatitude()+","+(int)gaia.getCustomPOI().get(i).getLongitude();
					GaiaPathPoint gp = new GaiaPathPoint(gppName, gppName);
					gaia.setPlannedPlaceLoc(gp);

				}
				
			}
			
			 Intent intent = new Intent(OptionTwo.this, Loading.class);
			 startActivity(intent);
             finish(); 
             
			Toast.makeText(OptionTwo.this, pos + " " + selectedItemName, Toast.LENGTH_SHORT).show();

		}
	};  
    
public void themeHandler(){
		
		
		if(gaia.getChosenTheme().equals("Black Theme"))
		{
			
			background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_black));
			background2.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_black));
			pplv.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_black));
		}
				
		if(gaia.getChosenTheme().equals("Blue Theme"))
		{
		
			background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_blue));
			background2.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_blue));
			pplv.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_blue));
		}
		else
			if(gaia.getChosenTheme().equals("Orange Theme"))
			{
				
				background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_orange));
				background2.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_orange));
				pplv.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_orange));
			}
		else
			if(gaia.getChosenTheme().equals("Green Theme"))
			{
				
				background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_green));
				background2.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_green));
				pplv.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_green));
			}
		else
			if(gaia.getChosenTheme().equals("Purple Theme"))
			{
				
				background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_purple));
				background2.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_purple));
				pplv.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_purple));
			}
		
		
	}


@Override
public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	// TODO Auto-generated method stub
	
}


@Override
public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
	
}



class ListViewAdapter extends BaseAdapter {
	@SuppressWarnings("unused")
	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater inflater;
	
	GaiaApp gaia = ((GaiaApp)getApplicationContext());
	
	public ListViewAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.list = list;
		int listsize = list.size();
		
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
			convertView = inflater.inflate(R.layout.plannerlist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.img = (ImageView) convertView
					.findViewById(R.id.icon);
			viewHolder.tv = (TextView) convertView.findViewById(R.id.title);
			viewHolder.tv2 = (TextView) convertView.findViewById(R.id.description);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.img.setImageResource((Integer) map.get("icon"));
		viewHolder.tv.setText((String) map.get("title"));
		viewHolder.tv2.setText((String) map.get("description"));

		return convertView;
	}
}
static class ViewHolder {
	private ImageView img;
	private TextView tv;
	private TextView tv2;
}


}