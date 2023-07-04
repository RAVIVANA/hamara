package com.nkxgen.spring.jdbc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.nkxgen.spring.jdbc.DaoInterfaces.UserCredentialsDAO;
import com.nkxgen.spring.jdbc.Exception.UsernameNotFoundException;
import com.nkxgen.spring.jdbc.Exception.WrongPasswordException;

public class LoginFilter implements Filter {

	@Autowired
	private UserCredentialsDAO userCredentialsDAO;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// Get the username and password from the request parameters
		String username = httpRequest.getParameter("username");
		String password = httpRequest.getParameter("password");

		try {
			// Perform your login verification logic here
			boolean isValidCredentials = userCredentialsDAO.userCredentialsCheck(username, password);

			if (isValidCredentials) {
				// Create a session and set an attribute to store the logged-in user information
				HttpSession session = httpRequest.getSession(true);
				session.setAttribute("username", username);

				// Continue the request processing
				chain.doFilter(request, response);
			}
		} catch (UsernameNotFoundException e) {
			// Set the error message as a session attribute
			String errorMessage = e.getMessage();
			HttpSession session = httpRequest.getSession();
			session.setAttribute("errorMessage", errorMessage);
			System.out.println(session.getAttribute("errorMessage"));
			// Redirect the request to the login page
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/");

		} catch (WrongPasswordException e) {
			// Set the error message as a session attribute
			String errorMessage = e.getMessage();
			HttpSession session = httpRequest.getSession();
			session.setAttribute("errorMessage", errorMessage);
			System.out.println(session.getAttribute("errorMessage"));
			// Redirect the request to the login page
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/");

		}
	}

	@Override
	public void destroy() {
		// Cleanup code goes here, if needed
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}
}
