package com.libratech.cfssms;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.libratech.cfssms.Models.Message;

public class HomeAdapter extends BaseAdapter {

	public Context context;
	public ArrayList<Message> data = new ArrayList<Message>();

	public HomeAdapter(Context context, ArrayList<Message> aMessages) {
		this.context = context;
		this.data = aMessages;
		Log.d("data", data.toString());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		if (vi == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(R.layout.message_row, parent,false);
		}
		TextView phoneNumber = (TextView) vi.findViewById(R.id.phoneNumber);
		phoneNumber.setText(data.get(position).getphoneNumber());
		
		TextView message = (TextView) vi.findViewById(R.id.message);		
		message.setText(data.get(position).gettextMessage());
		
		TextView sendDate = (TextView) vi.findViewById(R.id.sendDate);
		sendDate.setText(data.get(position).getsendDate());
		
		return vi;
	}

	public boolean areAllItemsEnabled(boolean set) {
		// TODO Auto-generated method stub
		return set;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

}
