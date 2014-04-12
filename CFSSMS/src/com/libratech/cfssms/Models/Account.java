package com.libratech.cfssms.Models;

public class Account 
{
	private String phoneNo;
	private float balance;
	
	public Account (String phoneNo,float balance)
	{
		this.phoneNo = phoneNo;
		this.balance = balance;
	}
	
	public String getPhoneNo()
	{
		return phoneNo;
	}
	
	public float getBalance()
	{
		return balance;
	}
	
	public void setBalance(float balance)
	{
		this.balance = balance;
	}
	
	public void addCredit(float amount)
	{
		balance+=amount;
	}
	
	public void subCredit(float amount)
	{
		balance-=amount;
	}
}
