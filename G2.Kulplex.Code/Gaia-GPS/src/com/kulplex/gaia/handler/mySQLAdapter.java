/**
 * mySQLAdapter
 * 
 * This class handles the querying of the online database
 * 
 * 
 * @author Alberto Vaccari, Henry Dang
 * 
 */

package com.kulplex.gaia.handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kulplex.gaia.GaiaApp;
import com.kulplex.gaia.SocialActivity;

import android.content.Context;
import android.net.ParseException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class mySQLAdapter {

	static public String id;
	static public String name = "";
	static public String status;
	static public Boolean isOnline;


	static public Boolean isConnected = false;


	public static boolean login(Context myContext, String name, String pwd,
			Boolean remember) {

		
		byte[] data;
		HttpPost httppost;
		StringBuffer buffer;
		HttpResponse response;
		HttpClient httpclient;
		InputStream inputStream;
		List<NameValuePair> nameValuePairs;

		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(
					"http://46.239.106.135:8080/GaiaKulplex/login.php");
			// Add your data
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("User", name.trim()));
			nameValuePairs.add(new BasicNameValuePair("Password", pwd.trim()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			response = httpclient.execute(httppost);
			inputStream = response.getEntity().getContent();

			data = new byte[256];

			buffer = new StringBuffer();
			int len = 0;
			while (-1 != (len = inputStream.read(data))) {
				buffer.append(new String(data, 0, len));
			}

			inputStream.close();
		}

		catch (Exception e) {
			Toast.makeText(myContext, "error" + e.toString(), Toast.LENGTH_LONG)
					.show();
			return false;
		}
		if (buffer.charAt(0) == 'Y') {
			isConnected = true;

				
				mySQLAdapter.name = name.trim();
				mySQLAdapter.id = getId();
				setStatus("Online");
				System.out.println("You're Online");
			
			return true;
		} else {

			return false;
		}

	}

	public static String getId(){
		
		final String url = "http://46.239.106.135:8080/GaiaKulplex/getid.php";
		  
        JSONArray jArray;
        String result = null;
        InputStream is = null;
        StringBuilder sb = null;
        HttpResponse response;
        HttpEntity entity;

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("User", mySQLAdapter.name ));
		
        // http post
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(url);

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);

            entity = response.getEntity();

            is = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection" + e.toString());
            System.exit(1);
        }

        // convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            result = sb.toString();

        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }


        // parsing data
        String id = "";
        try {
            jArray = new JSONArray(result);
            JSONObject json_data = null;
            
            for (int i = 0; i < jArray.length(); i++) {

                json_data = jArray.getJSONObject(i);
                id = json_data.getString("id");

                
               
            }
            
        } catch (JSONException e1) {
            Log.d("DB", "Not found");
            e1.printStackTrace();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        
		return id;
	}
	public static void logout() {

		setStatus("Offline");
		name = "";
		id = "";
		isConnected = false;
	}
	
	  public static ArrayList<String[]> getFriendList() {
		  
		  
		  ArrayList<String[]> f = new ArrayList<String[]>();
		  final String url = "http://46.239.106.135:8080/GaiaKulplex/friendlist.php";
		  
	        JSONArray jArray;
	        String result = null;
	        InputStream is = null;
	        StringBuilder sb = null;
	        HttpResponse response;
	        HttpEntity entity;

	        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("UserId", id ));
			
	        // http post
	        try {
	            HttpClient httpclient = new DefaultHttpClient();

	            HttpPost httppost = new HttpPost(url);

	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            response = httpclient.execute(httppost);

	            entity = response.getEntity();

	            is = entity.getContent();

	        } catch (Exception e) {
	            Log.e("log_tag", "Error in http connection" + e.toString());
	            System.exit(1);
	        }

	        // convert response to string
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	            sb = new StringBuilder();
	            sb.append(reader.readLine() + "\n");

	            String line;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	            is.close();
	            result = sb.toString();

	        } catch (Exception e) {
	            Log.e("log_tag", "Error converting result " + e.toString());
	        }


	        // parsing data
	        String idfriend;
	        String name;
	        String status;
	        try {
	            jArray = new JSONArray(result);
	            JSONObject json_data = null;

	            for (int i = 0; i < jArray.length(); i++) {

	                json_data = jArray.getJSONObject(i);
	                idfriend = json_data.getString("idfriend");
	                name = json_data.getString("name");
	                status = json_data.getString("status");

	                
	                f.add(new String[]{idfriend, name, status});
	            }
	            
	        } catch (JSONException e1) {
	            Log.d("DB", "Not found");
	        } catch (ParseException e1) {
	            e1.printStackTrace();
	        }
	        
	        
	        return f;
	    }

	public static boolean register(Context myContext, String name, String pwd) {

		byte[] data;
		HttpPost httppost;
		StringBuffer buffer;
		HttpResponse response;
		HttpClient httpclient;
		InputStream inputStream;
		List<NameValuePair> nameValuePairs;

		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(
					"http://46.239.106.135:8080/GaiaKulplex/register.php");
			// Add your data
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("User", name.trim()));
			nameValuePairs.add(new BasicNameValuePair("Password", pwd.trim()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			response = httpclient.execute(httppost);
			inputStream = response.getEntity().getContent();

			data = new byte[256];

			buffer = new StringBuffer();
			int len = 0;
			while (-1 != (len = inputStream.read(data))) {
				buffer.append(new String(data, 0, len));
			}

			inputStream.close();
		}

		catch (Exception e) {
			Toast.makeText(myContext, "error" + e.toString(), Toast.LENGTH_LONG)
					.show();
			return false;
		}


		if (buffer.charAt(0) == 'Y') {

			return true;
		} else {

			return false;
		}

	}
	public static boolean sendMsg(Context myContext, String idRecipient, String msg) {
		
        
		byte[] data;
		HttpPost httppost;
		StringBuffer buffer;
		HttpResponse response;
		HttpClient httpclient;
		InputStream inputStream;
		List<NameValuePair> nameValuePairs;

		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(
					"http://46.239.106.135:8080/GaiaKulplex/sendmsg.php");
			// Add your data
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("User1", id));
			nameValuePairs.add(new BasicNameValuePair("User2", idRecipient));
			nameValuePairs.add(new BasicNameValuePair("Text", msg));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			response = httpclient.execute(httppost);
			inputStream = response.getEntity().getContent();

			data = new byte[256];

			buffer = new StringBuffer();
			int len = 0;
			while (-1 != (len = inputStream.read(data))) {
				buffer.append(new String(data, 0, len));
			}

			inputStream.close();
		}

		catch (Exception e) {

			return false;
		}



		if (buffer.charAt(0) == 'Y') {

			return true;
		} else {

			return false;
		}

		
	}
	
	
	
	 public static ArrayList<String[]> getMessageList(String idRecipient) {
		  
		  
		  ArrayList<String[]> m = new ArrayList<String[]>();
		  final String url = "http://46.239.106.135:8080/GaiaKulplex/getmsg.php";
		  
	        JSONArray jArray;
	        String result = null;
	        InputStream is = null;
	        StringBuilder sb = null;
	        HttpResponse response;
	        HttpEntity entity;

	        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("User1", id ));
			nameValuePairs.add(new BasicNameValuePair("User2", idRecipient ));
			
	        // http post
	        try {
	            HttpClient httpclient = new DefaultHttpClient();

	            HttpPost httppost = new HttpPost(url);

	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            response = httpclient.execute(httppost);

	            entity = response.getEntity();

	            is = entity.getContent();

	        } catch (Exception e) {
	            Log.e("log_tag", "Error in http connection" + e.toString());
	            System.exit(1);
	        }

	        // convert response to string
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	            sb = new StringBuilder();
	            sb.append(reader.readLine() + "\n");

	            String line;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	            is.close();
	            result = sb.toString();

	        } catch (Exception e) {
	            Log.e("log_tag", "Error converting result " + e.toString());
	        }


	        // parsing data
	        String text;
	        String date;
	        String sender;
	        String recipient;
	        try {
	            jArray = new JSONArray(result);
	            JSONObject json_data = null;

	            for (int i = 0; i < jArray.length(); i++) {

	                json_data = jArray.getJSONObject(i);
	                text = json_data.getString("text");
	                date = json_data.getString("date");
	                sender = json_data.getString("idSender");
	                recipient = json_data.getString("idRecipient");

	                
	                m.add(new String[]{text, date, sender, recipient});
	            }
	            
	        } catch (JSONException e1) {
	            Log.d("DB", "Not found");
	        } catch (ParseException e1) {
	            e1.printStackTrace();
	        }
	        
	        
	        return m;
	    }
	 
		public static boolean setStatus(String status) {
			
	        
			byte[] data;
			HttpPost httppost;
			StringBuffer buffer;
			HttpResponse response;
			HttpClient httpclient;
			InputStream inputStream;
			List<NameValuePair> nameValuePairs;

			try {
				httpclient = new DefaultHttpClient();
				httppost = new HttpPost(
						"http://46.239.106.135:8080/GaiaKulplex/setstatus.php");
				// Add your data
				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("User", id));
				nameValuePairs.add(new BasicNameValuePair("Status", status));
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				response = httpclient.execute(httppost);
				inputStream = response.getEntity().getContent();

				data = new byte[256];

				buffer = new StringBuffer();
				int len = 0;
				while (-1 != (len = inputStream.read(data))) {
					buffer.append(new String(data, 0, len));
				}

				inputStream.close();
			}

			catch (Exception e) {
				
				return false;
			}


			if (buffer.charAt(0) == 'Y') {
				
				return true;
				
			} else {

				return false;
			}

			
		}
		
		public static ArrayList<String> getSearchList(String search) {
			  
			  
			  ArrayList<String> f = new ArrayList<String>();
			  final String url = "http://46.239.106.135:8080/GaiaKulplex/search.php";
			  
		        JSONArray jArray;
		        String result = null;
		        InputStream is = null;
		        StringBuilder sb = null;
		        HttpResponse response;
		        HttpEntity entity;

		        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("Search", search ));
				
		        // http post
		        try {
		            HttpClient httpclient = new DefaultHttpClient();

		            HttpPost httppost = new HttpPost(url);

		            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		            response = httpclient.execute(httppost);

		            entity = response.getEntity();

		            is = entity.getContent();

		        } catch (Exception e) {
		            Log.e("log_tag", "Error in http connection" + e.toString());
		            System.exit(1);
		        }

		        // convert response to string
		        try {
		            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
		            sb = new StringBuilder();
		            sb.append(reader.readLine() + "\n");

		            String line;
		            while ((line = reader.readLine()) != null) {
		                sb.append(line);
		            }
		            is.close();
		            result = sb.toString();

		        } catch (Exception e) {
		            Log.e("log_tag", "Error converting result " + e.toString());
		        }


		        // parsing data
		        String name;
		        String status;
		        try {
		            jArray = new JSONArray(result);
		            JSONObject json_data = null;

		            for (int i = 0; i < jArray.length(); i++) {

		                json_data = jArray.getJSONObject(i);
		                name = json_data.getString("name");

		                
		                f.add(name);
		            }
		            
		        } catch (JSONException e1) {
		            Log.d("DB", "Not found");
		        } catch (ParseException e1) {
		            e1.printStackTrace();
		        }
		        
		        
		        return f;
		    }
		
		public static boolean addFriend(Context myContext, String name) {

			byte[] data;
			HttpPost httppost;
			StringBuffer buffer;
			HttpResponse response;
			HttpClient httpclient;
			InputStream inputStream;
			List<NameValuePair> nameValuePairs;

			try {
				httpclient = new DefaultHttpClient();
				httppost = new HttpPost(
						"http://46.239.106.135:8080/GaiaKulplex/addfriend.php");
				
				// Add your data
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("IdUser", id));
				nameValuePairs.add(new BasicNameValuePair("Friend", name.trim()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				response = httpclient.execute(httppost);
				inputStream = response.getEntity().getContent();

				data = new byte[256];

				buffer = new StringBuffer();
				int len = 0;
				while (-1 != (len = inputStream.read(data))) {
					buffer.append(new String(data, 0, len));
				}

				inputStream.close();
			}

			catch (Exception e) {
				Toast.makeText(myContext, "error" + e.toString(), Toast.LENGTH_LONG)
						.show();
				return false;
			}

			String res = "";
			for(int i = 0; i < buffer.length(); i++)
			{
				res += buffer.charAt(i);
			}
			
			System.out.println(res);
			
			
			if (buffer.charAt(0) == 'Y') {

				return true;
			} else {

				return false;
			}

		}
		

}


