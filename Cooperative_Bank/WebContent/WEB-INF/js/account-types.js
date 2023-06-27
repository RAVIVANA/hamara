function submitAccountType() {

	var accountType = document.getElementById("accountType").value;
	switch (accountType) {
		case "savings":

			$.ajax({
				url: 'get_savings',
				method: 'get',
				data: { accountType: accountType },
				success: function(resultText) {
					$('#row').html(resultText);
					document.getElementById("id").classList.add("hidden");

				},
				error: function(jqXHR, exception) {
					console.log('Error occurred!');
				}
			})

			console.log("Savings Account selected");
			break;
		case "fixed":
			$.ajax({
				url: 'get_accounts',
				method: 'get',
				data: { accountType: accountType },
				success: function(resultText) {
					$('#row').html(resultText);
					document.getElementById("id").classList.add("hidden");

				},
				error: function(jqXHR, exception) {
					console.log('Error occurred!');
				}
			})
			console.log("Fixed Deposit selected");
			break;
		case "recurrent":
			$.ajax({
				url: 'get_accounts',
				method: 'get',
				data: { accountType: accountType },
				success: function(resultText) {
					$('#row').html(resultText);
					document.getElementById("id").classList.add("hidden");

				},
				error: function(jqXHR, exception) {
					console.log('Error occurred!');
				}
			})
			console.log("Recurring Deposit selected");
			break;
		default:
			break;
	}

}
function submit() {
	var accountType = document.getElementById("accountType").value;
	console.log(accountType);
	$.ajax({
		url: 'caluclate_intrest',
		method: 'get',
		data: { accountType: accountType },
		success: function(resultText) {
			$('#row').html(resultText);
			document.getElementById("id").classList.add("hidden");

		},
		error: function(jqXHR, exception) {
			console.log('Error occurred!');
		}
	})

	console.log("Savings Account selected");
}