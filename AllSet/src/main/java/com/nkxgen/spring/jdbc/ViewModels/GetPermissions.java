package com.nkxgen.spring.jdbc.ViewModels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "permissions")
public class GetPermissions {

	@Id
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "role")
	private String role;

	@Column(name = "dashboard")
	private boolean dashboard;

	@Column(name = "accounts")
	private boolean accounts;

	@Column(name = "loans")
	private boolean loans;

	@Column(name = "transactions")
	private boolean transactions;

	@Column(name = "application")
	private boolean application;

	@Column(name = "users")
	private boolean users;

	@Column(name = "customers")
	private boolean customers;

	// public Permission(Long userId, String role, boolean dashboard, boolean accounts, boolean loans,
	// boolean transactions, boolean application, boolean users, boolean customers, boolean accountProcessing,
	// boolean auditLog) {
	//
	// this.userId = userId;
	// this.role = role;
	// this.dashboard = dashboard;
	// this.accounts = accounts;
	// this.loans = loans;
	// this.transactions = transactions;
	// this.application = application;
	// this.users = users;
	// this.customers = customers;
	// this.accountProcessing = accountProcessing;
	// this.auditLog = auditLog;
	// }

	@Column(name = "account_processing")
	private boolean accountProcessing;

	@Column(name = "audit_log")
	private boolean auditLog;

	public GetPermissions() {
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setDashboard(boolean dashboard) {
		this.dashboard = dashboard;
	}

	public void setAccounts(boolean accounts) {
		this.accounts = accounts;
	}

	public void setLoans(boolean loans) {
		this.loans = loans;
	}

	public void setTransactions(boolean transactions) {
		this.transactions = transactions;
	}

	public void setApplication(boolean application) {
		this.application = application;
	}

	public void setUsers(boolean users) {
		this.users = users;
	}

	public void setCustomers(boolean customers) {
		this.customers = customers;
	}

	@Override
	public String toString() {
		return "Permission [userId=" + userId + ", role=" + role + ", dashboard=" + dashboard + ", accounts=" + accounts
				+ ", loans=" + loans + ", transactions=" + transactions + ", application=" + application + ", users="
				+ users + ", customers=" + customers + ", accountProcessing=" + accountProcessing + ", auditLog="
				+ auditLog + "]";
	}

	public boolean isDashboard() {
		return dashboard;
	}

	public boolean isAccounts() {
		return accounts;
	}

	public boolean isLoans() {
		return loans;
	}

	public boolean isTransactions() {
		return transactions;
	}

	public boolean isApplication() {
		return application;
	}

	public boolean isUsers() {
		return users;
	}

	public boolean isCustomers() {
		return customers;
	}

	public boolean isAccountProcessing() {
		return accountProcessing;
	}

	public void setAccountProcessing(boolean accountProcessing) {
		this.accountProcessing = accountProcessing;
	}

	public boolean isAuditLog() {
		return auditLog;
	}

	public void setAuditLog(boolean auditLog) {
		this.auditLog = auditLog;
	}

	// Getters and setters

}