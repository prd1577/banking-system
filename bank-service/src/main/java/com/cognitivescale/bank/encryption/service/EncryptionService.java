package com.cognitivescale.bank.encryption.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cognitivescale.bank.service.BankService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * The Encryption Service.
 * 
 * @author PiyushR
 *
 */
@Service
public class EncryptionService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(BankService.class);

	/**
	 * Returns the encrypted password.
	 * 
	 * @param password
	 *            the password.
	 * @return the encrypted password.
	 */
	public String encrypt(String password) {
		logger.debug("Encrypting password...");
		return Base64.encode(password.getBytes());
	}

	/**
	 * Returns the plain password.
	 * 
	 * @param encryptedPassword
	 *            the encrypted password
	 * @return the original password
	 */
	public String decrypt(String encryptedPassword) {
		logger.debug("Decrypting password...");
		byte[] bytes = Base64.decode(encryptedPassword);
		return new String(bytes);
	}
}
