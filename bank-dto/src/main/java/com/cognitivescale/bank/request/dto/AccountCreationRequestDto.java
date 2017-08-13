package com.cognitivescale.bank.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountCreationRequestDto {

	@JsonProperty("user_name")
	private String userName;

	@JsonProperty("password")
	private String password;

	@JsonProperty("balance")
	private String balance;

	@JsonProperty("account_number")
	private String accountNumber;

	@JsonProperty("currency")
	private String currency;

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
}
