package com.cognitivescale.bank.repo.mongo.config;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@Configuration
@EnableMongoRepositories(basePackages = { "com.cognitivescale.bank.repo" })
public class MongoConfig extends AbstractMongoConfiguration {

	/** The Logger */
	private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

	/** The mongo db host. */
	@Value("#{'${DB_HOSTS:10.15.0.133:27017}'}")
	private String mongoDbHosts;

	/** The database name. */
	@Value("${DB_DATABASE_NAME:bank_details}")
	private String databaseName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#
	 * getDatabaseName()
	 */
	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.mongodb.config.AbstractMongoConfiguration#mongo(
	 * )
	 */
	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient(getServerAddressListForCluster());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#
	 * mongoTemplate()
	 */
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongo(), databaseName);
	}

	/**
	 * Gets the server address list for cluster.
	 *
	 * @return the server address list for cluster
	 */
	private List<ServerAddress> getServerAddressListForCluster() {
		List<ServerAddress> mongosServerList = new ArrayList<ServerAddress>();
		if (!mongoDbHosts.isEmpty()) {
			logger.debug("Parsing {} url.", mongoDbHosts);
			String[] parsedMongoDbUrl = mongoDbHosts.split(":");
			if (parsedMongoDbUrl.length == 2) {
				logger.debug("Parsing {} url contians host name {} and port {}.", mongoDbHosts, parsedMongoDbUrl[0],
						parsedMongoDbUrl[1]);
				Integer portNumber = null;
				try {
					portNumber = Integer.parseInt(parsedMongoDbUrl[1].trim());
				} catch (NumberFormatException nfe) {
					logger.error(nfe.getMessage());
				}
				if (portNumber != null) {
					logger.debug("Adding host {} with port {} as a mongoDB server.", parsedMongoDbUrl[0], portNumber);
					try {
						mongosServerList.add(new ServerAddress(parsedMongoDbUrl[0].trim(), portNumber));
					} catch (UnknownHostException e) {// NOSONAR
						logger.error(e.getMessage());
					}
				} else {
					logger.debug("{} specifies as mongoDB port number could not be converted to Integer.",
							parsedMongoDbUrl[1]);
				}
			}
		}

		return mongosServerList;
	}
}
