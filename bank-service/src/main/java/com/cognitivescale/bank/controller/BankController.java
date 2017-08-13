package com.cognitivescale.bank.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognitivescale.bank.exception.ExecutionException;
import com.cognitivescale.bank.repo.mongo.bean.Transaction;
import com.cognitivescale.bank.service.BankService;

@RestController
public class BankController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(BankController.class);

	/** The bank service. */
	private BankService bankService;

	/**
	 * Instantiates a new bank controller.
	 *
	 * @param bankService
	 *            the bank service
	 */
	@Autowired
	public BankController(BankService bankService) {
		this.bankService = bankService;
	}

	/**
	 * Stores the account information.
	 * 
	 * @param accountInformation
	 *            the account information
	 * @return the response entity.
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> storeDetails(@RequestBody String accountInformation) {

		if (accountInformation != null) {
			logger.debug("Received request for creating ana account");

			try {
				bankService.createAccount(accountInformation);
			} catch (ExecutionException | IOException e) {
				logger.error("Exception occured while creating an account", e);
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}

			logger.info("Account created successfully");
			return new ResponseEntity<Void>(HttpStatus.CREATED);

		} else {
			logger.error("Received account information is null");
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Gets the session token for the user.
	 * 
	 * @param userName
	 *            the username.
	 * @param accountNumber
	 *            the account number.
	 * @param password
	 *            the password.
	 * @return the response entity.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<String> getSessionToken(@RequestParam(name = "username", required = false) String userName,
			@RequestParam(name = "account_number", required = false) String accountNumber,
			@RequestParam(name = "password", required = true) String password) {

		if (StringUtils.isBlank(accountNumber) && StringUtils.isBlank(userName)) {
			logger.error("Both username and account are empty.");
			return new ResponseEntity<String>("Both username and account number cannot be empty.",
					HttpStatus.BAD_REQUEST);
		}
		logger.debug("Getting the session token.");
		String sessionToken = bankService.getSessionToken(accountNumber, userName, password);

		if (StringUtils.isBlank(sessionToken)) {
			logger.error("Unable to create a session token.");
			return new ResponseEntity<String>("Authentication Error!!", HttpStatus.BAD_REQUEST);
		}
		logger.debug("Session token created successfully.");
		return new ResponseEntity<String>(sessionToken, HttpStatus.OK);
	}

	/**
	 * Gets the balance information for the respective account number.
	 * 
	 * @param accountInformation
	 *            the account information.
	 * @return the response entity.
	 */
	@RequestMapping(value = "/balance-info}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getBalanceInfo(@RequestBody String accountInformation) {

		if (StringUtils.isBlank(accountInformation)) {
			logger.error("Received account information is null.");
			return new ResponseEntity<>("Incorrect Information", HttpStatus.BAD_REQUEST);
		}
		String balance = null;
		try {
			balance = bankService.getBalanceInfo(accountInformation);
		} catch (IOException | ExecutionException e) {
			logger.error("Exception ocured: ", e);
			return new ResponseEntity<String>("Exception Occured", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(balance, HttpStatus.OK);
	}

	/**
	 * Updates the list of benificiaries.
	 * 
	 * @param benificiaryInformation
	 *            the benificiary information
	 * @return the response entity.
	 */
	@RequestMapping(value = "/modify-benificiary", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateBenificiary(@RequestBody String benificiaryInformation) {

		if (StringUtils.isBlank(benificiaryInformation)) {
			logger.error("Received benificiary information is not proper.");
			return new ResponseEntity<>("Incorrect Information", HttpStatus.BAD_REQUEST);
		}
		try {
			bankService.updateBenificiaryList(benificiaryInformation);
		} catch (IOException | ExecutionException e) {
			logger.error("Exception occured while updating the benificiary list", e);
			return new ResponseEntity<String>("Error while updating", HttpStatus.BAD_REQUEST);
		}
		logger.debug("Benificiary list has been successfully updated.");
		return new ResponseEntity<String>("Benificiaries list successfully updated.", HttpStatus.OK);
	}

	/**
	 * Does the transaction for the user.
	 * 
	 * @param transactionInformation
	 *            the transaction information
	 * @return the response entity.
	 */
	@RequestMapping(value = "/transaction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> doTransaction(@RequestBody String transactionInformation) {

		if (StringUtils.isBlank(transactionInformation)) {
			logger.error("Received transaction information is not proper.");
			return new ResponseEntity<>("Incorrect Information", HttpStatus.BAD_REQUEST);
		}

		try {

			logger.debug("Starting transaction process.");
			bankService.performTransaction(transactionInformation);

		} catch (IOException | ExecutionException e) {

			logger.error("Exception occurred while transfering the amount.", e);
			return new ResponseEntity<String>("Transaction Failed !!", HttpStatus.BAD_REQUEST);
		}
		logger.debug("Transaction completed successfully.");
		return new ResponseEntity<>("Transaction Successful !!", HttpStatus.OK);
	}

	/**
	 * Gets the transaction history for the user.
	 * 
	 * @param transactionHistoryInformation
	 *            the transaction history information.
	 * @return the response entity.
	 */
	@RequestMapping(value = "/transaction-history", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Transaction>> getTransactionHistory(@RequestBody String transactionHistoryInformation) {

		if (StringUtils.isNotBlank(transactionHistoryInformation)) {
			logger.debug("Started fetching transaction history.");
			List<Transaction> transactions = null;
			try {
				transactions = bankService.getTransactionHistory(transactionHistoryInformation);
			} catch (IOException | ExecutionException e) {

				logger.error("Exception occured while fetching transaction history.");
				return new ResponseEntity<List<Transaction>>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
			}
			logger.info("Got {} transaction history within the specified time range.", transactions.size());
			return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
		}
		logger.error("Information provided is not proper. Could not get transaction history.");
		return new ResponseEntity<List<Transaction>>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
	}

}
