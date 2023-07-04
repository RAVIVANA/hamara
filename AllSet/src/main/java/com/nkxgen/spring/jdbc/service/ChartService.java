package com.nkxgen.spring.jdbc.service;

import java.util.List;

public interface ChartService {
	List<String> getAccountLabels();

	List<String> getLoanLabels();

	List<Integer> getAccountData();

	List<Integer> getLoanData();
}
