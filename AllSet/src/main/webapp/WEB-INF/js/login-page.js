document.addEventListener('DOMContentLoaded', () => {
			// Retrieve the error message from the URL parameter (if present)
			const urlParams = new URLSearchParams(window.location.search);
			const errorMessage = urlParams.get('error');

			// Display the error message in the HTML page
			const errorMessageElement = document.getElementById('errorMessages');
			if (errorMessage) {
				errorMessageElement.innerText = errorMessage;
			} else {
				errorMessageElement.style.display = 'none';
			}

			// Attach an event listener to the login form submission
			const loginForm = document.getElementById('loginForm');
			loginForm.addEventListener('submit', (event) => {
				event.preventDefault(); // Prevent the default form submission

				// Get the username and password from the form inputs
				const usernameInput = document.getElementsByName('username')[0];
				const passwordInput = document.getElementsByName('password')[0];
				const username = usernameInput.value;
				const password = passwordInput.value;

				// Example client-side validation
				if (username.trim() === '') {
					showErrorMessage('Please enter your username.');
					return;
				}

				if (password.trim() === '') {
					showErrorMessage('Please enter your password.');
					return;
				}

				// If validation passes, submit the form
				loginForm.submit();
			});
		});

		// Function to display an error message
		function showErrorMessage(message) {
			const errorMessageElement = document.getElementById('errorMessages');
			errorMessageElement.innerText = message;
			errorMessageElement.style.display = 'block';
		}