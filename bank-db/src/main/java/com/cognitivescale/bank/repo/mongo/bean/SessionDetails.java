package com.cognitivescale.bank.repo.mongo.bean;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "session_details")
public class SessionDetails {

	@Id
	private ObjectId id;

	@Field("account_number")
	private String accountNumber;

	@Field("session_token")
	private String token;

	@Field("valid_upto")
	private long validity;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getValidity() {
		return validity;
	}

	public void setValidity(long validity) {
		this.validity = validity;
	}

}
