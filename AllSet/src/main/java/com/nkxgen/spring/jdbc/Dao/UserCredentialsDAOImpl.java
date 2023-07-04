package com.nkxgen.spring.jdbc.Dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nkxgen.spring.jdbc.DaoInterfaces.UserCredentialsDAO;
import com.nkxgen.spring.jdbc.model.UserCredentials;

@Component
public class UserCredentialsDAOImpl implements UserCredentialsDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserCredentialsDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public boolean userCredentialsCheck(String username, String password) {
		String queryString = "SELECT uc FROM UserCredentials uc WHERE uc.username = :username AND uc.password = :password";
		TypedQuery<UserCredentials> query = entityManager.createQuery(queryString, UserCredentials.class); // Create a
																											// typed
																											// query to
																											// retrieve
																											// UserCredentials
																											// objects
		query.setParameter("username", username); // Set the parameter "username" in the query to the provided username
		query.setParameter("password", password); // Set the parameter "password" in the query to the provided password
		logger.info("Checking user credentials for username: {}", username);
		return query.getResultList().size() > 0; // Return true if the result list has at least one element, indicating
													// that the user credentials match, otherwise return false
	}

}
