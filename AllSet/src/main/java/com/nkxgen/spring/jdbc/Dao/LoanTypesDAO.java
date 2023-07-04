package com.nkxgen.spring.jdbc.Dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.nkxgen.spring.jdbc.DaoInterfaces.LoanTypesInterface;
import com.nkxgen.spring.jdbc.model.LoansTypes;

@Repository
@Transactional
public class LoanTypesDAO implements LoanTypesInterface {
	private static final Logger logger = LoggerFactory.getLogger(LoanTypesDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public List<LoansTypes> getAllLoans() {
		logger.info("Getting all loan types");

		String jpql = "SELECT l FROM LoansTypes l"; // Define a JPQL query to retrieve all loan types
		TypedQuery<LoansTypes> query = entityManager.createQuery(jpql, LoansTypes.class); // Create a typed query using
																							// the JPQL query and
																							// specifying the result
																							// type as 'LoansTypes'
		return query.getResultList(); // Execute the query and return a list of all loan types
	}

	public List<LoansTypes> getAllLoanDetails() {
		logger.info("Getting all loan details");

		String jpql = "SELECT l FROM LoansTypes l"; // Define a JPQL query to retrieve all loan types
		TypedQuery<LoansTypes> query = entityManager.createQuery(jpql, LoansTypes.class); // Create a typed query using
																							// the JPQL query and
																							// specifying the result
																							// type as 'LoansTypes'
		return query.getResultList(); // Execute the query and return a list of all loan types
	}

	public LoansTypes getSelectedLoanDetails(int loanType) {
		logger.info("Getting selected loan details for loan type: {}", loanType);
		LoansTypes loan = entityManager.find(LoansTypes.class, loanType); // Find the loan type object with the given
																			// loanType using the entity manager
		System.out.println("im in the dao of loan types");
		System.out.println(loan.getLoanType());
		System.out.println(loan.getDescriptionForm());
		return loan; // Return the found loan type object
	}

	public void save(LoansTypes LoansTypes) {
		if (LoansTypes.getLoanType() != null && LoansTypes.getDescriptionForm() != null) {
			entityManager.merge(LoansTypes); // Merge the account type entity with the persistence context
			logger.info("Loan type saved successfully");
		} else {
			// Handle the case where either accountType or descriptionForm is null
			// You can throw an exception, log an error, or perform any appropriate action
			System.err.println("Invalid account type data. Account type or description form is null.");
		} // Merge the 'LoansTypes' object with the entity manager to update it in the
			// data store
	}

}
