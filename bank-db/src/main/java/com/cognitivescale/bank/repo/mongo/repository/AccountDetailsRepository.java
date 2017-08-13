package com.cognitivescale.bank.repo.mongo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cognitivescale.bank.repo.mongo.bean.AccountDetails;

@Repository
public interface AccountDetailsRepository extends MongoRepository<AccountDetails, ObjectId>, IAccountDetailsRepository {

	/**
	 * Gets the account details for corresponding username.
	 * 
	 * @param userName
	 *            the username.
	 * @return the account details.
	 */
	public AccountDetails findByUserName(String userName);

	/**
	 * Gets the account details for corresponding account number.
	 * 
	 * @param accountNumber
	 *            the account number
	 * @return the account details
	 */
	public AccountDetails findByAccountNumber(String accountNumber);
}
