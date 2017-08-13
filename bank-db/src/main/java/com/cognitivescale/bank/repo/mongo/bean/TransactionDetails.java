package com.cognitivescale.bank.repo.mongo.bean;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "transaction_details")
public class TransactionDetails {

	@Id
	private ObjectId id;

	@Field("account_number")
	private String accountNumber;

	@Field("transactions")
	private List<Transaction> transactions;

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

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
}
