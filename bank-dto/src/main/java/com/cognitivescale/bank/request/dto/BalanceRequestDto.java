package com.cognitivescale.bank.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceRequestDto {

	@JsonProperty("account_number")
	private String accountNumber;

	@JsonProperty("session_token")
	private String sessionToken;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

}
