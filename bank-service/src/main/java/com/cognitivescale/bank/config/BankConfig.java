package com.cognitivescale.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.cognitivescale.bank" })
public class BankConfig {

	/**
	 * Properties.
	 *
	 * @return the property sources placeholder configurer.
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
