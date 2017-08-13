package com.cognitivescale.bank.repo.mongo.bean;

import org.springframework.data.mongodb.core.mapping.Field;

public class Transaction {

	@Field("benificiary_name")
	private String benificiaryName;

	@Field("amount")
	private String amount;

	@Field("transaction_type")
	private String transactionType;

	@Field("timestamp")
	private long timestamp;

	public String getBenificiaryName() {
		return benificiaryName;
	}

	public void setBenificiaryName(String benificiaryName) {
		this.benificiaryName = benificiaryName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
