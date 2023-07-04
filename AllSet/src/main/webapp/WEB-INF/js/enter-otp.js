var timer;

function moveToNextInput(nextInput) {
	var currentInput = document.getElementById("digit" + nextInput);
	var nextInputId = "digit" + (nextInput + 1);

	if (currentInput.value.length === 1) {
		document.getElementById(nextInputId).focus();
	}
}

function populateHiddenField() {
	var otpDigits = "";
	for (var i = 1; i <= 6; i++) {
		otpDigits += document.getElementById("digit" + i).value;
	}
	document.getElementById("otp").value = otpDigits;
}

function performActions(event) {
	event.preventDefault();
	populateHiddenField();
	document.getElementById("otpForm").submit();
}

function startTimer() {
	var seconds = 30;
	timer = setInterval(function() {
		seconds--;
		document.getElementById("timer").textContent = seconds + " seconds remaining to resend otp";

		if (seconds <= 0) {
			clearInterval(timer);
			document.getElementById("timer").style.display = "none";
			document.getElementById("resendButton").style.display = "block";
		}
	}, 1000);
}

function resendOTP() {

	// Perform actions to resend OTP
	// Add your code here to resend the OTP
	$.ajax({
		url: 'enterOTP',
		type: 'GET',
		success: function(response) {
			clearInterval(timer); // Clear the existing timer
			document.getElementById("timer").textContent = "30 seconds remaining to resend otp";
			document.getElementById("timer").style.display = "block";
			document.getElementById("resendButton").style.display = "none";
			startTimer(); // Start the timer again
		},
		error: function(jqXHR, textStatus, errorThrown) {
			// Handle the error
			console.error('Third server request error:', textStatus, errorThrown);
		}
	});
}

// Start the timer on page load
startTimer();