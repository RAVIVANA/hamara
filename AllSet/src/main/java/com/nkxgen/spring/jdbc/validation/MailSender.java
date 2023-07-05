package com.nkxgen.spring.jdbc.validation;

import com.nkxgen.spring.jdbc.model.BankUser;

public interface MailSender {
	String send(String to_user);

	void userAdded(BankUser bankUser, String userID);

}
