package com.kulplex.gaia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TabPOIActivity extends Activity implements
		OnCheckedChangeListener {
	
	private ListView listView;
	private List<Map<String, Object>> list;
	private CheckBox cball;
	private Button btnsave;
	private Button btncancel;
	ListViewAdapter adapter;
	
	String poiSet;	
	String poiDefSet;
	
	String[] POIList;
	public GaiaApp gaia;
	LinearLayout background;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences settings = getSharedPreferences(SettingsActivity.prefsFile, 0);
		final SharedPreferences.Editor editor = settings.edit();
        // Keeps screen alive
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.favouriteslayout);

		poiSet = "";

		poiDefSet = "f, f, f, f, f, t, t, t, t";

		gaia = ((GaiaApp)getApplicationContext());

		poiSet = settings.getString("poiStates", poiDefSet);

		gaia.setPOIstate(strToBool(poiSet));

		background = (LinearLayout) findViewById(R.id.background);
		listView = (ListView) findViewById(R.id.lv);
		
        themeHandler();
        
		
		listView.setBackgroundResource(R.drawable.drawerbackground);
		cball = (CheckBox) findViewById(R.id.cball);
		cball.setOnCheckedChangeListener(this);// 

		
		
		btnsave = (Button) findViewById(R.id.btnsave);
		btncancel = (Button) findViewById(R.id.btncancel);

		btnsave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				for (int i = 0; i < adapter.checkBoxesStatus.size(); i++) {
					System.out.println(adapter.checkBoxesStatus.get(i));
					
					gaia.POIstate[i] = adapter.checkBoxesStatus.get(i);
				}
				
				editor.putString("poiStates", boolToStr(gaia.getPOIstate()));
				editor.commit();
				
				Intent intent = new Intent(TabPOIActivity.this,
						GaiaActivity.class);
				startActivity(intent);
				finish();
				

			}
		});

		btncancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(TabPOIActivity.this,
						GaiaActivity.class);
				startActivity(intent);
				finish();

			}
		});
		list = buildList();
		adapter = new ListViewAdapter(this, list);
		listView.setAdapter(adapter);
	}

	private List<Map<String, Object>> buildList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		POIList = getResources().getStringArray(R.array.POIList);
		
		for (int i = 0; i < POIList.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", gaia.POIicon[i]);
			map.put("title", POIList[i]);
			list.add(map);
		}
		return list;
	}

	class ListViewAdapter extends BaseAdapter {
		@SuppressWarnings("unused")
		private Context context;
		private List<Map<String, Object>> list;
		private LayoutInflater inflater;
		private List<Boolean> checkBoxesStatus;
		
		GaiaApp gaia = ((GaiaApp)getApplicationContext());
		
		public ListViewAdapter(Context context, List<Map<String, Object>> list) {
			super();
			this.context = context;
			this.list = list;
			int listsize = list.size();
			checkBoxesStatus = new ArrayList<Boolean>(listsize);
			for (int i = 0; i < list.size(); i++) {
				checkBoxesStatus.add(gaia.POIstate[i]);
			}
			this.inflater = LayoutInflater.from(context);
		}

		public List<Map<String, Object>> getList() {
			return list;
		}

		public List<Boolean> getCheckBoxesStatus() {
			return checkBoxesStatus;
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
			Boolean checkBoxStatus = checkBoxesStatus.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.icon);
				viewHolder.tv = (TextView) convertView.findViewById(R.id.title);
				viewHolder.cb = (CheckBox) convertView.findViewById(R.id.cb);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.img.setImageResource((Integer) map.get("icon"));
			viewHolder.tv.setText((String) map.get("title"));

			viewHolder.cb.setId(position);
			viewHolder.cb.setChecked(checkBoxStatus);
			viewHolder.cb
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							checkBoxesStatus.set(buttonView.getId(), isChecked);
							notifyDataSetChanged();
						}
					});
			return convertView;
		}
	}

	static class ViewHolder {
		private ImageView img;
		private TextView tv;
		private CheckBox cb;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		List<Boolean> checkBoxesStatus = adapter.getCheckBoxesStatus();
		for (int i = 0; i < list.size(); i++) {
			checkBoxesStatus.set(i, isChecked);
		}
		adapter.notifyDataSetChanged();
	}
	
	public void themeHandler(){
		
		if(gaia.getChosenTheme().equals("Black Theme"))
		{
			
			background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_black));
			listView.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_black));
			
		}
				
		if(gaia.getChosenTheme().equals("Blue Theme"))
		{
		
			background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_blue));
			listView.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_blue));
			
		}
		else
			if(gaia.getChosenTheme().equals("Orange Theme"))
			{
				
				background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_orange));
				listView.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_orange));

			}
		else
			if(gaia.getChosenTheme().equals("Green Theme"))
			{
				
				background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_green));
				listView.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_green));

			}
		else
			if(gaia.getChosenTheme().equals("Purple Theme"))
			{
				
				background.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_purple));
				listView.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawerbackground_purple));

			}
		
		
	}
	
	private Boolean[] strToBool(String a){

		String[] temp = a.split(",");
		
		Boolean[] bools = new Boolean[temp.length];
		
		for(int i = 0; i < temp.length; i++){
			
			bools[i] = sToB(temp[i].trim());
			
		}
	
		return bools;
	
	}
	
	private String boolToStr(Boolean[] a){
		
		String full = "";
		
		for(int i = 0; i < a.length; i++){
			
			full += bToS(a[i]);
			
			if(i+1<a.length)
				full += ",";
			
		}
		
		return full;
	}
	
	private String bToS(Boolean a){
		
		if(a)
			return "t";
		else 
			return "f";	
	}
	
	private Boolean sToB(String a){
		
		if(a.equals("t"))
			return true;
		else 
			return false;	
	}


}
