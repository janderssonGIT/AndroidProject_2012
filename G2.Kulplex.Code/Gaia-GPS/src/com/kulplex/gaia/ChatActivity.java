/**
 * ChatActivity
 * 
 * This Activity handles chat between two users.
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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity {

	static public Handler mHandler;

	public GaiaApp gaia;

	static public String FriendName;
	static public String FriendId;

	LinearLayout background;

	static public ListView lv;

	static public ListViewAdapter adapter;
	static public List<Map<String, Object>> list;

	static ArrayList<String[]> msgList;
	static int msgCount = 0;

	static Boolean getMsg = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatlayout);
		msgCount = 0;
		Bundle extras = getIntent().getExtras();
		FriendName = extras.getString("FriendName");
		FriendId = extras.getString("FriendId");

		getMsg = true;

		lv = (ListView) findViewById(R.id.list);
		gaia = ((GaiaApp) getApplicationContext());
		mHandler = new Handler();

		background = (LinearLayout) findViewById(R.id.background);

		loadMsg();

		ImageView BackBtn = (ImageView) findViewById(R.id.BackBtn);

		BackBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				getMsg = false;
				finish();
				Intent intent = new Intent(ChatActivity.this,
						FriendListActivity.class);
				startActivity(intent);
			}
		});

		themeHandler();

		ImageView sendBtn = (ImageView) findViewById(R.id.send);

		final EditText textMsg = (EditText) findViewById(R.id.textMsg);

		sendBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (mySQLAdapter.sendMsg(getBaseContext(), FriendId, textMsg
						.getText().toString())) {
					Toast.makeText(getBaseContext(), "Sent", Toast.LENGTH_SHORT)
							.show();
					textMsg.setText("");
				} else

					Toast.makeText(getBaseContext(), "Error",
							Toast.LENGTH_SHORT).show();

			}
		});

	}

	/**
	 * 
	 * Loads Messages from Server
	 * 
	 */
	private void loadMsg() {

		ChatActivity.msgList = new ArrayList<String[]>();
		LoadTask t = new LoadTask(this);
		t.execute();

	}

	/**
	 * 
	 * Composes the list before being shown
	 * 
	 */
	private static List<Map<String, Object>> buildList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < msgList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			if(msgList.get(i)[2].equals(mySQLAdapter.id))
			{
				map.put("sender", mySQLAdapter.name);
			}
			else
				map.put("sender", FriendName);
			
			

			map.put("text", msgList.get(i)[0]);
			map.put("date", msgList.get(i)[1]);
			list.add(map);
		}
		return list;
	}

	/**
	 * 
	 * Handles the fetching from the Server
	 * 
	 */
	private static class LoadTask extends AsyncTask<Void, Void, Void> {

		Context mContext;

		protected void onPreExecute() {

		}

		public LoadTask(Context context) {

			mContext = context;
		}

		protected Void doInBackground(Void... params) {

			while (getMsg) {

				if (mySQLAdapter.getMessageList(ChatActivity.FriendId).size() > ChatActivity.msgCount) {
					ChatActivity.msgList = mySQLAdapter
							.getMessageList(ChatActivity.FriendId);

					ChatActivity.msgCount = ChatActivity.msgList.size();

					mHandler.post(new Runnable() {
						@Override
						public void run() {

							list = buildList();

							adapter = new ListViewAdapter(mContext, list);

							lv.setAdapter(adapter);

							lv.setSelection(ChatActivity.msgCount - 1);
						}

					});
				}
			}

			return null;
		}
	}

	/**
	 * 
	 * This method handles the theme
	 * 
	 */
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

	public static class ListViewAdapter extends BaseAdapter {
		private List<Map<String, Object>> list;
		private LayoutInflater inflater;

		public ListViewAdapter(Context context, List<Map<String, Object>> list) {
			super();
			this.list = list;
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
				convertView = inflater.inflate(R.layout.chat_item, null);
				viewHolder = new ViewHolder();

				viewHolder.sender = (TextView) convertView
						.findViewById(R.id.sender);
				viewHolder.text = (TextView) convertView
						.findViewById(R.id.text);
				viewHolder.date = (TextView) convertView
						.findViewById(R.id.date);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.sender.setText((String) map.get("sender"));
			viewHolder.text.setText((String) map.get("text"));
			viewHolder.date.setText((String) map.get("date"));

			return convertView;
		}
	}

	static class ViewHolder {
		private TextView sender;
		private TextView text;
		private TextView date;
	}
}
