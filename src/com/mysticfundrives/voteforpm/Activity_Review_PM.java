package com.mysticfundrives.voteforpm;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Review_PM extends Activity {

	public ArrayList<String> arr_Name = new ArrayList<String>();
	public ArrayList<String> arr_Date = new ArrayList<String>();
	public ArrayList<String> arr_Review = new ArrayList<String>();

	private ImageView img_Pm;

	private TextView txt_PM_Name;
	private TextView txt_PM_Prty;

	public Integer int_Images;
	public String str_Names;
	public String str_Partys;
	public int postions;

	private ListView list_Review;

	String response;
	Post result;
	String link = "http://tp14.pwh-r1.com/~wwwvikra/voteforpm/VoteForPMJSON.php?";
	public ProgressDialog dialog;
	 AlertDialog dialognew;
	 
	 public String[] arr_Order = {"Older","Newest"};
	 public Spinner spnr_Ordr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_review_pm);

		AdView adView = (AdView)this.findViewById(R.id.adViewsnews);
		adView.loadAd(new AdRequest());
		
		boolean online_Status = isOnline();
		if (online_Status == true) {
			result = new Post(link);

			img_Pm = (ImageView) findViewById(R.id.img_PM_Imagess);

			txt_PM_Name = (TextView) findViewById(R.id.txt_name_PMs);
			txt_PM_Prty = (TextView) findViewById(R.id.txt_partys);

			list_Review = (ListView) findViewById(R.id.lv_Reviews);
			
			spnr_Ordr = (Spinner)	findViewById(R.id.spnr_For_List_Order);
			spnr_Ordr.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Collections.reverse(arr_Date);
					Collections.reverse(arr_Name);
					Collections.reverse(arr_Review);

					list_Review.setAdapter(new TextAdapterNew(getApplicationContext()));
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
			ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,arr_Order);
			spnr_Ordr.setAdapter(adp);

			int_Images = getIntent().getIntExtra("Key_Image", 0);
			str_Names = getIntent().getStringExtra("Key_Name");
			str_Partys = getIntent().getStringExtra("Key_Party");
			postions = getIntent().getIntExtra("Key_Pos", 0);
			Log.e("Actvity_PM_Detail", ""+postions);

			img_Pm.setBackgroundResource(int_Images);
			txt_PM_Name.setText(str_Names);
			txt_PM_Prty.setText(str_Partys);
			list_Review.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Review_PM.this);
					
					try {
						builder.setTitle(""+URLDecoder.decode(arr_Name.get(arg2), "UTF-8")+" Review");
						builder.setMessage(""+URLDecoder.decode(arr_Review.get(arg2), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dialognew = builder.create();
					dialognew.getWindow().getAttributes().windowAnimations =  R.style.dialog_animation;

					dialognew.show();

				}
			});
		

		} else {
			Toast.makeText(getApplicationContext(), "Internet Is Not Avable",
					Toast.LENGTH_LONG).show();
		}

		

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		arr_Name.clear();
		arr_Review.clear();
		arr_Date.clear();
		new getResponse().execute();
	}

	class getResponse extends AsyncTask<Void, Void, Void> {
		JSONArray jarr;

		@Override
		protected Void doInBackground(Void... params) {

			List<BasicNameValuePair> AV = new ArrayList<BasicNameValuePair>();
			// hit the main tag in the response "ambulance_reg"
			AV.add(new BasicNameValuePair("act", "Select_Review_ForPm"));
			// now enter the values in the tags so that values can be sent to
			// the given link
			AV.add(new BasicNameValuePair("mob_Review", String
					.valueOf(postions)));
			try {
				response = result.calLoginService(AV);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {

				JSONObject jobj = new JSONObject(response);
				// main_tag is the main tag at url which you hit fist and get
				// the inner values
				Object obj = jobj.get("InforeviewPm");
				if (obj instanceof JSONArray) {
					// this executes if the json is an array
					jarr = (JSONArray) obj;
					for (int i = 0; i < jarr.length(); i++) {
						JSONObject jobjnew = jarr.getJSONObject(i);
						// hit the inner tag in the main tag and get the value
						// from it
						arr_Name.add(jobjnew.getString("User_Name"));
						arr_Date.add(jobjnew.getString("Vote_DateAndTime"));
						arr_Review.add(jobjnew.getString("User_Suggestion"));
					}

				} else {
					// this executes if the json is an object
					JSONObject newjobj = (JSONObject) obj;
					arr_Name.add(newjobj.getString("User_Name"));
					arr_Date.add(newjobj.getString("Vote_DateAndTime"));
					arr_Review.add(newjobj.getString("User_Suggestion"));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}


	
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(Activity_Review_PM.this);
			dialog.setMessage("Loading please wait");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			Log.e("response", "is::  " + response);
			list_Review.setAdapter(new TextAdapterNew(getApplicationContext()));

			dialog.dismiss();

			
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	class TextAdapterNew extends BaseAdapter {
		private Context mContext;
		public LayoutInflater inflater;

		public TextAdapterNew(Context c) {
			mContext = c;
			inflater = LayoutInflater.from(c);
		}

		@Override
		public int getCount() {
			return arr_Name.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TextView name, date, rivew;
		
			View v;

			if (convertView == null) { // if it's not recycled, initialize some
				// attributes
				inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.list_review, null);
			} else {
				v = convertView;
			}
			name = (TextView) v.findViewById(R.id.txt_Username);
			date = (TextView) v.findViewById(R.id.txt_DateAdTime);
			rivew = (TextView) v.findViewById(R.id.txt_Review);
		
			try {
				name.setText(URLDecoder.decode(arr_Name.get(position), "UTF-8"));
				rivew.setText(URLDecoder.decode(arr_Review.get(position), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			date.setText(arr_Date.get(position));
			

			return v;
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}

}
