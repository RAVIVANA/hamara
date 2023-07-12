package com.nkxgen.spring.jdbc.ViewModels;

import java.util.List;

public class GraphModel {
	private List<Integer> accountData;
	private List<Integer> loanData;
	private List<String> accountLabels;
	private List<String> loanLabels;

	public List<Integer> getAccountData() {
		return accountData;
	}

	public void setAccountData(List<Integer> accountData) {
		this.accountData = accountData;
	}

	public List<Integer> getLoanData() {
		return loanData;
	}

	public void setLoanData(List<Integer> loanData) {
		this.loanData = loanData;
	}

	public List<String> getAccountLabels() {
		return accountLabels;
	}

	public void setAccountLabels(List<String> accountLabels) {
		this.accountLabels = accountLabels;
	}

	public List<String> getLoanLabels() {
		return loanLabels;
	}

	public void setLoanLabels(List<String> loanLabels) {
		this.loanLabels = loanLabels;
	}
}
