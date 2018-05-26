package com.example.d2_eugene.ft_hangouts.models;

public class SmsMessage {

	//from sms
	public final String from;

	//Sms body
	public final String body;

	//date
	public final String date;


	public SmsMessage(String from, String body, String date) {
		this.from = from;
		this.body   = body;
		this.date   = date;
	}
}
