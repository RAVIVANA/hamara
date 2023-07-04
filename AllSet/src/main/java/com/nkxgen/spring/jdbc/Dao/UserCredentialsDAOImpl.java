package com.nkxgen.spring.jdbc.Dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nkxgen.spring.jdbc.DaoInterfaces.UserCredentialsDAO;
import com.nkxgen.spring.jdbc.Exception.UsernameNotFoundException;
import com.nkxgen.spring.jdbc.Exception.WrongPasswordException;
import com.nkxgen.spring.jdbc.model.UserCredentials;

@Component
public class UserCredentialsDAOImpl implements UserCredentialsDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public boolean userCredentialsCheck(String username, String password)
			throws UsernameNotFoundException, WrongPasswordException {

		String queryString = "SELECT uc FROM UserCredentials uc WHERE uc.username = :username AND uc.password = :password";
		TypedQuery<UserCredentials> query = entityManager.createQuery(queryString, UserCredentials.class);
		query.setParameter("username", username);
		query.setParameter("password", password);
		int c = query.getResultList().size();

		String queryString1 = "SELECT uc FROM UserCredentials uc WHERE uc.username = :username";
		TypedQuery<UserCredentials> query1 = entityManager.createQuery(queryString1, UserCredentials.class);
		query1.setParameter("username", username);
		int count = query1.getResultList().size();

		if (c > 0) {
			return true;
		} else if (count == 0) {
			throw new UsernameNotFoundException("User not found");
		} else if (count > 0) {
			throw new WrongPasswordException("Wrong Password");
		} else {
			throw new RuntimeException("Error occurred while checking user credentials");
		}
	}
}
