package com.cognitivescale.bank.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cognitivescale.bank.currency.converter.CurrencyConverter;
import com.cognitivescale.bank.dto.mapper.JsonObjectMapper;
import com.cognitivescale.bank.encryption.service.EncryptionService;
import com.cognitivescale.bank.exception.ExecutionException;
import com.cognitivescale.bank.repo.mongo.bean.AccountDetails;
import com.cognitivescale.bank.repo.mongo.bean.Benificiary;
import com.cognitivescale.bank.repo.mongo.bean.SessionDetails;
import com.cognitivescale.bank.repo.mongo.bean.Transaction;
import com.cognitivescale.bank.repo.mongo.bean.TransactionDetails;
import com.cognitivescale.bank.repo.mongo.repository.AccountDetailsRepository;
import com.cognitivescale.bank.repo.mongo.repository.SessionDetailsRepository;
import com.cognitivescale.bank.repo.mongo.repository.TransactionDetailsRepository;
import com.cognitivescale.bank.request.dto.AccountCreationRequestDto;
import com.cognitivescale.bank.request.dto.BalanceRequestDto;
import com.cognitivescale.bank.request.dto.BenificiaryRequestDto;
import com.cognitivescale.bank.request.dto.TransactionHistoryRequestDto;
import com.cognitivescale.bank.request.dto.TransactionRequestDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class BankService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(BankService.class);

	/** The account details repository. */
	private AccountDetailsRepository accountDetailsRepository;

	/** The session details repository. */
	private SessionDetailsRepository sessionDetailsRepository;

	/** The transaction details repository. */
	private TransactionDetailsRepository transactionDetailsRepository;

	/** The encryption service. */
	private EncryptionService encryptionService;

	/** The session duration in minutes. */
	private static final long SESSION_DURATION_IN_MINS = 10l;

	@Autowired
	public BankService(AccountDetailsRepository accountDetailsRepository,
			SessionDetailsRepository sessionDetailsRepository,
			TransactionDetailsRepository transactionDetailsRepository, EncryptionService encryptionService) {
		this.accountDetailsRepository = accountDetailsRepository;
		this.sessionDetailsRepository = sessionDetailsRepository;
		this.transactionDetailsRepository = transactionDetailsRepository;
		this.encryptionService = encryptionService;
	}

	/**
	 * Store the account details to mongoDB.
	 * 
	 * @param accountInformation
	 *            the account information
	 * @throws ExecutionException
	 *             the ExecutionException
	 * @throws IOException
	 *             the IOException
	 * @throws JsonMappingException
	 *             the JsonMappingException
	 * @throws JsonParseException
	 *             the JsonParseException
	 */
	public void createAccount(String accountInformation)
			throws ExecutionException, JsonParseException, JsonMappingException, IOException {

		AccountCreationRequestDto request = JsonObjectMapper.INSTANCE
				.readAccountCreationRequestFromJson(accountInformation);
		if (request != null) {

			logger.debug("Adding account information.");
			AccountDetails accountDetails = new AccountDetails();
			accountDetails.setAccountNumber(request.getAccountNumber());
			accountDetails.setBalance(request.getBalance());
			accountDetails.setCurrency(request.getCurrency());
			accountDetails.setUserName(request.getUserName());
			accountDetails.setPassword(encryptionService.encrypt(request.getPassword()));

			accountDetailsRepository.save(accountDetails);
			logger.debug("Account information stored successfully.");
			return;

		}
		throw new ExecutionException("Entered details are not proper.");
	}

	/**
	 * Gets the session token for the user.
	 * 
	 * @param accountNumber
	 *            the account number
	 * @param userName
	 *            the user name.
	 * @param password
	 *            the original password.
	 * @return the session token.
	 */
	public String getSessionToken(String accountNumber, String userName, String password) {

		AccountDetails accountDetails;
		if (StringUtils.isBlank(accountNumber)) {
			accountDetails = accountDetailsRepository.findByUserName(userName);
			accountNumber = accountDetails.getAccountNumber();
		} else {
			accountDetails = accountDetailsRepository.findByAccountNumber(accountNumber);
		}
		if (accountDetails != null && isUserValid(accountDetails.getPassword(), password)) {

			logger.debug("User is a valid one.");
			String sessionToken = generateSessionToken();
			SessionDetails session = new SessionDetails();
			session.setAccountNumber(accountNumber);
			session.setToken(sessionToken);
			// The validity of the session token is for 10 minutes.
			session.setValidity(System.currentTimeMillis() + (SESSION_DURATION_IN_MINS * 60l * 1000l));
			logger.debug("Session token created for the user.");

			SessionDetails existingDetail = sessionDetailsRepository.findByAccountNumber(accountNumber);
			// Delete if a session token already exists for the user.
			if (existingDetail != null) {
				logger.debug("Deleting the existing session token for the user.");
				sessionDetailsRepository.delete(existingDetail);
			}
			sessionDetailsRepository.save(session);
			logger.debug("New session detail is saved to mongoDB.");
			return "token: ".concat(sessionToken);
		}

		return "";
	}

	/**
	 * Gets the session token for the user.
	 * 
	 * @return the session token.
	 */
	private String generateSessionToken() {

		String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder token = new StringBuilder();
		Random random = new Random();
		while (token.length() < 21) {
			int index = random.nextInt() * allowedCharacters.length();
			token.append(allowedCharacters.charAt(index));
		}
		return token.toString();
	}

	/**
	 * Checks whether the user is genuine or not.
	 * 
	 * @param encryptedPassword
	 *            the encrypted password
	 * @param password
	 *            the original password
	 * @return true if user if valid, otherwise false.
	 */
	private boolean isUserValid(String encryptedPassword, String password) {
		return encryptionService.decrypt(encryptedPassword).equals(password) ? true : false;
	}

	/**
	 * Gets the balance information with respect to the account number.
	 * 
	 * @param accountInformation
	 *            the account information.
	 * 
	 * @return the balance.
	 * @throws IOException
	 *             the IOException.
	 * @throws ExecutionException
	 *             the ExecutionException.
	 */
	public String getBalanceInfo(String accountInformation) throws IOException, ExecutionException {

		BalanceRequestDto request = JsonObjectMapper.INSTANCE.readBalanceRequestFromJson(accountInformation);

		if (request != null) {

			String accountNumber = request.getAccountNumber();
			;
			String sessionToken = request.getSessionToken();
			if (StringUtils.isNoneBlank(accountNumber) && StringUtils.isNoneBlank(sessionToken)) {
				logger.debug("Received the required details for account {}", accountNumber);

				if (isSessionTokenValid(accountNumber, sessionToken)) {

					logger.debug("Getting the balance.");
					return accountDetailsRepository.findByAccountNumber(accountNumber).getBalance();
				}
			}
			logger.error("Either of account number or session token is not received.");
			throw new ExecutionException("Error while fetching account number or session token");
		}
		logger.error("Account information is not proper.");
		throw new ExecutionException("Invalid access to the account");
	}

	/**
	 * Checks the validity of session token.
	 * 
	 * @param accountNumber
	 *            the account number
	 * @param sessionToken
	 *            the session token
	 * @return true is session is valid, otherwise false.
	 */
	private boolean isSessionTokenValid(String accountNumber, String sessionToken) {

		SessionDetails sessionDetail = sessionDetailsRepository.findByAccountNumber(accountNumber);
		if (sessionDetail.getToken().equals(sessionToken)) {

			logger.debug("Session token matched.");
			if (sessionDetail.getValidity() > System.currentTimeMillis()) {

				logger.debug("Session token is valid.");
				return true;
			} else {

				logger.error("Session token is correct but has expired.");
				return false;
			}
		} else {

			logger.error("Incorrect session token entered.");
			return false;
		}
	}

	/**
	 * Updates the list of benificiary.
	 * 
	 * @param benificiaryInformation
	 *            the benificiary information
	 * @throws JsonParseException
	 *             the JsonParseException
	 * @throws JsonMappingException
	 *             the JsonMappingException
	 * @throws IOException
	 *             the IOException
	 * @throws ExecutionException
	 *             the ExecutionException
	 */
	public void updateBenificiaryList(String benificiaryInformation)
			throws JsonParseException, JsonMappingException, IOException, ExecutionException {

		logger.info("Started uptating the list of benificiaries.");
		BenificiaryRequestDto request = JsonObjectMapper.INSTANCE
				.readBenificiaryRequestFromJson(benificiaryInformation);
		logger.debug("Received request for modifying benificiary.");

		String accountNumber = request.getAccountNumber();
		String sessionToken = request.getSessionToken();

		if (StringUtils.isBlank(accountNumber) && StringUtils.isBlank(sessionToken)) {
			throw new ExecutionException("Either or both account number and session token is empty.");
		}
		if (isSessionTokenValid(accountNumber, sessionToken)) {

			String benificiaryAccountNumber = request.getBenificiaryAccountNumber();
			String ifsc = request.getBenificiaryIfscCode();
			String benificiaryName = request.getBenificiaryName();

			if (StringUtils.isBlank(ifsc) && StringUtils.isBlank(benificiaryName)
					&& StringUtils.isBlank(benificiaryAccountNumber)) {
				throw new ExecutionException("Benificiary details are not proper..");
			}

			AccountDetails accountDetails = accountDetailsRepository.findByAccountNumber(accountNumber);
			if (accountDetails != null) {

				logger.debug("Fetched the account details.");
				Benificiary benificiary = new Benificiary();
				benificiary.setAccountNumber(accountNumber);
				benificiary.setIfsc(ifsc);
				benificiary.setName(benificiaryName);

				String operationType = request.getOperationType();
				if (StringUtils.isNoneBlank(operationType) && operationType.equals("add")) {

					accountDetails.getBenificieries().add(benificiary);
					logger.debug("Benificiary added to the list");

				} else if (StringUtils.isNoneBlank(operationType) && operationType.equals("remove")) {

					accountDetails.getBenificieries().remove(accountDetails);
					logger.debug("Benificiary removed from the list");

				} else {
					throw new ExecutionException("Provide a proper operation type.");
				}
				accountDetailsRepository.save(accountDetails);
				logger.info("List of benificiaries is updated.");

			} else {
				throw new ExecutionException("Could not find the account number.");
			}
		}
	}

	/**
	 * Trnsfers the amount from user's account to benificiary's account
	 * 
	 * @param transactionInformation
	 *            the transaction information
	 * @throws JsonParseException
	 *             the JsonParseException
	 * @throws JsonMappingException
	 *             the JsonMappingException
	 * @throws IOException
	 *             the IOException
	 * @throws ExecutionException
	 *             the ExecutionException
	 */
	public void performTransaction(String transactionInformation)
			throws JsonParseException, JsonMappingException, IOException, ExecutionException {

		logger.info("Started transaction.");
		TransactionRequestDto request = JsonObjectMapper.INSTANCE
				.readTransactionRequestFromJson(transactionInformation);
		logger.debug("Received request for transaction.");

		if (request != null) {
			String accountNumber = request.getAccountNumber();
			String sessionToken = request.getSessionToken();

			if (StringUtils.isBlank(accountNumber) && StringUtils.isBlank(sessionToken)) {
				throw new ExecutionException("Either or both account number and session token is empty.");
			}
			if (isSessionTokenValid(accountNumber, sessionToken)) {

				logger.debug("Session token is valid.");
				String benificiaryAccountNumber = request.getBenificiaryAccountNumber();
				String ifsc = request.getBenificiaryIfscCode();
				String benificiaryName = request.getBenificiaryName();
				String amount = request.getAmount();
				String benificiaryCurrency = request.getBenificiaryCurrency();

				if (StringUtils.isBlank(benificiaryCurrency) && StringUtils.isBlank(amount) && StringUtils.isBlank(ifsc)
						&& StringUtils.isBlank(benificiaryName) && StringUtils.isBlank(benificiaryAccountNumber)) {
					throw new ExecutionException("Benificiary details are not proper.");
				}

				AccountDetails accountDetails = accountDetailsRepository.findByAccountNumber(accountNumber);
				if (accountDetails != null) {

					logger.debug("Account details fetched successfully.");
					double debitAmount = amountToDebit(accountDetails.getBalance(), accountDetails.getCurrency(),
							amount, benificiaryCurrency);
					if (debitAmount > 0) {

						logger.debug("Updating balance..");
						double updatedBalance = Double.parseDouble(accountDetails.getBalance()) - debitAmount;
						accountDetails.setBalance(String.valueOf(updatedBalance));

						accountDetailsRepository.save(accountDetails);

						logger.info("Balance updated successfully for the user.");
						updateTransactionDetailsToDB(accountDetails.getAccountNumber(), benificiaryName, debitAmount,
								"debit", System.currentTimeMillis());
						logger.debug("Transaction detail recorede in Mongo DB.");

						if (accountDetailsRepository.findByAccountNumber(benificiaryAccountNumber) != null) {

							logger.info("Benificiary has account in the same bank.");
							updateTransactionDetailsToDB(benificiaryAccountNumber, accountDetails.getUserName(),
									Double.parseDouble(amount), "credit", System.currentTimeMillis());
							logger.debug("Benificiary transaction details recorded in Mongo DB.");

						} else {
							logger.info("Benificiary has account in another bank.");
						}
						return;
					}
					throw new ExecutionException("Insuffecient fund in account !!");
				}
			}
			throw new ExecutionException("Invalid Session Token");
		}
		throw new ExecutionException("Transaction request is null.");
	}

	/**
	 * Updates the transaction history for the corresponding account number
	 * 
	 * @param accountNumber
	 *            the account number
	 * @param benificiaryName
	 *            the benificiary name
	 * @param amount
	 *            the amount transfered
	 * @param transactionType
	 *            the transaction type (credit or debit)
	 * @param currentTimeMillis
	 *            the transaction time
	 */
	private void updateTransactionDetailsToDB(String accountNumber, String benificiaryName, double amount,
			String transactionType, long currentTimeMillis) {

		Transaction transaction = new Transaction();
		transaction.setAmount(String.valueOf(amount));
		transaction.setBenificiaryName(benificiaryName);
		transaction.setTimestamp(currentTimeMillis);
		transaction.setTransactionType(transactionType);

		TransactionDetails transactionDetails = transactionDetailsRepository.findByAccountNumber(accountNumber);
		if (transactionDetails != null) {
			transactionDetails.getTransactions().add(transaction);
			transactionDetailsRepository.save(transactionDetails);
			logger.debug("Updated the transaction history.");
		} else {
			TransactionDetails firstTransaction = new TransactionDetails();
			firstTransaction.setAccountNumber(accountNumber);
			firstTransaction.setTransactions(Arrays.asList(transaction));
			transactionDetailsRepository.save(firstTransaction);
			logger.debug("Added transaction history for the user.");
		}
	}

	/**
	 * Gets the amount to be debited by considering latest exchange rate.
	 * 
	 * @param balance
	 *            the current balance.
	 * @param currency
	 *            the currency of the user
	 * @param amount
	 *            the amount to debit
	 * @param benificiaryCurrency
	 *            the benificiary's currency
	 * @return the actual amount to be debited
	 * @throws IOException
	 *             the IOException
	 */
	private double amountToDebit(String balance, String currency, String amount, String benificiaryCurrency)
			throws IOException {

		double exchangeRate = CurrencyConverter.INSTANCE.getExchangeRate(currency, benificiaryCurrency);
		logger.debug("Exchange rate is '{}'", exchangeRate);
		return Double.parseDouble(balance) >= Double.parseDouble(amount) / exchangeRate
				? Double.parseDouble(amount) / exchangeRate : 0;
	}

	/**
	 * Gets the transaction history.
	 * 
	 * @param transactionHistoryInformation
	 *            the transaction history information.
	 * @return the list of transactions
	 * @throws JsonParseException
	 *             the JsonParseException
	 * @throws JsonMappingException
	 *             the JsonMappingException
	 * @throws IOException
	 *             the IOException
	 * @throws ExecutionException
	 *             the ExecutionException
	 */
	public List<Transaction> getTransactionHistory(String transactionHistoryInformation)
			throws JsonParseException, JsonMappingException, IOException, ExecutionException {

		TransactionHistoryRequestDto request = JsonObjectMapper.INSTANCE
				.readTransactionHistoryRequestFromJson(transactionHistoryInformation);

		if (request != null) {
			logger.debug("Received request for transaction history.");
			String accountNumber = request.getAccountNumber();
			String sessionToken = request.getSessionToken();

			if (StringUtils.isBlank(accountNumber) && StringUtils.isBlank(sessionToken)) {
				throw new ExecutionException("Either or both account number and session token is empty.");
			}
			if (isSessionTokenValid(accountNumber, sessionToken)) {

				logger.debug("Session token is valid.");
				TransactionDetails transactionDetails = transactionDetailsRepository.findByAccountNumber(accountNumber);
				if (transactionDetails != null) {
					List<Transaction> transactions = transactionDetails.getTransactions();
					if (transactions != null && !transactions.isEmpty()) {
						logger.debug("Found a total of {} transaction history for the user.", transactions.size());
						List<Transaction> result = new ArrayList<>();
						for (Transaction transaction : transactions) {
							if (transaction.getTimestamp() >= request.getFrom()
									&& transaction.getTimestamp() <= request.getTo()) {
								result.add(transaction);
							}
						}
						logger.debug("Got {} transaction details within the search range.", result.size());
						return result;
					}
				}
				throw new ExecutionException("No transaction history for the user.");
			}
			throw new ExecutionException("Session token is not valid.");
		}
		throw new ExecutionException("Request received is not proper.");
	}
}
