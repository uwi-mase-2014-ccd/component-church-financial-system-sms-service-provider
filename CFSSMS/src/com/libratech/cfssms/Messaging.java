package com.libratech.cfssms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;

import com.libratech.cfssms.Models.Account;
import com.libratech.cfssms.Models.DatabaseConnector;
import com.libratech.cfssms.Models.Message;
import com.libratech.cfssms.NewMessage.pushBalance;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class Messaging extends Activity {
	
	ListView allMessages;
	ArrayList<Message> messages = new ArrayList<Message>();
	DatabaseConnector db = new DatabaseConnector();
	private String textMessage = "";
	private String phoneNumber = "";
	private Date sendDate;
	private String dateString = "";
	HomeAdapter messageAdapter = new HomeAdapter(this, messages);
	Account account;
	String mPhoneNumber;
	private String accountNo = "";
	private float balance = 0;
	public final static String EXTRA_MESSAGE ="com.libratech.cfssms.messaging";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messaging);
		allMessages = (ListView) findViewById(R.id.allmessages);
		sendDate = new Date();
		dateString = new SimpleDateFormat("yyyy-MM-dd").format(sendDate);
		//TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		//mPhoneNumber = tMgr.getLine1Number();
		try {
			new getMessages()
					.execute("http://www.holycrosschurchjm.com/serviceprovider.php?getmessages=yes");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messaging, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		

		case R.id.newMessage:
			/*Intent i = new Intent(this, NewMessage.class);
			i.putExtra("EXTRA_MESSAGE", balance);
			startActivity(i);*/
		
			onClick();
			
			return true;
			
		case R.id.checkCredit:
			String message = "your primary balance is $"+balance;
			Toast.makeText(Messaging.this, message,
					Toast.LENGTH_LONG).show();			
			return true; 
			
		case R.id.addCredit:
			new pushBalances().execute("http://holycrosschurchjm.com/serviceprovider.php?" +
					"setaccount=yes&balance=50000");
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	//@Override
	public void onClick() {
		Intent i = new Intent(this, NewMessage.class);
		i.putExtra("user_balance", account.getBalance());
		startActivity(i);
	}
	
	class getMessages extends AsyncTask<String, Void, JSONArray> {
		protected JSONArray doInBackground(String... url) {
			return db.DBPull(url[0]);
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			
			if (result != null) {
				for (int i = 0; i < result.length(); i++) {
					try {
						textMessage = result.getJSONArray(i).getString(0);
						phoneNumber = result.getJSONArray(i).getString(1);
						dateString = result.getJSONArray(i).getString(2);						
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					messages.add(new Message(textMessage, phoneNumber, dateString));
				}
				allMessages.setAdapter(messageAdapter);
				new getAccount().execute("http://www.holycrosschurchjm.com/serviceprovider.php?getaccount=yes");
			}
		}
	}
	
	
	class getAccount extends AsyncTask<String, Void, JSONArray> {
		protected JSONArray doInBackground(String... url) {
			return db.DBPull(url[0]);
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			
			if (result != null) {
				for (int i = 0; i < result.length(); i++) {
					try {
						accountNo = result.getJSONArray(i).getString(0);
						balance = (float) result.getJSONArray(i).getDouble(1);
						account = new Account (accountNo,balance);
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					
				}
				
			}
		}
			
	
	}
	
	class pushBalances extends AsyncTask<String, Void, Boolean> {
		protected Boolean doInBackground(String... fields) {			
			return db.DBPush(fields[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			String message;
			if (result) {
				message = "Credit Added";
				Toast.makeText(Messaging.this, message,
						Toast.LENGTH_SHORT).show();				
			} 
			else 
			{
				message = "Error while sending message.";								
			}
			
			//NavUtils.navigateUpFromSameTask(Messaging.this);			
		}
	}

}
