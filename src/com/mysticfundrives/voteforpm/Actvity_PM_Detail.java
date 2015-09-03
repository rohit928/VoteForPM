package com.mysticfundrives.voteforpm;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Actvity_PM_Detail extends Activity{

	private ImageView img_Pm;

	private TextView txt_PM_Name;
	private TextView txt_PM_Prty;
	private TextView txt_PM_Info;
	private TextView txt_Active_Your_Vote;


	private EditText edt_Name;
	private EditText edt_PM_Suggetion;
	private Button btn_Submit_Vote;
	private Button btn_mailUs;
	private Button btn_reviews_Vote;

	public Integer int_Image;
	public String str_Name;
	public String str_Party;
	public int postion;

	public String get_User_Suggestion;

	public String[] arr_Info = {""};

	public TelephonyManager tm;
	public String dev_id;

	public int get_Postion = 10;

	public String get_Email;

	String response;
	Post result;
	SharedPreferences prefs;
	String link = "http://tp14.pwh-r1.com/~wwwvikra/voteforpm/VoteForPMJSON.php?";

	String userUDID;
	String userSuggestion;
	String userEmaiId;
	String get_FullStory;
	String get_Suggestion = "";
	int getIndex;


	public String get_User_Suggestionnew;
	public String get_User_Name;
	public String get_Vote_Id;
	public String get_Vote_DateAndTime;

	String dn_get_User_Suggestionnew;
	String dn_get_User_Name;

	ProgressDialog dialog;
	
	public String emailContent;
	
	public String[] email = { "mysticfundrives@gmail.com" };
	
	public String[] pm_Info = {"Rahul Gandhi is currently the General Secretary of Congress party. In 21st century, according to Rahul Gandhi leadership should go into the hands of youth. This will lead to a government of the youth that will come to power in 2014 which will work for the poor and change the country. It will bring change by empowering the last man in the queue. It will be really interesting to see if he is able to prove the trump card for Congress for the Next Prime Minister of India.",
			"Narendra Modi has been endorsed as a candidate for the country's top job - the Next Prime Minister of India. Modi has PM material. People of the country today want development and good governance and he has proved these (in Gujarat). Modi has the power to thinks about how to do things that are not there anywhere. Narendra Modi shines for his impeccable integrity. He has focused his entire energy on building in Gujarat an able administration and good governance.",
			"Sushma Swaraj is the leader of her party in the Lok Sabha. She has the capabilities of becoming PM as speaks well and is not in any controversy. Her party always speaks for India's culture and civilization. She says - For running a coalition government, you need to take along all your partners. She is in Dewas to take part in the district planning committee meeting of her party.",
			"The world will love Mayawati as PM. Mayawati is actually the most presentable candidate by far. If she becomes PM, India can claim to be the most empowering democracy in the world. If Mayawati becomes prime minister, she will become a beacon of hope for oppressed people across the world. Many middle class Indians want a prime minister who is honest, principled and erudite, who can debate intellectual issues with the best in the world.",
			"Nitish Kumar is capable of holding the prime minister's chair. He has brought Bihar in focus not only in India but globally. He has redefined good governance in a state that was for decades believed to have intractable underdevelopment, crime and corruption for decades. His performance has earned him praise not only from his supporters but also from his political opponents. He has set example of how to run a coalition government.",
			"Dr Manmohan Singh is the Prime Minister of India, since 2004, is one of the most honored Statesman in the globe. He is the highest qualified Prime Minister in the world. He is known for his integrity, honesty, knowledge and intelligence in economic and financial matters. He showed his capabilities asFinance Minister of India and Prime Minister of India. He is the person who became instrumental to liberalization in Indian economy as Finance Minister.He has proven his quality as student, as teacher, as author, as civil servant and also as parliamentarian before adorning the prime post. He is the first non political person to qualify as prime ministerial candidate in India.",
			"Aam Aadmi Party (AAP) leader Arvind Kejriwal is another big name for the Prime Minister's post. According to him stage has now come where the people have lost faith in almost all the political parties and that is why there is coalition. There is a serious leadership crisis in this country. There is no strong and credible leader on the horizon. From an activist to a full-fledged politician, he has had a rollercoaster journey, and he is still fighting for political credibility. One of the major reasons why Arvind can be successful is that he is a very effective speaker and a fantastic organizer.",
			"If you are dissatisfied with the candidate of your area, then voice your disapproval by choosing this option. Though there is no such button (right not to vote) on an Electronic Voting Machine (EVM), there is a provision in the law to ask for Form 49-O at polling booths. One can fill this form for the right to not to vote. "};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.actvity_pm_detail);
		
		boolean getOnline_Status = isOnline();
		
		if (getOnline_Status == true) {
			result = new Post(link);
			

			get_Email = GetEmail.getEmail(getApplicationContext());

			tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			dev_id = tm.getDeviceId();
			Log.e("UDID", ""+dev_id);
			new getResponseForUdid().execute();
			getIndex    =   getIntent().getIntExtra("key_IndexStatus", 0);

			int_Image	=	getIntent().getIntExtra("key_Image", 0);
			str_Name	=	getIntent().getStringExtra("key_Name");
			str_Party	=	getIntent().getStringExtra("key_Party");
			postion		=	getIntent().getIntExtra("key_Postion", 0);

			if (getIndex == 1) {
				get_Suggestion	=	getIntent().getStringExtra("key_Suggestion");
				get_Postion 	= 	getIntent().getIntExtra("key_VoteId_Postion", 0);
			}

			img_Pm		=	(ImageView)	findViewById(R.id.img_PM_Image);

			txt_PM_Name	=	(TextView)	findViewById(R.id.txt_name_PM);
			txt_PM_Prty	=	(TextView)	findViewById(R.id.txt_party);
			txt_PM_Info	=	(TextView)	findViewById(R.id.txt_Info);
			txt_Active_Your_Vote = (TextView)	findViewById(R.id.txt_Active_Your_Vote);
			txt_PM_Info.setText(pm_Info[postion]);

			edt_PM_Suggetion = (EditText)	findViewById(R.id.edt_Suggestion);
			edt_Name = (EditText)	findViewById(R.id.edt_Name);

			btn_Submit_Vote  = (Button)		findViewById(R.id.btn_Active_Your_Vote);
			btn_mailUs		 = (Button)		findViewById(R.id.btn_Mail_Active_Your_Vote);
			btn_reviews_Vote = (Button)		findViewById(R.id.btn_Review_Your_Vote);
			
			btn_reviews_Vote.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent int_For_Reviews_Pm = new Intent(getApplicationContext(),Activity_Review_PM.class);
					int_For_Reviews_Pm.putExtra("Key_Image", int_Image);
					int_For_Reviews_Pm.putExtra("Key_Name", str_Name);
					int_For_Reviews_Pm.putExtra("Key_Party", str_Party);
					int_For_Reviews_Pm.putExtra("Key_Pos", postion);
					Log.e("Actvity_PM_Detail", ""+postion);
					startActivity(int_For_Reviews_Pm);
				}
			});
			

			img_Pm.setBackgroundResource(int_Image);
			txt_PM_Name.setText(str_Name);
			txt_PM_Prty.setText(str_Party);
			
			emailContent = "Request For Vote Again"+"\n\n"+"Please accpet my reuest for vote again for PM"+"\n"+"My Request Number is : "+dev_id+" Vote For Again Vote "+"\n\n"+"Keep using "+""+ "Mysticfundrives "+""+"on play store and "+""+"learn with fun "
			+" \n\n"+"https://play.google.com/store/apps/developer?id=MysticFunDrives"; 

		}else {
			Toast.makeText(getApplicationContext(), "Internet is not avalibe", Toast.LENGTH_LONG).show();
			finish();
		}
		


	}


	/////////////////////////////////////////////
	class getResponseForUdid extends AsyncTask<Void, Void, Void> {


		@Override
		protected Void doInBackground(Void... params) {

			List<BasicNameValuePair> AV = new ArrayList<BasicNameValuePair>();
			// hit the main tag in the response "ambulance_reg"
			AV.add(new BasicNameValuePair("act", "Chk_UDID_Details"));
			// now enter the values in the tags so that values can be sent to
			// the given link

			Log.e("UDID ::", ""+dev_id);

			AV.add(new BasicNameValuePair("mob_UDID", dev_id));

			try {
				for (int i = 0; i < AV.size(); i++) {
					Log.e("Check", "" + AV.get(i));
				}
				response = result.calLoginService(AV);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(Actvity_PM_Detail.this);
			dialog.setMessage("Loading please wait");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.e("response", "is::  " + response);

			JSONObject jobjnew;
			get_FullStory ="";
			try {
				jobjnew = new JSONObject(response);
				get_FullStory = jobjnew.getString("id");

			} catch (JSONException e1) {

			}

			if (get_FullStory.equals("No User")) {
				dialog.dismiss();
				btn_Submit_Vote.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String get_UserNae = edt_Name.getText().toString();
						String get_UserSuggestion = edt_PM_Suggetion.getText().toString();
						edt_PM_Suggetion.setFocusable(true);
						edt_Name.setFocusable(true);
						if ((!get_UserNae.equals("")) && (!get_UserSuggestion.equals(""))) {
							try {
								dn_get_User_Suggestionnew = URLEncoder.encode(get_UserSuggestion, "UTF-8");
								dn_get_User_Name = URLEncoder.encode(get_UserNae, "UTF-8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							
							btn_Submit_Vote.setBackgroundResource(R.drawable.butonon);
							new getResponse().execute();
						}else {
							Toast.makeText(getApplicationContext(), "Please Fill your details", Toast.LENGTH_LONG).show();
						}
					}
				});
				
			}else {
				dialog.dismiss();
				JSONObject jobj;
				try {
					jobj = new JSONObject(response);
					JSONObject nobj = jobj.getJSONObject("personaldetails");

					get_User_Suggestionnew = nobj.optString("User_Suggestion");
					get_User_Name = nobj.optString("User_Name");
					get_Vote_Id = nobj.optString("Vote_Id");
					get_Vote_DateAndTime = nobj.optString("Vote_DateAndTime");
					btn_mailUs.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							 shareToGMail(email, "Request Vote Again", emailContent);
						}
					});
					edt_PM_Suggetion.setFocusable(false);
					edt_Name.setFocusable(false);
					txt_Active_Your_Vote.setText("Your Vote Already Activated");
					
					if (postion == Integer.parseInt(get_Vote_Id)) {
						btn_Submit_Vote.setBackgroundResource(R.drawable.butonon);
						

						String en_get_User_Suggestionnew = URLDecoder.decode(get_User_Suggestionnew, "UTF-8");
						String en_get_User_Name = URLDecoder.decode(get_User_Name, "UTF-8");

						edt_PM_Suggetion.setText(en_get_User_Suggestionnew);
						edt_Name.setText(en_get_User_Name);
						edt_PM_Suggetion.setFocusable(false);
						edt_Name.setFocusable(false);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

		}
	}
	/////////////////////////////////////////////



	class getResponse extends AsyncTask<Void, Void, Void> {


		@Override
		protected Void doInBackground(Void... params) {

			List<BasicNameValuePair> AV = new ArrayList<BasicNameValuePair>();
			// hit the main tag in the response "ambulance_reg"
			AV.add(new BasicNameValuePair("act", "Insert_VoteDetails"));
			// now enter the values in the tags so that values can be sent to
			// the given link



			AV.add(new BasicNameValuePair("userUDID", dev_id));
			AV.add(new BasicNameValuePair("mob_Suggestion", dn_get_User_Suggestionnew));
			AV.add(new BasicNameValuePair("mob_Name", dn_get_User_Name));
			AV.add(new BasicNameValuePair("mob_EmaiId", get_Email));
			AV.add(new BasicNameValuePair("mob_VoteId", String.valueOf(postion)));
			try {
				for (int i = 0; i < AV.size(); i++) {
					Log.e("Check", "" + AV.get(i));
				}
				response = result.calLoginService(AV);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(Actvity_PM_Detail.this);
			dialog.setMessage("Loading please wait");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.e("response", "is::  " + response);

			JSONObject jobjnew;
			try {
				jobjnew = new JSONObject(response);
				get_FullStory = jobjnew.getString("submit_story");
				String congo = "Congratulations";
				show_Toast(congo);
				dialog.dismiss();
			} catch (JSONException e1) {

			}
		}
	}

	public void show_Toast(String get_ToastString) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast,
				(ViewGroup) findViewById(R.id.toast_layout_root));

		ImageView image = (ImageView) layout.findViewById(R.id.image);
		image.setImageResource(R.drawable.like);
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(get_ToastString);

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
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
	
	public void shareToGMail(String[] email, String subject, String content) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
		final PackageManager pm = getPackageManager();
		final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent,
		0);
		ResolveInfo best = null;
		for (final ResolveInfo info : matches)
		if (info.activityInfo.packageName.endsWith(".gm")
		|| info.activityInfo.name.toLowerCase().contains("gmail"))
		best = info;
		if (best != null)
		emailIntent.setClassName(best.activityInfo.packageName,
		best.activityInfo.name);
		startActivity(emailIntent);
		}



}
