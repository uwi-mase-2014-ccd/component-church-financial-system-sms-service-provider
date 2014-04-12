package com.libratech.cfssms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.libratech.cfssms.Models.DatabaseConnector;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class NewMessage extends Activity {
	
	DatabaseConnector db = new DatabaseConnector();
	EditText phoneNumber;
	EditText textMessage;
	Button sendMessage;
	float balance;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		sendMessage = (Button) findViewById(R.id.sendMsg);		
		Intent intent = getIntent();		
		float balance = intent.getFloatExtra("user_balance",0);
		this.balance = balance;
		setContentView(R.layout.activity_new_message);
		// Show the Up button in the action bar.
		setupActionBar();
		
		
	}
	
	public void proClick(View v) {
		// TODO Auto-generated method stub
		String textMsg,phNum;
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		textMessage = (EditText) findViewById(R.id.typeMsg);
		phNum = phoneNumber.getText().toString();
		textMsg = textMessage.getText().toString();
		
		
		Log.d("balance","value:"+balance);
		
		if(phNum.isEmpty()||textMsg.isEmpty())
		{
			Toast.makeText(NewMessage.this,
					"Empty Field", Toast.LENGTH_LONG).show();
		}
		else
		if ((balance<3)||balance<Float.parseFloat(textMsg)+3)
		{			
			Toast.makeText(NewMessage.this,
					"your credit balance is too low", Toast.LENGTH_LONG).show();
		}
		else
		{
			balance= balance - (Float.parseFloat(textMsg)+3);			
			int msgNo = (int) (100000 * Math.random());
			Log.d("msgNo","value: "+msgNo);			
			Date date = new Date();
			String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			new pushMessage().execute("http://holycrosschurchjm.com/serviceprovider.php?"+
			"sendmessage=yes&message_no="+msgNo+"&text_message="+textMsg+"&phone_number="+phNum+
			"&send_date="+dateString.replace(" ", "%20"));
		}
		
	}
	
	class pushMessage extends AsyncTask<String, Void, Boolean> {
		protected Boolean doInBackground(String... fields) {			
			return db.DBPush(fields[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			String message;
			if (result) {
				message = "Message Sent";
				new pushBalance().execute("http://holycrosschurchjm.com/serviceprovider.php?" +
						"setaccount=yes&balance="+balance);
															
			} 
			else 
			{
				message = "Error while sending message.";	
				Toast.makeText(NewMessage.this, message,
						Toast.LENGTH_SHORT).show();	
			}
			
						
		}
	}
	
	class pushBalance extends AsyncTask<String, Void, Boolean> {
		protected Boolean doInBackground(String... fields) {			
			return db.DBPush(fields[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
		textMessage = (EditText) findViewById(R.id.typeMsg);
		String textMsg = textMessage.getText().toString();
		String email[] = new String []{"maseu21@gmail.com","New Donation","A new donation of "+(Float.parseFloat(textMsg))+" has been made"};			
		new logEmail().execute(email[0],email[1],email[2]);
		}
	}
	
	class logEmail extends AsyncTask<String, Void, Boolean>
	{
		protected Boolean doInBackground(String... fields) {			
			return db.mailtoChurchLog(fields[0],fields[1],fields[2]);
						
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			String message;	
			if (result)
			{	
				message = "Message Sent";
				Toast.makeText(NewMessage.this, message,
						Toast.LENGTH_SHORT).show();	
															
			}
			else
			{
				message = "Error while sending message.";
				Toast.makeText(NewMessage.this, message,
						Toast.LENGTH_SHORT).show();	
			}
			NavUtils.navigateUpFromSameTask(NewMessage.this);
		}
	}
	

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
