package com.cognitivescale.bank.repo.mongo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cognitivescale.bank.repo.mongo.bean.TransactionDetails;

@Repository
public interface TransactionDetailsRepository
		extends MongoRepository<TransactionDetails, ObjectId>, ITransactionDetailsRepository {

	/**
	 * Gets the account details for corresponding account number.
	 * 
	 * @param accountNumber
	 *            the account number
	 * @return the account details
	 */
	public TransactionDetails findByAccountNumber(String accountNumber);
}
