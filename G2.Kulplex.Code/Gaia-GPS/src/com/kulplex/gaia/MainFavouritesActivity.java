/**
 * MainFavouritesActivity
 * 
 * This Activity handles the main favourite activity features
 * 
 * @author Petra Ceczi, Jiayu Hu
 * 
 */

package com.kulplex.gaia;


import com.kulplex.gaia.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
 
@SuppressWarnings("deprecation")
public class MainFavouritesActivity extends   TabActivity implements OnCheckedChangeListener {
    
	public ListView listView;
	public List<Map<String, Object>> list;
	public CheckBox cball;
	public Button btnsave;
	public Button btncancel;
	
	
	ListViewAdapter adapter;
	String[] mTestArray;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Keeps screen alive
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
        setContentView(R.layout.mainfavourites);
 
        TabHost tabHost = getTabHost();
 
        
        TabSpec favouritespec = tabHost.newTabSpec("Favourites");
        
        favouritespec.setIndicator("Favourites", getResources().getDrawable(R.drawable.icon_favourites_tab));
        Intent favouriteIntent = new Intent(this, TabFavouritesActivity.class);
        favouritespec.setContent(favouriteIntent);
 
        
        TabSpec poispec = tabHost.newTabSpec("POI");
        poispec.setIndicator("POI", getResources().getDrawable(R.drawable.icon_poi_tab));
        Intent poiIntent = new Intent(this, TabPOIActivity.class);
        poispec.setContent(poiIntent);
 

        tabHost.addTab(favouritespec); 
        tabHost.addTab(poispec);
    }

	
    public void onCheckedChanged1(CompoundButton buttonView, boolean isChecked) {
		
	}
    
    /**
	 * 
	 * This method handles the information need to map
	 * 
	 */
    @SuppressWarnings("unused")
	private List<Map<String, Object>> buildList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		mTestArray = getResources().getStringArray(R.array.testArray);

		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", R.drawable.ic_launcher);
			map.put("title", mTestArray[i]);
			list.add(map);
		}
		return list;
	}
	
    /**
	 * 
	 * This method creates own adapter for listview
	 * 
	 */
	@SuppressWarnings("unused")
	class ListViewAdapter extends BaseAdapter {
		private Context context;
		private List<Map<String, Object>> list;
		private LayoutInflater inflater;
		private List<Boolean> checkBoxesStatus;

		public ListViewAdapter(Context context, List<Map<String, Object>> list) {
			super();
			this.context = context;
			this.list = list;
			int listsize = list.size();
			checkBoxesStatus = new ArrayList<Boolean>(listsize);
			for (int i = 0; i < list.size(); i++) {
				checkBoxesStatus.add(false);
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
				convertView = inflater.inflate(com.kulplex.gaia.R.layout.list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.icon);
				viewHolder.tv = (TextView) convertView.findViewById(com.kulplex.gaia.R.id.title);
				viewHolder.cb = (CheckBox) convertView.findViewById(com.kulplex.gaia.R.id.cb);
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

/**
 * 
 * This method handles the listener when items in the listview are clicked
 * 
 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		List<Boolean> checkBoxesStatus = adapter.getCheckBoxesStatus();
		for (int i = 0; i < list.size(); i++) {
			checkBoxesStatus.set(i, isChecked);
		}
		adapter.notifyDataSetChanged();
	}



	public ListView getListView() {
		return listView;
	}



	public void setListView(ListView listView) {
		this.listView = listView;
	}



	public CheckBox getCball() {
		return cball;
	}



	public void setCball(CheckBox cball) {
		this.cball = cball;
	}



	public Button getBtnsave() {
		return btnsave;
	}



	public void setBtnsave(Button btnsave) {
		this.btnsave = btnsave;
	}



	public Button getBtncancel() {
		return btncancel;
	}



	public void setBtncancel(Button btncancel) {
		this.btncancel = btncancel;
	}
	
	
}