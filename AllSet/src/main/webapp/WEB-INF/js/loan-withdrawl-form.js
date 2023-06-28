function validateAndProcessAccountNumber() {
	var input = document.getElementById("loanid");
	var value = input.value;

	if (value.length > 1) {
		input.value = value.slice(0, 1); 
	}
	if (value.length === 1) {
		processAccountNumber(value);
	}
}

function processAccountNumber(loanid) {
	document.getElementById('resulttable').innerHTML = "";
	$.ajax({
		url: 'getLoanDetails',
		method: 'post',
		data: {
			accountNumber: loanid
		},
		success: function(resultText) {
			$('#resulttable').html(resultText);
		},
		error: function(jqXHR, exception) {
			console.log('Error occurred!');
		}
	})
}

