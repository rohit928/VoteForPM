package com.mysticfundrives.voteforpm;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashScreen extends Activity {
	private boolean mIsBackButtonPressed;
	/** Called when the activity is first created. */
	public TelephonyManager tm;
	public String dev_id;

	String response;
	Post result;
	SharedPreferences prefs;
	String link = "http://tp14.pwh-r1.com/~wwwvikra/voteforpm/VoteForPMJSON.php?";
	private String get_FullStory;

	private String get_id;
	private String get_User_UDID;
	private String get_User_Suggestion;
	private String get_User_EmailId;
	private String get_User_Name;
	private String get_Vote_DateAndTime;
	private String get_Vote_Id;
	ProgressDialog dialog;

	private ImageView img_Chakar;
	private int j;
	private int i;
	private int animangle;
	private RotateAnimation rotateAnimation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);

		result = new Post(link);

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		dev_id = tm.getDeviceId();

		img_Chakar = (ImageView) findViewById(R.id.img_Chakra);

		j = 0;
		i = (int) (Math.floor(Math.random() * (7200)));
		j = i % 360;
		animangle = j;
		rotateAnimation = new RotateAnimation(0, i, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setRepeatMode(Animation.INFINITE);
		rotateAnimation.setFillAfter(true);
		rotateAnimation.setDuration(10000);
		rotateAnimation.setInterpolator(new DecelerateInterpolator());
		img_Chakar.startAnimation(rotateAnimation);

		// if (!mIsBackButtonPressed) {
		// start the home screen if the back button wasn't pressed already
		boolean get_onlineStatus = isOnline();
		// Log.e("", ""+get_onlineStatus);
		if (get_onlineStatus == true) {

			new getResponse().execute();
		} else {
			Toast.makeText(getApplicationContext(),
					"No Internet Access Avalibe", Toast.LENGTH_LONG).show();
		}

		// }
	}

	class getResponse extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			List<BasicNameValuePair> AV = new ArrayList<BasicNameValuePair>();
			// hit the main tag in the response "ambulance_reg"
			AV.add(new BasicNameValuePair("act", "Chk_UDID_Details"));
			// now enter the values in the tags so that values can be sent to
			// the given link

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

			dialog = new ProgressDialog(SplashScreen.this);
			dialog.setMessage("Loading please wait");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.e("response", "is::  " + response);

			JSONObject jobjnew;
			get_FullStory = "";
			try {
				
				jobjnew = new JSONObject(response);
				get_FullStory = jobjnew.getString("id");

			} catch (JSONException e1) {

			}

			if (get_FullStory.equals("No User")) {
				dialog.dismiss();
				Intent intent = new Intent(SplashScreen.this,
						MainActivity.class);
				intent.putExtra("Key_Index", 0);
				SplashScreen.this.startActivity(intent);
				finish();
			} else {
				JSONObject jobj;
				try {
					jobj = new JSONObject(response);
					JSONObject nobj = jobj.getJSONObject("personaldetails");

					get_id = nobj.getString("id");
					get_User_UDID = nobj.optString("User_UDID");
					get_User_Suggestion = nobj.optString("User_Suggestion");
					get_User_EmailId = nobj.optString("User_EmailId");
					get_User_Name = nobj.optString("User_Name");
					get_Vote_Id = nobj.optString("Vote_Id");
					get_Vote_DateAndTime = nobj.optString("Vote_DateAndTime");
					dialog.dismiss();

					rotateAnimation
							.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationRepeat(
										Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									// TODO Auto-generated method stub
									if (!mIsBackButtonPressed) {
										CountDownTimer cd = new CountDownTimer(
												1500, 1) {

											@Override
											public void onTick(
													long millisUntilFinished) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onFinish() {
												// TODO Auto-generated method
												// stub
												Intent intent = new Intent(
														SplashScreen.this,
														MainActivity.class);
												intent.putExtra("Key_Index", 1);
												intent.putExtra("key_get_id",
														get_id);
												intent.putExtra(
														"key_get_User_UDID",
														get_User_UDID);
												intent.putExtra(
														"key_get_User_Suggestion",
														get_User_Suggestion);
												intent.putExtra(
														"key_get_User_EmailId",
														get_User_EmailId);
												intent.putExtra(
														"key_get_User_Name",
														get_User_Name);
												intent.putExtra(
														"key_get_Vote_Id",
														get_Vote_Id);
												intent.putExtra(
														"key_get_Vote_DateAndTime",
														get_Vote_DateAndTime);

												SplashScreen.this
														.startActivity(intent);
												finish();

												
											}
										}.start();
									}
								}
							});

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

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

	@Override
	public void onBackPressed() {

		// set the flag to true so the next activity won't start up
		mIsBackButtonPressed = true;
		super.onBackPressed();

	}
}
