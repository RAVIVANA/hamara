
        var passwordField = document.getElementById("password");
        var confirmPasswordField = document.getElementById("confirmPassword");
        var passwordError = document.getElementById("passwordError");
        var confirmPasswordError = document.getElementById("confirmPasswordError");
        var submitButton = document.querySelector('input[type="submit"]');
        submitButton.disabled = true; // Disable submit button by default

        // Password validation rules
        var uppercaseRegex = /^(?=.*[A-Z])/;
        var specialCharRegex = /^(?=.*[!@#$%^&*])/;
        var lengthRegex = /^.{8,}$/;

        passwordField.addEventListener("keyup", validatePassword);
        confirmPasswordField.addEventListener("keyup", validatePasswordMatch);

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

            validatePasswordMatch(); // Validate password match whenever password changes
        }

        function validatePasswordMatch() {
            var password = passwordField.value;
            var confirmPassword = confirmPasswordField.value;
            confirmPasswordError.textContent = "";

            if (password !== confirmPassword) {
                submitButton.disabled = true; // Disable submit button if passwords don't match
                confirmPasswordError.textContent = "Passwords do not match!";
            } else {
                submitButton.disabled = false; // Enable submit button if passwords match
            }
        }

