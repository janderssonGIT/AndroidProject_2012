/**
 * AndroidTwitterActivty
 * 
 * This Activity handles the fetching and visualization of the Kulplex Twitter
 * Part of this code has also been used on the GU Master Program application
 * 
 * @author Alberto Vaccari
 * 
 */

package com.kulplex.gaia;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import twitter4j.Status;

import com.kulplex.gaia.handler.TwitterFetch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AndroidTwitterActivity extends Activity {
	/** Called when the activity is first created. */

	// number of tweets to show
	private int notweets = 15;
	GaiaApp gaia;
	RelativeLayout background;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.twitterlayout);

		gaia = ((GaiaApp) getApplicationContext());

		background = (RelativeLayout) findViewById(R.id.background);

		themeHandler();

		ImageView btnBack = (ImageView) findViewById(R.id.btnBack);

		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

				System.out.println("Finished MainActivity");
				Intent intent = new Intent(AndroidTwitterActivity.this,
						GaiaActivity.class);
				startActivity(intent);
			}
		});

		andtwitter("Kulplex");

	}

	/**
	 * 
	 * This method handles the fetching and setup of the Twitter Activty
	 * 
	 * @param twitter_id
	 *            The Twitter ID that needs to be fetched
	 */
	@SuppressWarnings("deprecation")
	private void andtwitter(String twitter_id) {
		TwitterFetch tf = new TwitterFetch();
		List<Status> status = tf.twitter_fetch(twitter_id);
		ArrayList<String[]> str_list = new ArrayList<String[]>();
		LayoutInflater inflater = getLayoutInflater();
		TableLayout tl = (TableLayout) findViewById(R.id.twitterTL);
		if (status.isEmpty()) {
			TableRow tr = (TableRow) inflater.inflate(R.layout.tablerow, tl,
					false);
			TextView tw2 = (TextView) inflater.inflate(R.layout.twtweettext,
					tr, false);
			tw2.setText("There are no tweets to show!");
		} else {
			for (Status st : status) {
				int i = 1;
				String[] str_arr = new String[3];
				String sts = st.getText();
				if (sts.startsWith("RT @")) {
					String[] str1 = sts.split(":", 2);
					String[] str2 = str1[0].split("RT ");
					str_arr[0] = str2[1];
					str_arr[1] = str1[1];
				} else {
					str_arr[0] = st.getUser().getName();
					str_arr[1] = st.getText();
				}

				Date dt = st.getCreatedAt();
				Date nw = new Date();
				String ufdate = user_friendly_date(nw.getTime(), dt.getTime());
				str_arr[2] = ufdate;
				str_list.add(str_arr);
				i++;
				if (i >= notweets)
					return;
			}
			int tweet_num;
			if (notweets <= status.size())
				tweet_num = notweets;
			else
				tweet_num = status.size();
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			for (int i = 0; i < tweet_num; i++) {
				TableRow tr1 = (TableRow) inflater.inflate(R.layout.tablerow,
						tl, false);
				TableRow tr2 = (TableRow) inflater.inflate(R.layout.tablerow,
						tl, false);
				TableRow tr3 = (TableRow) inflater.inflate(R.layout.tablerow,
						tl, false);
				TextView tw1 = (TextView) inflater.inflate(
						R.layout.twtweetauthor, tr1, false);
				TextView tw2 = (TextView) inflater.inflate(
						R.layout.twtweettext, tr2, false);
				TextView tw3 = (TextView) inflater.inflate(
						R.layout.twtweetdate, tr3, false);
				tw2.setMovementMethod(LinkMovementMethod.getInstance());
				tw1.setText(str_list.get(i)[0]);
				tw2.setText(str_list.get(i)[1]);
				tw3.setText(str_list.get(i)[2]);
				tr1.addView(tw1);
				tr2.addView(tw2);
				tr3.addView(tw3);
				lp.setMargins(10, 0, 10, 0);
				tl.addView(tr1, lp);
				tl.addView(tr2, lp);
				tl.addView(tr3, lp);
			}
		}
	}

	/**
	 * 
	 * This method converts the difference into a readable date
	 * 
	 * 
	 * @param tm1
	 *            the first time
	 * @param tm2
	 *            the second time
	 */
	private String user_friendly_date(long tm1, long tm2) {
		// difference in hours
		long diff = (tm1 - tm2) / (1000 * 60 * 60);
		if (diff < 24)
			return String.valueOf(diff) + " hours ago";
		else if (diff >= 24 && diff < 48)
			return "yesterday";
		else
			return String.valueOf(diff / 24) + " days ago";
	}

	/**
	 * 
	 * This method handles the theme
	 * 
	 */
	private void themeHandler() {

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

}
