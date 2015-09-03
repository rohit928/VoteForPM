package com.mysticfundrives.voteforpm;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
	String[] array = { "Rahul Gandi", "Narendra Modi", "Sushma Swaraj",
			"Mayawati", "Nitish Kumar", "Manmohan Singh", "Arvind Kejriwal","None Of This"};
	int[] images = { R.drawable.rahul, R.drawable.modi, R.drawable.sushma,
			R.drawable.mayawati, R.drawable.nitish, R.drawable.manmohan_singh, R.drawable.arvind_kejriwal, R.drawable.wrong};
	String[] parties = { "Congress", "BJP", "NDA", "BSP", "BJP", "Congress", "Aam Aadmi Party","No Party Name"};

	public String get_id = "";
	public String get_Udid = "";
	public String get_Suggestion = "";
	public String get_Email = "";
	public String get_Name = "";
	public String get_voteId = "";
	public String get_DateandTime = "";
	public int get_Index;

	ProgressDialog dialog;
	
	public int get_VoteId_Postion;

	Get get;
	String response;
	String link = "http://tp14.pwh-r1.com/~wwwvikra/voteforpm/VoteForPMJSON.php?act=SelectAll_VoteDetails";

	ArrayList<String> list = new ArrayList<String>();
	ArrayList<Integer> listForVotes = new ArrayList<Integer>();

	ListView list1;

	public Integer zero;
	public Integer one;
	public Integer two;
	public Integer three;
	public Integer four;
	public Integer five;
	public Integer six;
	public Integer seven;
	
	public Integer temp = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		AdView adView = (AdView)this.findViewById(R.id.adViewsnew);
		adView.loadAd(new AdRequest());

		boolean online_Status = isOnline();
		if (online_Status == true) {
			
			

			get_Index = getIntent().getIntExtra("Key_Index", 0);

			if (get_Index == 0) {
				get_VoteId_Postion = 10;
			} else {
				get_id = getIntent().getStringExtra("key_get_id");
				get_Udid = getIntent().getStringExtra("key_get_User_UDID");
				get_Suggestion = getIntent().getStringExtra(
						"key_get_User_Suggestion");
				get_Email = getIntent().getStringExtra("key_get_User_EmailId");
				get_Name = getIntent().getStringExtra("key_get_User_Name");
				get_voteId = getIntent().getStringExtra("key_get_Vote_Id");
				get_DateandTime = getIntent().getStringExtra(
						"key_get_Vote_DateAndTime");
				get_VoteId_Postion = Integer.parseInt(get_voteId);
			}

		
			
			Log.e("", ""+listForVotes.size());
			list1 = (ListView) findViewById(R.id.lv1);
			
		    list1.setOnItemClickListener(this);
		} else {
			Toast.makeText(getApplicationContext(), "Internet Is Not Avable",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		boolean online_Status = isOnline();
		if (online_Status == true) {

			zero = null;
			one = null;
			two = null;
			three = null;
			four = null;
			five = null;
			six = null;
			seven = null;
			list.clear();
			listForVotes.clear();
			new getR().execute();
		}
	}

	class getR extends AsyncTask<Void, Void, Void> {
		JSONArray jarr;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Loading please wait");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			get = new Get(link);
			try {
				response = get.calLoginService();
				JSONObject jobj = new JSONObject(response);
				// main_tag is the main tag at url which you hit fist and get
				// the inner values
				Object obj = jobj.get("personaldetails");
				if (obj instanceof JSONArray) {
					// this executes if the json is an array
					jarr = (JSONArray) obj;

					for (int i = 0; i < jarr.length(); i++) {
						JSONObject jobjnew = jarr.getJSONObject(i);
						// hit the inner tag in the main tag and get the value
						// from it

						list.add(jobjnew.getString("Vote_Id"));
					}

				} else {
					// this executes if the json is an object

					JSONObject newjobj = (JSONObject) obj;
					list.add(newjobj.optString("Vote_Id"));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		

		@Override
		protected void onPostExecute(Void result) {
			if (response.equals("success")) {
				Toast.makeText(getApplicationContext(), "Success",
						Toast.LENGTH_LONG).show();
			}

			zero = Collections.frequency(list, "0");
			one = Collections.frequency(list, "1");
			two = Collections.frequency(list, "2");
			three = Collections.frequency(list, "3");
			four = Collections.frequency(list, "4");
			five = Collections.frequency(list, "5");
			six = Collections.frequency(list, "6");
			seven = Collections.frequency(list, "7");

			listForVotes.add(zero);
			listForVotes.add(one);
			listForVotes.add(two);
			listForVotes.add(three);
			listForVotes.add(four);
			listForVotes.add(five);
			listForVotes.add(six);
			listForVotes.add(seven);

			list1.setAdapter(new TextAdapter1(getApplicationContext()));
			dialog.dismiss();
			super.onPostExecute(result);
		}
	}

	class TextAdapter1 extends BaseAdapter {
		private Context mContext;
		public LayoutInflater inflater;

		public TextAdapter1(Context c) {
			mContext = c;
			inflater = LayoutInflater.from(c);
		}

		@Override
		public int getCount() {
			return array.length;
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

			TextView name, party, TotalVotes;
			ImageView photo;
			View v;

			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.list_row, null);
			} else {
				v = convertView;
			}
			photo = (ImageView) v.findViewById(R.id.image);
			name = (TextView) v.findViewById(R.id.name);
			party = (TextView) v.findViewById(R.id.party);
			TotalVotes = (TextView) v.findViewById(R.id.totalsvotes);
			TotalVotes.setText("Votes : " + listForVotes.get(position));

			name.setText(array[position]);
			if (position != get_VoteId_Postion) {
				photo.setImageResource(R.drawable.frame);
			} else {
				photo.setImageResource(R.drawable.greenframe);
			}
			photo.setBackgroundResource(images[position]);
			party.setText(parties[position]);

			return v;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		boolean online_Status = isOnline();
		if (online_Status == true) {
			if (get_Index == 0) {
				Intent int_For_PM_Detail = new Intent(getApplicationContext(),
						Actvity_PM_Detail.class);
				int_For_PM_Detail.putExtra("key_IndexStatus", get_Index);
				int_For_PM_Detail.putExtra("key_Image", images[arg2]);
				int_For_PM_Detail.putExtra("key_Name", array[arg2]);
				int_For_PM_Detail.putExtra("key_Party", parties[arg2]);
				int_For_PM_Detail.putExtra("key_Postion", arg2);
				startActivity(int_For_PM_Detail);
			} else {
				Intent int_For_PM_Detail = new Intent(getApplicationContext(),
						Actvity_PM_Detail.class);
				int_For_PM_Detail.putExtra("key_IndexStatus", get_Index);
				int_For_PM_Detail.putExtra("key_Image", images[arg2]);
				int_For_PM_Detail.putExtra("key_Name", array[arg2]);
				int_For_PM_Detail.putExtra("key_Party", parties[arg2]);
				int_For_PM_Detail.putExtra("key_Suggestion", get_Suggestion);
				int_For_PM_Detail.putExtra("key_VoteId_Postion",
						get_VoteId_Postion);
				int_For_PM_Detail.putExtra("key_Postion", arg2);

				startActivity(int_For_PM_Detail);
			}

		} else {
			Toast.makeText(getApplicationContext(),
					"No Internet Access Avalibe", Toast.LENGTH_LONG).show();
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
