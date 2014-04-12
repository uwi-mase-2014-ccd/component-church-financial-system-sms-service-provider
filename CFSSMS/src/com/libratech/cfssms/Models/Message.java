package com.libratech.cfssms.Models;

import java.util.Date;

public class Message {
	
	private String textMessage;
	private String phoneNumber;
	private String sendDate;

	public Message (String textMessage,String phoneNumber, String sendDate)
	{
		this.textMessage = textMessage;
		this.phoneNumber = phoneNumber;
		this.sendDate = sendDate;
	}
	
	public String gettextMessage()
	{
		return textMessage;
	}
	
	public String getphoneNumber()
	{
		return phoneNumber;
	}
	
	public String getsendDate()
	{
		return sendDate;
	}
}
