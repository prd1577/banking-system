package com.cognitivescale.bank.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BenificiaryRequestDto {

	@JsonProperty("account_number")
	private String accountNumber;

	@JsonProperty("session_token")
	private String sessionToken;

	@JsonProperty("operation_type")
	private String operationType;

	@JsonProperty("benificiary_name")
	private String benificiaryName;

	@JsonProperty("benificiary_account_number")
	private String benificiaryAccountNumber;

	@JsonProperty("benificiary_ifsc_code")
	private String benificiaryIfscCode;

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

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
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

}
