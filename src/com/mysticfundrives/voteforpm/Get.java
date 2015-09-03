package com.mysticfundrives.voteforpm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class Get {
	public String responseString;
	public JSONObject jobjResponse;
	String tagname = "";
	HttpPost httppost;

	public Get(String link) {
		httppost = new HttpPost(link);
	}

	public String calLoginService() throws JSONException {
		HttpClient httpclient = new DefaultHttpClient();

		try {
			HttpResponse response = httpclient.execute(httppost);

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

		} catch (IOException e) {
		}
		return responseString;
	}

}
