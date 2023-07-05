package com.nkxgen.spring.jdbc.validation;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

import com.nkxgen.spring.jdbc.model.BankUser;

@Component
public class MailSenderImpl implements MailSender {
	public String send(String to_user) {
		String to = to_user; // Assign the value of the 'to_user' parameter to the 'to' variable
		String otp = generateOTP(); // Generate a random OTP
		String subject = "Password Reset"; // Set the email subject
		String body = "The OTP for the Password Reset: " + otp; // Set the email body
		sendEmail(to, subject, body); // Invoke the sendEmail method to send the email
		return otp; // Return the generated OTP
	}

	public void userAdded(BankUser bankUser, String userID) {
		String to = bankUser.getBusr_email(); // Assign the value of the 'to_user' parameter to the 'to' variable
		String subject = "Greetings From HAMARA BANK"; // Set the email subject

		String body = "Dear " + bankUser.getBusr_title() + ",\n\n"
				+ "Welcome to the Hamara Bank family! We are thrilled to have you on board as a valued member of our team.\n\n"
				+ "At Hamara Bank, we strive for excellence in providing exceptional banking services to our customers, and we believe that your skills and expertise will greatly contribute to our mission.\n\n"
				+ "As you embark on this exciting journey with us, we want you to know that we are committed to fostering a positive and inclusive work environment. We encourage open communication, collaboration, and personal growth, ensuring that you have all the support and resources needed to thrive in your role.\n\n"
				+ "Please take the time to familiarize yourself with our organization's values, goals, and the services we offer. We believe that together, we can make a significant impact in the lives of our customers and the communities we serve.\n\n"
				+ "Once again, welcome to Hamara Bank!\n\n" + "Here are your login details:\n" + "Username: "
				+ bankUser.getBusr_id() + "\n" + "Password: " + bankUser.getBusr_pwd() + "\n\n" + "Best regards,\n"
				+ userID + "\n" + bankUser.getBusr_desg() + "\n" + "Hamara Bank";

		sendEmail(to, subject, body); // Invoke the sendEmail method to send the email
	}

	private static void sendEmail(String to, String subject, String body) {
		String host = "smtp.gmail.com"; // SMTP server host (Gmail)
		int port = 587; // SMTP server port (Gmail)

		final String username = "pametineeraj2001@gmail.com"; // Your Gmail account username
		final String password = "agvxucccuiyrehqv"; // Your Gmail account password

		// Set properties
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true"); // Enable SMTP authentication
		props.put("mail.smtp.starttls.enable", "true"); // Enable TLS encryption
		props.put("mail.smtp.host", host); // Set the SMTP server host
		props.put("mail.smtp.port", port); // Set the SMTP server port

		// Create session
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Create message
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username)); // Set the sender's email address
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // Set the recipient's email
																						// address
			message.setSubject(subject); // Set the email subject
			message.setText(body); // Set the email body

			// Send message
			Transport.send(message); // Send the email

			System.out.println("Email sent successfully.");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private static String generateOTP() {
		Random random = new Random();
		String s = "";
		for (int i = 0; i < 6; i++) {
			int randomIndex = random.nextInt(10);
			s = s + String.valueOf(randomIndex);

		}

		return s;
	}

}
