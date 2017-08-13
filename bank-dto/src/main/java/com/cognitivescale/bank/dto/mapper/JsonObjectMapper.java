package com.cognitivescale.bank.dto.mapper;

import java.io.IOException;

import com.cognitivescale.bank.request.dto.AccountCreationRequestDto;
import com.cognitivescale.bank.request.dto.BalanceRequestDto;
import com.cognitivescale.bank.request.dto.BenificiaryRequestDto;
import com.cognitivescale.bank.request.dto.TransactionHistoryRequestDto;
import com.cognitivescale.bank.request.dto.TransactionRequestDto;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * This class is responsible for holding object mapper instances.
 * 
 * @author PiyushR
 *
 */
public enum JsonObjectMapper {

	/** The instance. */
	INSTANCE;

	/** The object mapper for benificiary request. */
	private final ObjectMapper balanceRequestMapper = new ObjectMapper();

	/** The object mapper for benificiary request. */
	private final ObjectMapper benificiaryRequestMapper = new ObjectMapper();

	/** The object mapper for transaction request. */
	private final ObjectMapper transactionRequestMapper = new ObjectMapper();

	/** The object mapper for account creation request. */
	private final ObjectMapper accountCreationRequestMapper = new ObjectMapper();

	/** The object mapper for transaction history request. */
	private final ObjectMapper transactionHistoryRequestMapper = new ObjectMapper();

	/**
	 * Instantiates a new object mapper.
	 */
	private JsonObjectMapper() {

		balanceRequestMapper.setSerializationInclusion(Include.NON_NULL);
		balanceRequestMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		balanceRequestMapper.getFactory().configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

		benificiaryRequestMapper.setSerializationInclusion(Include.NON_NULL);
		benificiaryRequestMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		benificiaryRequestMapper.getFactory().configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

		transactionRequestMapper.setSerializationInclusion(Include.NON_NULL);
		transactionRequestMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		transactionRequestMapper.getFactory().configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

		accountCreationRequestMapper.setSerializationInclusion(Include.NON_NULL);
		accountCreationRequestMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		accountCreationRequestMapper.getFactory().configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

		transactionHistoryRequestMapper.setSerializationInclusion(Include.NON_NULL);
		transactionHistoryRequestMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		transactionHistoryRequestMapper.getFactory().configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

	}

	/**
	 * Reads benificiary request from json.
	 * 
	 * @param jsonString
	 *            the json
	 * @return the BenificiaryRequestDto
	 * @throws JsonParseException
	 *             the JsonParseException
	 * @throws JsonMappingException
	 *             the JsonMappingException
	 * @throws IOException
	 *             the IOException
	 */
	public BenificiaryRequestDto readBenificiaryRequestFromJson(String jsonString)
			throws JsonParseException, JsonMappingException, IOException {

		return benificiaryRequestMapper.readValue(jsonString, BenificiaryRequestDto.class);
	}

	/**
	 * Reads balance request from json
	 * 
	 * @param jsonString
	 *            the json
	 * @return the BalanceRequestDto
	 * @throws JsonParseException
	 *             the JsonParseException
	 * @throws JsonMappingException
	 *             the JsonMappingException
	 * @throws IOException
	 *             the IOException
	 */
	public BalanceRequestDto readBalanceRequestFromJson(String jsonString)
			throws JsonParseException, JsonMappingException, IOException {
		return balanceRequestMapper.readValue(jsonString, BalanceRequestDto.class);
	}

	/**
	 * Reads transaction request from json
	 * 
	 * @param jsonString
	 *            the json
	 * @return the TransactionRequestDto
	 * @throws JsonParseException
	 *             the JsonParseException
	 * @throws JsonMappingException
	 *             the JsonMappingException
	 * @throws IOException
	 *             the IOException
	 */
	public TransactionRequestDto readTransactionRequestFromJson(String jsonString)
			throws JsonParseException, JsonMappingException, IOException {

		return transactionRequestMapper.readValue(jsonString, TransactionRequestDto.class);
	}

	/**
	 * Reads account creation request from json
	 * 
	 * @param jsonString
	 *            the json
	 * @return the AccountCreationRequestDto
	 * @throws JsonParseException
	 *             the JsonParseException
	 * @throws JsonMappingException
	 *             the JsonMappingException
	 * @throws IOException
	 *             the IOException
	 */
	public AccountCreationRequestDto readAccountCreationRequestFromJson(String jsonString)
			throws JsonParseException, JsonMappingException, IOException {

		return accountCreationRequestMapper.readValue(jsonString, AccountCreationRequestDto.class);
	}

	/**
	 * Reads transaction history request from json
	 * 
	 * @param jsonString
	 *            the json
	 * @return the TransactionHistoryRequestDto
	 * @throws JsonParseException
	 *             the JsonParseException
	 * @throws JsonMappingException
	 *             the JsonMappingException
	 * @throws IOException
	 *             the IOException
	 */
	public TransactionHistoryRequestDto readTransactionHistoryRequestFromJson(String jsonString)
			throws JsonParseException, JsonMappingException, IOException {

		return transactionHistoryRequestMapper.readValue(jsonString, TransactionHistoryRequestDto.class);
	}
}
