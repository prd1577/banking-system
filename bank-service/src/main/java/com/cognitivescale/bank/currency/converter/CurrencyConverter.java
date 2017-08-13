package com.cognitivescale.bank.currency.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is responsible for getting the current exchange rate between two
 * currencies.
 * 
 * @author PiyushR
 *
 */
public enum CurrencyConverter {

	/** The Instance. */
	INSTANCE;

	private static final String URL = "http://api.fixer.io/DATE?base=CURRENCY1&symbols=CURRENCY2";

	/**
	 * Gets the exchange rate.
	 * 
	 * @param fromCurrency
	 *            the base currency.
	 * @param toCurrency
	 *            the enquired currency.
	 * @return the exchange rate
	 * @throws IOException
	 *             the IOException
	 */
	public double getExchangeRate(String fromCurrency, String toCurrency) throws IOException {

		String modifiedURL = buildUrl(fromCurrency, toCurrency);

		URL object = new URL(modifiedURL);
		HttpURLConnection connection = (HttpURLConnection) object.openConnection();

		connection.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// Extracting the exchange rate from the response.
		String exchangeRate = response.substring(response.lastIndexOf(":") + 1, response.length() - 2);

		return Double.parseDouble(exchangeRate);
	}

	/**
	 * Builds the URL
	 * 
	 * @param fromCurrency
	 *            the base currency
	 * @param toCurrency
	 *            the enquired currency
	 * @return the URL
	 */
	private String buildUrl(String fromCurrency, String toCurrency) {

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String modifiedUrl = URL.replace("DATE", format.format(date)).replace("CURRENCY1", fromCurrency)
				.replace("CURRENCY2", toCurrency);

		return modifiedUrl;
	}
}
