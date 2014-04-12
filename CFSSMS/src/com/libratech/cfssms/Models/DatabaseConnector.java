package com.libratech.cfssms.Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class DatabaseConnector 
{
	
	InputStream is;
	String result;
	JSONArray jArray;

	public DatabaseConnector() {
		is = null;
		result = "";
		jArray = null;
	}

	public JSONArray DBPull(String url) {
		// Download JSON data from URLs
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
		}

		// Convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {

			jArray = new JSONArray(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return jArray;
	}

	public boolean DBPush(String fields) {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(fields);
		
		try {
			
			httpclient.execute(httppost);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean mailtoChurchLog(String receiver, String subject, String body)
	{
		HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(
                "http://ticketmanager.mysoftware.io:8100/component-email/src/send_email.php");
        httpPost.addHeader("Content-type",
                "application/x-www-form-urlencoded");
        BasicNameValuePair destination = new BasicNameValuePair(
                "To", receiver); 
        BasicNameValuePair topic = new BasicNameValuePair(
                "Subject", subject);
        BasicNameValuePair content = new BasicNameValuePair(
                "Body", body);
        

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(destination);
        nameValuePairList.add(topic);
        nameValuePairList.add(content);
        
        try {

            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost); 
            return true;
        } 
        catch (ClientProtocolException e) {
            Log.d("Exception - ","HttpResponese:... "
                            + e);
            e.printStackTrace();
            return false;
        }
        catch (UnsupportedEncodingException e) {
            Log.d("Exception - ","UrlEncodedFormEntity argument:... "
                            + e);
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            Log.d("Exception - ","HttpResponse:... "
                            + e);
            e.printStackTrace();
            return false;
        }
        
    
	}

}
