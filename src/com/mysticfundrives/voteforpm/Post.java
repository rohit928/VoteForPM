package com.mysticfundrives.voteforpm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


//import android.util.Log;

public class Post {
	public String responseString;
	public JSONObject jobjResponse;
	String tagname = "";
	HttpPost http;

	public Post(String link) {
		http = new HttpPost(link);
	}

	public String calLoginService(List<BasicNameValuePair> nameValuePairs)
			throws JSONException {
		HttpClient httpclient = new DefaultHttpClient();

		try {
			// Log.e("###Request###", "request" + nameValuePairs);
			http.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(http);

			// Log.e("###Response###", "response");

			InputStream is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			responseString = sb.toString();

//			JSONObject jobj = new JSONObject(responseString);
//			// Log.e("obj", "value  " + jobj);
//			tagname = jobj.getString("message");
//			Log.e("tag", "" + responseString);

		} catch (IOException e) {
		}
		return responseString;
	}

}
