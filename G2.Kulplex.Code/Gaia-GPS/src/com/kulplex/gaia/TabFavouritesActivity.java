/**
 * TabFavouritesActivity
 * 
 * This Activity handles the tab part of favourite and deal with user information
 * 
 * @author Jiayu Hu, Petra Beczi, Alberto Vaccari, Jim Andersson
 * 
 */

package com.kulplex.gaia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kulplex.gaia.GaiaApp;
import com.kulplex.gaia.R;
import com.kulplex.gaia.obj.GaiaPOI;
import com.kulplex.gaia.obj.GaiaPoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TabFavouritesActivity extends Activity implements
		OnItemSelectedListener {

	FrameLayout background;
	GaiaApp gaia;
	public ListView listview;

	final Context context = this;
	private ImageView addfavoruritebtn;
	private ImageView poiBackBtn;

	private Spinner sIcon;

	private List<Map<String, Object>> list;
	ListViewAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getSharedPreferences(
				SettingsActivity.prefsFile, 0);
		final SharedPreferences.Editor editor = settings.edit();
		setContentView(R.layout.poi_layout);
		gaia = ((GaiaApp) getApplicationContext());

		background = (FrameLayout) findViewById(R.id.background);

		themeHandler();

		listview = (ListView) findViewById(R.id.list);

		loadPOI();

		listview.setOnItemClickListener(listViewListener);

		// Keeps screen alive
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		addfavoruritebtn = (ImageView) findViewById(R.id.addfavouritesbtn);
		poiBackBtn = (ImageView) findViewById(R.id.poiBackBtn);

		addfavoruritebtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				// Get Dialog
				final Dialog dialog = new Dialog(context);

				dialog.setContentView(R.layout.add_favourites);
				dialog.setTitle("Add Custom Location");
				sIcon = (Spinner) dialog.findViewById(R.id.iconChooser);

				ArrayAdapter<CharSequence> adapter = ArrayAdapter
						.createFromResource(getBaseContext(),
								R.array.icon_arrays,
								android.R.layout.simple_spinner_item);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				sIcon.setAdapter(adapter);
				// Components of the dialog
				TextView name = (TextView) dialog
						.findViewById(R.id.favouritesnametxt);

				final EditText addpoitextbox = (EditText) dialog
						.findViewById(R.id.writename);

				TextView longitude = (TextView) dialog
						.findViewById(R.id.favouriteslongitude);

				final EditText addpoilongitude = (EditText) dialog
						.findViewById(R.id.writelongitude);

				TextView latitude = (TextView) dialog
						.findViewById(R.id.favouriteslatitude);

				final EditText addpoilatitude = (EditText) dialog
						.findViewById(R.id.writelatitude);

				TextView choose_icon = (TextView) dialog
						.findViewById(R.id.favouritesiconstxt);

				// Spinner selectIcon = (Spinner)

				sIcon = (Spinner) dialog.findViewById(R.id.iconChooser);

				Button ok_add_favourites = (Button) dialog
						.findViewById(R.id.addfavouritesOk);
				Button cancel_add_favourites = (Button) dialog
						.findViewById(R.id.addfavouritesCancel);

				/**
				 * 
				 * This method handles to add new places
				 * 
				 */
				ok_add_favourites.setOnClickListener(new View.OnClickListener() {

							public void onClick(View v) {

								try {
									String poiname = addpoitextbox.getText().toString();
									String poilongitude = addpoilongitude.getText().toString();
									String poilatitude = addpoilatitude.getText().toString();

									System.out.println(poiname);
									if (poiname.equals("")) {
										Toast.makeText(getBaseContext(),
												"Please insert a valid name",
												Toast.LENGTH_SHORT).show();
									} else {

										if (poilongitude.equals("")
												&& poilatitude.equals("")) {

											addPOI(poiname, gaia.curPos.getLongitude(), gaia.curPos.getLatitude(),
													(int) sIcon.getSelectedItemId());
											editor.putStringSet("FavSet", gaia.getCustomPOISet());
											editor.commit();
											dialog.dismiss();
										} else if (poilongitude.equals("") || poilatitude.equals("")) {
											Toast.makeText(
													dialog.getContext(),
													"Please insert valid coordinates",
													Toast.LENGTH_SHORT).show();
										}

										else if (!poilongitude.equals("") && !poilatitude.equals("")) {
											addPOI(poiname, Float.parseFloat(poilongitude), Float.parseFloat(poilatitude),
													(int) sIcon.getSelectedItemId());

											editor.putStringSet("FavSet", gaia.getCustomPOISet());
											editor.commit();
											dialog.dismiss();
										}
									}
								} catch (Exception e) {
									Toast.makeText(dialog.getContext(),
											"Please insert valid coordinates",
											Toast.LENGTH_SHORT).show();
								}
							}

						});

				/**
				 * 
				 * This method handles to close the dialog
				 * 
				 */
				cancel_add_favourites
						.setOnClickListener(new View.OnClickListener() {

							public void onClick(View v) {
								dialog.dismiss();
							}
						});
				dialog.show();
			}
		});

		poiBackBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				finish();
				Intent intent = new Intent(TabFavouritesActivity.this,
						GaiaActivity.class);
				startActivity(intent);
			}
		});

		list = buildList();
		adapter = new ListViewAdapter(this, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(listViewListener);

	}

	/**
	 * 
	 * This method handles the listener for listview
	 * 
	 */
	private ListView.OnItemClickListener listViewListener = new ListView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			final int pos = position;
			final String selectedItemName = (String) ((HashMap) parent
					.getItemAtPosition(position)).get("title");

			AlertDialog.Builder delete = new AlertDialog.Builder(
					TabFavouritesActivity.this);
			delete.setTitle("DELETE Selected Address?");
			delete.setMessage(selectedItemName);
			delete.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {

							removePOI(pos);
						}
					});
			delete.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {

						}
					});
			delete.show();
		}
	};

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	/**
	 * 
	 * This method handles the own adapter for listview
	 * 
	 */
	class ListViewAdapter extends BaseAdapter {
		@SuppressWarnings("unused")
		private Context context;
		private List<Map<String, Object>> list;
		private LayoutInflater inflater;

		GaiaApp gaia = ((GaiaApp) getApplicationContext());

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
				viewHolder.tv2 = (TextView) convertView
						.findViewById(R.id.description);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.img.setImageResource(Integer.parseInt(map.get("icon")
					.toString()));
			viewHolder.tv.setText((String) map.get("title"));
			viewHolder.tv2.setText((String) map.get("description"));

			return convertView;
		}
	}

	/**
	 * 
	 * This method handles to add new pois
	 * 
	 */
	private void addPOI(String name, float lon, float lat, int id) {

		GaiaPOI gp = new GaiaPOI(name, new GaiaPoint(lon, lat), id);
		gaia.addPOI(gp);
		list = buildList();
		adapter = new ListViewAdapter(this, list);
		listview.setAdapter(adapter);
	}

	/**
	 * 
	 * This method handles to delete exist pois
	 * 
	 */
	private void removePOI(int id) {
		gaia.removePOI(gaia.getCustomPOI().get(id));

		list = buildList();
		adapter = new ListViewAdapter(this, list);
		listview.setAdapter(adapter);

	}

	private List<Map<String, Object>> buildList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ArrayList<String[]> poiList = loadPOI();

		for (int i = 0; i < poiList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", poiList.get(i)[2]);
			map.put("title", poiList.get(i)[0]);
			map.put("description", poiList.get(i)[1]);
			list.add(map);
		}
		return list;
	}

	private ArrayList<String[]> loadPOI() {
		ArrayList<GaiaPOI> poi = gaia.getCustomPOI();

		ArrayList<String[]> p = new ArrayList<String[]>();
		for (int i = 0; i < poi.size(); i++) {
			p.add(new String[] {
					poi.get(i).getName(),
					poi.get(i).getLatitude() / 1000000 + ","
							+ poi.get(i).getLongitude() / 1000000,
					"" + poi.get(i).getIcon() });
		}

		return p;

	}

	public void themeHandler() {

		if (gaia.getChosenTheme().equals("Black Theme")) {

			background.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.drawerbackground_black));

		}

		if (gaia.getChosenTheme().equals("Blue Theme")) {

			background.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.drawerbackground_blue));

		} else if (gaia.getChosenTheme().equals("Orange Theme")) {

			background.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.drawerbackground_orange));
		} else if (gaia.getChosenTheme().equals("Green Theme")) {

			background.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.drawerbackground_green));
		} else if (gaia.getChosenTheme().equals("Purple Theme")) {

			background.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.drawerbackground_purple));
		}

	}

	static class ViewHolder {
		private ImageView img;
		private TextView tv;
		private TextView tv2;
	}
}