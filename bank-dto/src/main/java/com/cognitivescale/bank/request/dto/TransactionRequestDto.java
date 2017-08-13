package com.cognitivescale.bank.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionRequestDto {

	@JsonProperty("account_number")
	private String accountNumber;

	@JsonProperty("session_token")
	private String sessionToken;

	@JsonProperty("benificiary_name")
	private String benificiaryName;

	@JsonProperty("benificiary_account_number")
	private String benificiaryAccountNumber;

	@JsonProperty("benificiary_ifsc_code")
	private String benificiaryIfscCode;

	@JsonProperty("amount")
	private String amount;

	@JsonProperty("benificiary_currency")
	private String benificiaryCurrency;

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

	public String getBenificiaryName() {
		return benificiaryName;
	}

	public void setBenificiaryName(String benificiaryName) {
		this.benificiaryName = benificiaryName;
	}

	public String getBenificiaryAccountNumber() {
		return benificiaryAccountNumber;
	}

	public void setBenificiaryAccountNumber(String benificiaryAccountNumber) {
		this.benificiaryAccountNumber = benificiaryAccountNumber;
	}

	public String getBenificiaryIfscCode() {
		return benificiaryIfscCode;
	}

	public void setBenificiaryIfscCode(String benificiaryIfscCode) {
		this.benificiaryIfscCode = benificiaryIfscCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBenificiaryCurrency() {
		return benificiaryCurrency;
	}

	public void setBenificiaryCurrency(String benificiaryCurrency) {
		this.benificiaryCurrency = benificiaryCurrency;
	}

}
