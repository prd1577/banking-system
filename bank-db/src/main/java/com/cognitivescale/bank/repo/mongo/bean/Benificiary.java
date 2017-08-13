package com.cognitivescale.bank.repo.mongo.bean;

import org.springframework.data.mongodb.core.mapping.Field;

public class Benificiary {

	@Field("account_number")
	private String accountNumber;

	@Field("name")
	private String name;

	@Field("ifsc")
	private String ifsc;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		Benificiary other = (Benificiary) obj;
		if (accountNumber.equals(other.getAccountNumber()) && ifsc.equals(other.getIfsc()))
			return true;
		return false;
	}

}
