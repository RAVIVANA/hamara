var passwordField = document.getElementById("password");
var confirmPasswordField = document.getElementById("confirmPassword");
var passwordError = document.getElementById("passwordError");
var confirmPasswordError = document.getElementById("confirmPasswordError");

// Password validation rules
var uppercaseRegex = /^(?=.*[A-Z])/;
var specialCharRegex = /^(?=.*[!@#$%^&*])/;
var lengthRegex = /^.{8,}$/;

passwordField.addEventListener("keyup", validatePassword);
confirmPasswordField.addEventListener("keyup", validateConfirmPassword);

function validatePassword() {
	var password = passwordField.value;
	passwordError.textContent = "";

	if (!uppercaseRegex.test(password)) {
		passwordError.textContent = "Password must contain at least one uppercase letter!";
	} else if (!specialCharRegex.test(password)) {
		passwordError.textContent = "Password must contain at least one special character!";
	} else if (!lengthRegex.test(password)) {
		passwordError.textContent = "Password must be at least 8 characters long!";
	}
}

function validateConfirmPassword() {
	var password = passwordField.value;
	var confirmPassword = confirmPasswordField.value;
	confirmPasswordError.textContent = "";

	if (password !== confirmPassword) {
		confirmPasswordError.textContent = "Passwords do not match!";
	}
}