package com.cognitivescale.bank.repo.mongo.bean;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "account_details")
public class AccountDetails {

	@Id
	private ObjectId id;

	@Field("user_name")
	private String userName;

	@Field("password")
	private String password;

	@Field("balance")
	private String balance;

	@Field("account_number")
	private String accountNumber;

	@Field("currency")
	private String currency;

	@Field("benificieries")
	private List<Benificiary> benificieries;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<Benificiary> getBenificieries() {
		return benificieries;
	}

	public void setBenificieries(List<Benificiary> benificieries) {
		this.benificieries = benificieries;
	}

	@Override
	public String toString() {
		return "Account for user: " + userName;
	}
}
