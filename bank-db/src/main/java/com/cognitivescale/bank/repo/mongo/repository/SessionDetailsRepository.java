package com.cognitivescale.bank.repo.mongo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cognitivescale.bank.repo.mongo.bean.SessionDetails;

@Repository
public interface SessionDetailsRepository extends MongoRepository<SessionDetails, ObjectId>, ISessionDetailsRepository {

	/**
	 * Gets the session detail for the corresponding account nymber.
	 * 
	 * @param accountNumber
	 *            the account number
	 * @return the session detail
	 */
	public SessionDetails findByAccountNumber(String accountNumber);
}
