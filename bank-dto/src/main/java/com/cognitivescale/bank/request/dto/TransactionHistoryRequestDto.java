package com.cognitivescale.bank.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionHistoryRequestDto {

	@JsonProperty("account_number")
	private String accountNumber;

	@JsonProperty("session_token")
	private String sessionToken;

	@JsonProperty("from")
	private long from;

	@JsonProperty("to")
	private long to;

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

	public long getFrom() {
		return from;
	}

	public void setFrom(long from) {
		this.from = from;
	}

	public long getTo() {
		return to;
	}

	public void setTo(long to) {
		this.to = to;
	}
}
